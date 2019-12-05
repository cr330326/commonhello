package com.cryallen.commonlib.okhttp.interf;

import okhttp3.Response;

/**
 * 响应接口
 */
public interface IResponseHandler {
    void onSuccess(Response response);

    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
