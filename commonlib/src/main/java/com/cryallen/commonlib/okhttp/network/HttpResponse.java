package com.cryallen.commonlib.okhttp.network;

import com.cryallen.commonlib.okhttp.OkHttpRequest;

import okhttp3.Headers;
import okhttp3.Protocol;

/**
 * 网络响应类
 * @author Allen
 * @DATE 2019-09-19
 */
public final class HttpResponse {
    public final static int IO_EXCEPTION_CODE   = 1000;

    public static HttpResponse error(OkHttpRequest request, int code, String message){
        if (message == null){
            message = "unknown exception.";
        }
        return new HttpResponse(request, new okhttp3.Response.Builder()
                .request(request.raw())
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(message)
                .build());
    }

    public static HttpResponse newResponse(OkHttpRequest request, okhttp3.Response raw){
        return new HttpResponse(request, raw);
    }

    private final OkHttpRequest request;
    private final okhttp3.Response rawResponse;

    private HttpResponse(OkHttpRequest request, okhttp3.Response rawResponse){
        this.request = request;
        this.rawResponse = rawResponse;
    }

    public OkHttpRequest request() {
        return request;
    }

    public okhttp3.Response raw() {
        return rawResponse;
    }

    /** HTTP status code. */
    public int code() {
        return rawResponse.code();
    }

    /** HTTP status message or null if unknown. */
    public String message() {
        return rawResponse.message();
    }

    /** HTTP headers. */
    public Headers headers() {
        return rawResponse.headers();
    }

}
