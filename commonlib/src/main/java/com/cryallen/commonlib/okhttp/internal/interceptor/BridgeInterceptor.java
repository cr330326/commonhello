package com.cryallen.commonlib.okhttp.internal.interceptor;

import com.cryallen.commonlib.okhttp.abstarct.AbsSingleton;
import com.cryallen.commonlib.okhttp.internal.Version;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 内部拦截器
 * @author Allen
 * @DATE 2019-09-19
 ***/
public class BridgeInterceptor implements Interceptor  {
	private BridgeInterceptor(){}

	public static AbsSingleton<BridgeInterceptor> instance = new AbsSingleton<BridgeInterceptor>() {
		@Override
		protected BridgeInterceptor create() {
			return new BridgeInterceptor();
		}
	};

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		Request.Builder builder = request.newBuilder();

		if (request.header("User-Agent") == null) {
			builder.header("User-Agent", Version.userAgent());
		}

		return chain.proceed(builder.build());
	}
}
