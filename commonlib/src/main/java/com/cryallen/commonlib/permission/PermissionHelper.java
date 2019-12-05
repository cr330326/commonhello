package com.cryallen.commonlib.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.cryallen.commonlib.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * 权限申请帮助类
 * @author Allen
 * @DATE 2019-10-30
 ***/
public class PermissionHelper {
	public final static String TAG = PermissionHelper.class.getSimpleName();

	private final static int PERMISSION_GRANTED = 1;
	private final static int PERMISSION_RATIONAL = 2;
	private final static int PERMISSION_DENIED = 3;

	private static PermissionHelper instance;
	private IPermissionCallBack mPermissionResultCallBack;
	private List<String> mGrantedList;

	private PermissionHelper() { }

	public static PermissionHelper getInstance() {
		if (instance == null) {
			synchronized (PermissionHelper.class) {
				if (instance == null) {
					instance = new PermissionHelper();
				}
			}
		}
		return instance;
	}

	@TargetApi(Build.VERSION_CODES.M)
	private int checkPermissionStatus(@NonNull Activity activity, String permission) {
		if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
			return PERMISSION_GRANTED;
		} else {
			if (activity.shouldShowRequestPermissionRationale(permission)) {
				return PERMISSION_RATIONAL;
			} else {
				return PERMISSION_DENIED;
			}
		}
	}

	/**
	 * 请求权限核心方法
	 */
	@RequiresApi(Build.VERSION_CODES.M)
	public void requestWithActivity(Activity activity, String[] permissions, IPermissionCallBack callBack) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return;
		}
		mPermissionResultCallBack = callBack;
		if (!checkEnv()) {
			onResult(Arrays.asList(permissions), new ArrayList<String>(), new ArrayList<String>());
		}

		List<String> needRationList = new ArrayList<>();
		List<String> deniedList = new ArrayList<>();
		List<String> grantedList = new ArrayList<>();

		for (String permission : permissions) {
			int result = checkPermissionStatus(activity, permission);
			LogUtils.d(TAG,"permission name:" + permission);
			LogUtils.d(TAG,"permission result:" + result);
			switch (result) {
				case PERMISSION_GRANTED:
					grantedList.add(permission);
					break;
				case PERMISSION_RATIONAL:
					needRationList.add(permission);
					break;
				case PERMISSION_DENIED:
					deniedList.add(permission);
					break;
				default:
					break;
			}
		}

		if (needRationList.size() != 0 || deniedList.size() != 0) {
            if (needRationList.size() != 0 && callBack != null) {
                if (callBack.onRational(needRationList)) {
                    //阻塞此请求
                    return;
                }
            }
			mGrantedList = grantedList;
			List<String> toReq = new ArrayList<>(deniedList);
			toReq.addAll(needRationList);
			requestPermissions(activity, toReq.toArray(new String[toReq.size()]));
		} else {
			onResult(grantedList, needRationList, deniedList);
		}
	}

	private boolean checkEnv() {
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("request permission only can run in MainThread!");
		}
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	public void requestWithContext(Context context, String[] permissions, IPermissionCallBack callBack){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return;
		}
		mPermissionResultCallBack = callBack;
		requestPermissions(context,permissions);
	}

	/**
	 * 通过开启一个新的activity作为申请权限的媒介
	 */
	private void requestPermissions(Context context, String[] permissions) {
		LogUtils.d(TAG,"requestPermissions permissions:" + Arrays.asList(permissions).toString());
		Intent intent = new Intent(context, PermissionHelpActivity.class);
		intent.putExtra("permissions", permissions);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public void onHelperResult(List<String> acceptPermissionList, List<String> needRationalPermissionList, List<String> totallyDeniedPermissionList) {
		if (mGrantedList == null){
			mGrantedList = new ArrayList<>();
		}
		mGrantedList.addAll(acceptPermissionList);
		onResult(mGrantedList, needRationalPermissionList, totallyDeniedPermissionList);
	}

	private void onResult(List<String> acceptPermissionList, List<String> needRationalPermissionList, List<String> deniedPermissionList) {
		if (mPermissionResultCallBack == null) {
			return;
		}
		mPermissionResultCallBack.onResult(acceptPermissionList, needRationalPermissionList, deniedPermissionList);
	}

	public void releaseRef() {
		mPermissionResultCallBack = null;
		mGrantedList = null;
	}
}
