package com.cryallen.commonlib.rxhttp.interceptor;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Allen on 2017/5/3.
 * @author Allen
 * 请求拦截器  统一添加请求头使用
 */

public abstract class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Map<String, String> headerMap = buildHeaders();
        if (headerMap == null || headerMap.isEmpty()) {
            return chain.proceed(request);
        } else {
            Headers headers = request.headers();
            Request.Builder builder = request.newBuilder();
            if (headers != null) {
                for (String key : headerMap.keySet()) {
                    builder.header(key, headerMap.get(key));
                }
            }
            return chain.proceed(builder.build());
        }
    }

    public abstract Map<String, String> buildHeaders();
}
