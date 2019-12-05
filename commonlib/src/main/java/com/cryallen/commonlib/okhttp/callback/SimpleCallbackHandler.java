package com.cryallen.commonlib.okhttp.callback;

import com.cryallen.commonlib.okhttp.interf.IResponseHandler;
import com.cryallen.commonlib.utils.AppUtils;
import com.cryallen.commonlib.utils.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *  SimpleCallbackHandler
 */
public class SimpleCallbackHandler implements Callback {
    private IResponseHandler mResponseHandler;
    public SimpleCallbackHandler(IResponseHandler responseHandler) {
        mResponseHandler = responseHandler;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        LogUtils.e("onFailure", e);
        AppUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailure(0, e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) {
        if(response.isSuccessful()) {
            mResponseHandler.onSuccess(response);
        } else {
            LogUtils.e("onResponse fail status=" + response.code());
            AppUtils.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(response.code(), "fail status=" + response.code());
                }
            });
        }
    }
}
