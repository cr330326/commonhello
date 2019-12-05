package com.cryallen.commonlib.okhttp;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;

import androidx.annotation.NonNull;

import com.cryallen.commonlib.okhttp.internal.interceptor.BridgeInterceptor;
import com.cryallen.commonlib.okhttp.internal.interceptor.HttpLoggingInterceptor;
import com.cryallen.commonlib.okhttp.internal.interceptor.URLInterceptor;
import com.cryallen.commonlib.okhttp.internal.log.LoggerImpl;
import com.cryallen.commonlib.okhttp.utils.HttpsUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
/**
 * 封装好的网络请求帮助类
 * Created by chenran3 on 2017/10/25.
 */
public final class OkHttpHelper {
	private volatile static OkHttpHelper instance;
	private OkHttpClient.Builder mOkBuilder;
	private OkHttpClient mInnerClient;
	private URLInterceptor mURLInterceptor;
	private final HttpLoggingInterceptor mLoggingInterceptor = new HttpLoggingInterceptor(LoggerImpl.instance.get());

	/**
	 * 获取单例句柄
	 * @return
	 */
	public static OkHttpHelper getInstance() {
		if (instance == null) {
			synchronized (OkHttpHelper.class) {
				if (instance == null) {
					instance = new OkHttpHelper();
				}
			}
		}
		return instance;
	}

	private OkHttpHelper() {
		mOkBuilder = new OkHttpClient.Builder().addInterceptor(BridgeInterceptor.instance.get());
	}

	/**
	 * OkHttpClient
	 */
	public OkHttpHelper customOkHttpClient(@NonNull OkHttpClient client) {
		mOkBuilder = client.newBuilder().addInterceptor(BridgeInterceptor.instance.get());
		return this;
	}


	/**
	 * Set logging level
	 */
	public OkHttpHelper loggingLevel(HttpLoggingInterceptor.Level level) {
		mLoggingInterceptor.setLevel(level);
		LoggerImpl.instance.get().setLevel(level);
		if (!mOkBuilder.interceptors().contains(mLoggingInterceptor)) {
			mOkBuilder.addInterceptor(mLoggingInterceptor);
		}
		return this;
	}

	/**
	 * Set cache Dir
	 */
	public OkHttpHelper cache(Context context, String dirName) {
		File cache = new File(context.getApplicationContext().getCacheDir(), dirName);
		if (!cache.exists()) {
			//noinspection ResultOfMethodCallIgnored
			cache.mkdirs();
		}
		long size = 5 * 1024 * 1024;
		try {
			StatFs statFs = new StatFs(cache.getAbsolutePath());
			long count, blockSize;
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
				count = statFs.getBlockCountLong();
				blockSize = statFs.getBlockSizeLong();
			} else {
				count = statFs.getBlockCount();
				blockSize = statFs.getBlockSize();
			}
			long available = count * blockSize;
			// Target 2% of the total space.
			size = available / 50;
		} catch (IllegalArgumentException ignored) {
		}
		// Bound inside min/max size for disk cache.
		size = Math.max(Math.min(size, size * 10), size);
		mOkBuilder.cache(new Cache(cache, size));
		return this;
	}

	/**
	 * Set connect, read and write time with {@link TimeUnit#SECONDS}
	 */
	public OkHttpHelper timeout(int timeout) {
		timeout(timeout, TimeUnit.SECONDS);
		return this;
	}

	public OkHttpHelper timeout(int timeout, TimeUnit unit) {
		connectTimeout(timeout, unit);
		readTimeout(timeout, unit);
		writeTimeout(timeout, unit);
		return this;
	}

	public OkHttpHelper connectTimeout(long timeout, TimeUnit unit) {
		mOkBuilder.connectTimeout(timeout, unit);
		return this;
	}

	public OkHttpHelper readTimeout(long timeout, TimeUnit unit) {
		mOkBuilder.readTimeout(timeout, unit);
		return this;
	}

	public OkHttpHelper writeTimeout(long timeout, TimeUnit unit) {
		mOkBuilder.writeTimeout(timeout, unit);
		return this;
	}

	public OkHttpHelper setURLInterceptor(URLInterceptor interceptor) {
		this.mURLInterceptor = interceptor;
		return this;
	}

	/**
	 * Trust all certificate for debug
	 */
	public OkHttpHelper trustAllCertificate() {
		HttpsUtils.trustAllCertificate(mOkBuilder);
		return this;
	}

	/**
	 * Set Certificate
	 */
	public OkHttpHelper setCertificates(InputStream... certificates) {
		return setCertificates(certificates, null, null);
	}

	/**
	 * Set Certificate
	 */
	public OkHttpHelper setCertificates(X509TrustManager trustManager, InputStream bksFile, String password) {
		try {
			HttpsUtils.setCertificates(mOkBuilder, trustManager, null, bksFile, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	/**
	 * Set Certificate
	 */
	public OkHttpHelper setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
		try {
			HttpsUtils.setCertificates(mOkBuilder, null, certificates, bksFile, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public OkHttpHelper hostnameVerifier(HostnameVerifier hostnameVerifier) {
		mOkBuilder.hostnameVerifier(hostnameVerifier);
		return this;
	}

	/**
	 * @return Current client.
	 */
	public OkHttpClient client() {
		if (mOkBuilder == null) {
			throw new IllegalArgumentException("OkHttpClient cannot be null, please call the MHttp#client(OkHttpClient client) method first.");
		}
		if (mInnerClient == null) {
			mInnerClient = mOkBuilder.build();
		}
		return mInnerClient;
	}

	/**
	 * Cancel all request.
	 */
	public OkHttpHelper cancelAll() {
		client().dispatcher().cancelAll();
		return this;
	}

	/**
	 * Cancel request with {@code tag}
	 */
	public OkHttpHelper cancel(Object tag) {
		for (Call call : client().dispatcher().queuedCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
		for (Call call : client().dispatcher().runningCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
		return this;
	}

	public String proceedURL(String url) {
		if (mURLInterceptor != null) {
			return mURLInterceptor.interceptor(url);
		}
		return url;
	}

	public HttpUrl proceedURL(HttpUrl url) {
		if (mURLInterceptor != null) {
			return mURLInterceptor.interceptor(url);
		}
		return url;
	}

	public URL proceedURL(URL url) {
		if (mURLInterceptor != null) {
			return mURLInterceptor.interceptor(url);
		}
		return url;
	}
}
