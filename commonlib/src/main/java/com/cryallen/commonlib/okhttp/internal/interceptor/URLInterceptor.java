package com.cryallen.commonlib.okhttp.internal.interceptor;

import java.net.URL;

import okhttp3.HttpUrl;

/***
 * URI拦截器类
 * @author Allen
 * @DATE 2019-09-19
 ***/
public interface URLInterceptor {

	String interceptor(String origin);

	HttpUrl interceptor(HttpUrl origin);

	URL interceptor(URL origin);
}
