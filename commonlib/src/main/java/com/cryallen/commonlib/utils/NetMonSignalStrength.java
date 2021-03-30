package com.cryallen.commonlib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/***
 * 网络网格状态封装
 * @author Allen
 * @DATE 2020/6/5
 ***/
public class NetMonSignalStrength {
	private static final String TAG = NetMonSignalStrength.class.getSimpleName();
	public static final int UNKNOWN = Integer.MAX_VALUE;
	public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
	//Use int max, as -1 is a valid value in signal strength
	private static final int INVALID = 0x7FFFFFFF;

	private static final int SIGNAL_STRENGTH_POOR = 1;
	private static final int SIGNAL_STRENGTH_MODERATE = 2;
	private static final int SIGNAL_STRENGTH_GOOD = 3;
	private static final int SIGNAL_STRENGTH_GREAT = 4;

	private static final int GSM_SIGNAL_STRENGTH_GREAT = 12;
	private static final int GSM_SIGNAL_STRENGTH_GOOD = 8;
	private static final int GSM_SIGNAL_STRENGTH_MODERATE = 5;

	private final TelephonyManager mTelephonyManager;

	public NetMonSignalStrength(Context context) {
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * @return a value between 0 {@link #SIGNAL_STRENGTH_NONE_OR_UNKNOWN} and 4 {@link #SIGNAL_STRENGTH_GREAT}.
	 */
	public int getLevel(SignalStrength signalStrength) {
		LogUtils.d(TAG, "getLevel " + signalStrength);
		if (signalStrength.isGsm()) {
			return getGSMSignalStrength(signalStrength);
		} else {
			return getCDMASignalStrength(signalStrength);
		}
	}

	/**
	 * @return the signal strength as dBm.
	 */
	public int getDbm(SignalStrength signalStrength) {
		LogUtils.d(TAG, "getDbm " + signalStrength);
		int dBm;

		if (signalStrength.isGsm()) {
			if (getLteLevel(signalStrength) == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
				dBm = getGsmDbm(signalStrength);
			} else {
				dBm = getLteDbm(signalStrength);
			}
		} else {
			int cdmaDbm = signalStrength.getCdmaDbm();
			int evdoDbm = signalStrength.getEvdoDbm();

			return evdoDbm == -120 ? cdmaDbm : cdmaDbm == -120 ? evdoDbm : cdmaDbm < evdoDbm ? cdmaDbm : evdoDbm;
		}
		return dBm;
	}


	private int getCDMASignalStrength(SignalStrength signalStrength) {
		LogUtils.d(TAG, "getCDMASignalStrength " + signalStrength);
		int level;

		int cdmaLevel = getCdmaLevel(signalStrength);
		int evdoLevel = getEvdoLevel(signalStrength);
		if (evdoLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
			/* We don't know evdo, use cdma */
			level = getCdmaLevel(signalStrength);
		} else if (cdmaLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
			/* We don't know cdma, use evdo */
			level = getEvdoLevel(signalStrength);
		} else {
			/* We know both, use the lowest level */
			level = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
		}
		LogUtils.d(TAG, "getLevel=" + level);
		return level;
	}

	/**
	 * Get cdma as level 0..4
	 */
	private int getCdmaLevel(SignalStrength signalStrength) {
		final int cdmaDbm = signalStrength.getCdmaDbm();
		final int cdmaEcio = signalStrength.getCdmaEcio();
		int levelDbm;
		int levelEcio;

		if (cdmaDbm >= -75) levelDbm = SIGNAL_STRENGTH_GREAT;
		else if (cdmaDbm >= -85) levelDbm = SIGNAL_STRENGTH_GOOD;
		else if (cdmaDbm >= -95) levelDbm = SIGNAL_STRENGTH_MODERATE;
		else if (cdmaDbm >= -100) levelDbm = SIGNAL_STRENGTH_POOR;
		else
			levelDbm = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

		// Ec/Io are in dB*10
		if (cdmaEcio >= -90) levelEcio = SIGNAL_STRENGTH_GREAT;
		else if (cdmaEcio >= -110) levelEcio = SIGNAL_STRENGTH_GOOD;
		else if (cdmaEcio >= -130) levelEcio = SIGNAL_STRENGTH_MODERATE;
		else if (cdmaEcio >= -150) levelEcio = SIGNAL_STRENGTH_POOR;
		else
			levelEcio = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

		int level = levelDbm < levelEcio ? levelDbm : levelEcio;
		LogUtils.d(TAG, "getCdmaLevel=" + level);
		return level;
	}

	/**
	 * Get Evdo as level 0..4
	 */
	private int getEvdoLevel(SignalStrength signalStrength) {
		int evdoDbm = signalStrength.getEvdoDbm();
		int evdoSnr = signalStrength.getEvdoSnr();
		int levelEvdoDbm;
		int levelEvdoSnr;

		if (evdoDbm >= -65) levelEvdoDbm = SIGNAL_STRENGTH_GREAT;
		else if (evdoDbm >= -75) levelEvdoDbm = SIGNAL_STRENGTH_GOOD;
		else if (evdoDbm >= -90) levelEvdoDbm = SIGNAL_STRENGTH_MODERATE;
		else if (evdoDbm >= -105) levelEvdoDbm = SIGNAL_STRENGTH_POOR;
		else
			levelEvdoDbm = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

		if (evdoSnr >= 7) levelEvdoSnr = SIGNAL_STRENGTH_GREAT;
		else if (evdoSnr >= 5) levelEvdoSnr = SIGNAL_STRENGTH_GOOD;
		else if (evdoSnr >= 3) levelEvdoSnr = SIGNAL_STRENGTH_MODERATE;
		else if (evdoSnr >= 1) levelEvdoSnr = SIGNAL_STRENGTH_POOR;
		else
			levelEvdoSnr = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

		int level = levelEvdoDbm < levelEvdoSnr ? levelEvdoDbm : levelEvdoSnr;
		LogUtils.d(TAG, "getEvdoLevel=" + level);
		return level;
	}

	/**
	 * Get signal level as an int from 0..4
	 */
	private int getGSMSignalStrength(SignalStrength signalStrength) {
		LogUtils.d(TAG, "getGSMSignalStrength " + signalStrength);

		// ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
		// asu = 0 (-113dB or less) is very weak
		// signal, its better to show 0 bars to the user in such cases.
		// asu = 99 is a special case, where the signal strength is unknown.
		int asu = signalStrength.getGsmSignalStrength();
		LogUtils.d(TAG, "getGSMSignalStrength asu " + asu);
		if (asu > 0) {
			if (asu <= 2 || asu == 99) return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
			else if (asu >= GSM_SIGNAL_STRENGTH_GREAT) return SIGNAL_STRENGTH_GREAT;
			else if (asu >= GSM_SIGNAL_STRENGTH_GOOD) return SIGNAL_STRENGTH_GOOD;
			else if (asu >= GSM_SIGNAL_STRENGTH_MODERATE) return SIGNAL_STRENGTH_MODERATE;
			else
				return SIGNAL_STRENGTH_POOR;
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// On a Huawei P9 Lite, getGsmSignalStrength() always returns 0, but the private
			// apis getGsmLevel() and getLteLevel() don't.  Let's try them.
			try {
				if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
					Method methodGetLteLevel = SignalStrength.class.getMethod("getLteLevel");
					return (Integer) methodGetLteLevel.invoke(signalStrength);
				} else {
					Method methodGetGsmLevel = SignalStrength.class.getMethod("getGsmLevel");
					return (Integer) methodGetGsmLevel.invoke(signalStrength);
				}
			} catch (Throwable t) {
				LogUtils.e(TAG, "getGsmLevel or getLteLevel failed: " + t.getMessage(), t);
				return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
			}
		} else {
			return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		}
	}

	/**
	 * Get the GSM signal strength as dBm
	 */
	private int getGsmDbm(SignalStrength signalStrength) {
		LogUtils.v(TAG, "getGsmDbm" + signalStrength);

		int level = signalStrength.getGsmSignalStrength();
		int asu = level == 99 ? SIGNAL_STRENGTH_NONE_OR_UNKNOWN : level;
		if (asu != SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
			return -113 + 2 * asu;
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// On the Huawei P9 Lite, getGsmSignalStrength() returns 0 but getGsmDbm() returns a value.
			try {
				Method methodGetGsmDbm = SignalStrength.class.getMethod("getGsmDbm");
				return (Integer) methodGetGsmDbm.invoke(signalStrength);
			} catch (Throwable t) {
				LogUtils.e(TAG, "Couldn't execute getGsmDbm() " + t.getMessage(), t);
				return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
			}
		} else {
			return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		}
	}

	/**
	 * Get LTE as level 0..4
	 */
	private int getLteLevel(SignalStrength signalStrength) {
		LogUtils.d(TAG, "getLteLevel " + signalStrength);
		// For now there's no other way besides reflection :( The getLteLevel() method
		// in the SignalStrength class access private fields.
		// On some Samsung devices, getLteLevel() can actually return 4 (the highest signal strength) even if we're not on Lte.
		// It seems that Samsung has reimplemented getLteLevel(). So we add an extra check to make sure we only use Lte level if we're on LTE.

		if (mTelephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_LTE) {
			LogUtils.d(TAG, "getLteLevel: returning " + SIGNAL_STRENGTH_NONE_OR_UNKNOWN + " because we're not on Lte");
			return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		}
		try {
			Method methodGetLteLevel = SignalStrength.class.getMethod("getLteLevel");
			return (Integer) methodGetLteLevel.invoke(signalStrength);
		} catch (Throwable t) {
			LogUtils.e(TAG, "getLteLevel failed: " + t.getMessage(), t);
			return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		}
	}

	/**
	 * Get LTE as dBm
	 */
	private int getLteDbm(SignalStrength signalStrength) {
		LogUtils.d(TAG, "getLteDbm " + signalStrength);
		// For now there's no other way besides reflection :( The getLteDbm() method
		// in the SignalStrength class returns a private field which is not
		// accessible in any public, non-hidden methods.
		try {
			Method methodGetLteDbm = SignalStrength.class.getMethod("getLteDbm");
			return (Integer) methodGetLteDbm.invoke(signalStrength);
		} catch (Throwable t) {
			LogUtils.e(TAG, "getLteDbm failed: " + t.getMessage(), t);
			return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		}
	}

	/**
	 * Get the signal level as an asu value between 0..31, 99 is unknown
	 */
	public int getAsuLevel(SignalStrength signalStrength) {
		int asuLevel;
		if (signalStrength.isGsm()) {
			if (getLteLevel(signalStrength) == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
				asuLevel = signalStrength.getGsmSignalStrength();
			} else {
				asuLevel = getLteAsuLevel(signalStrength);
			}
		} else {
			int cdmaAsuLevel = getCdmaAsuLevel(signalStrength);
			int evdoAsuLevel = getEvdoAsuLevel(signalStrength);
			if (evdoAsuLevel == 0) {
				/* We don't know evdo use, cdma */
				asuLevel = cdmaAsuLevel;
			} else if (cdmaAsuLevel == 0) {
				/* We don't know cdma use, evdo */
				asuLevel = evdoAsuLevel;
			} else {
				/* We know both, use the lowest level */
				asuLevel = cdmaAsuLevel < evdoAsuLevel ? cdmaAsuLevel : evdoAsuLevel;
			}
		}
		LogUtils.d(TAG, "getAsuLevel=" + asuLevel);
		return asuLevel;
	}

	@TargetApi(17)
	public int getLteRsrq(SignalStrength signalStrength) {
		// Two hacky ways to attempt to get the rsrq
		// First hacky way: reflection on the signalStrength object
		try {
			@SuppressLint("PrivateApi")  // Yes, I know it's a hack, thanks.
					Method method = SignalStrength.class.getDeclaredMethod("getLteRsrq");
			int rsrq = (Integer) method.invoke(signalStrength);
			LogUtils.d(TAG, "getLteRsrq: found " + rsrq + " using SignalStrength.getLteRsrq()");
			if (rsrq < 0) return rsrq;
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LogUtils.e(TAG, "getLteRsrq Could not get rsrq", e);
		}
		return SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
	}

	/**
	 * Get the LTE signal level as an asu value between 0..97, 99 is unknown
	 * Asu is calculated based on 3GPP RSRP. Refer to 3GPP 27.007 (Ver 10.3.0) Sec 8.69
	 */
	private int getLteAsuLevel(SignalStrength signalStrength) {
		int lteAsuLevel;
		int lteDbm = getLteDbm(signalStrength);
		/*
		 * 3GPP 27.007 (Ver 10.3.0) Sec 8.69
		 * 0   -140 dBm or less
		 * 1   -139 dBm
		 * 2...96  -138... -44 dBm
		 * 97  -43 dBm or greater
		 * 255 not known or not detectable
		 */
		/*
		 * validateInput will always give a valid range between -140 t0 -44 as
		 * per ril.h. so RSRP >= -43 & <-140 will fall under asu level 255
		 * and not 97 or 0
		 */
		if (lteDbm == INVALID) lteAsuLevel = 255;
		else
			lteAsuLevel = lteDbm + 140;
		LogUtils.d(TAG, "Lte Asu level: " + lteAsuLevel);
		return lteAsuLevel;
	}

	/**
	 * Get the cdma signal level as an asu value between 0..31, 99 is unknown
	 */
	private int getCdmaAsuLevel(SignalStrength signalStrength) {
		final int cdmaDbm = signalStrength.getCdmaDbm();
		final int cdmaEcio = signalStrength.getCdmaEcio();
		int cdmaAsuLevel;
		int ecioAsuLevel;

		if (cdmaDbm >= -75) cdmaAsuLevel = 16;
		else if (cdmaDbm >= -82) cdmaAsuLevel = 8;
		else if (cdmaDbm >= -90) cdmaAsuLevel = 4;
		else if (cdmaDbm >= -95) cdmaAsuLevel = 2;
		else if (cdmaDbm >= -100) cdmaAsuLevel = 1;
		else
			cdmaAsuLevel = 99;

		// Ec/Io are in dB*10
		if (cdmaEcio >= -90) ecioAsuLevel = 16;
		else if (cdmaEcio >= -100) ecioAsuLevel = 8;
		else if (cdmaEcio >= -115) ecioAsuLevel = 4;
		else if (cdmaEcio >= -130) ecioAsuLevel = 2;
		else if (cdmaEcio >= -150) ecioAsuLevel = 1;
		else
			ecioAsuLevel = 99;

		int level = cdmaAsuLevel < ecioAsuLevel ? cdmaAsuLevel : ecioAsuLevel;
		LogUtils.d(TAG, "getCdmaAsuLevel=" + level);
		return level;
	}

	/**
	 * Get the evdo signal level as an asu value between 0..31, 99 is unknown
	 */
	private int getEvdoAsuLevel(SignalStrength signalStrength) {
		int evdoDbm = signalStrength.getEvdoDbm();
		int evdoSnr = signalStrength.getEvdoSnr();
		int levelEvdoDbm;
		int levelEvdoSnr;

		if (evdoDbm >= -65) levelEvdoDbm = 16;
		else if (evdoDbm >= -75) levelEvdoDbm = 8;
		else if (evdoDbm >= -85) levelEvdoDbm = 4;
		else if (evdoDbm >= -95) levelEvdoDbm = 2;
		else if (evdoDbm >= -105) levelEvdoDbm = 1;
		else
			levelEvdoDbm = 99;

		if (evdoSnr >= 7) levelEvdoSnr = 16;
		else if (evdoSnr >= 6) levelEvdoSnr = 8;
		else if (evdoSnr >= 5) levelEvdoSnr = 4;
		else if (evdoSnr >= 3) levelEvdoSnr = 2;
		else if (evdoSnr >= 1) levelEvdoSnr = 1;
		else
			levelEvdoSnr = 99;

		int level = levelEvdoDbm < levelEvdoSnr ? levelEvdoDbm : levelEvdoSnr;
		LogUtils.d(TAG, "getEvdoAsuLevel=" + level);
		return level;
	}
}
