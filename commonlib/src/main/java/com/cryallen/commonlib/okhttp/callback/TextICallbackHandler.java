package com.cryallen.commonlib.okhttp.callback;

import com.cryallen.commonlib.okhttp.interf.IAccept;
import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.abstarct.AbsICallbackHandler;

/**
 * TextICallbackHandler
 */
public class TextICallbackHandler extends AbsICallbackHandler<String> {
    @Override
    public void onSuccess(String data, HttpResponse response) {
    }

    @Override
    public void onFailure(HttpResponse response, Throwable throwable) {
    }

    @Override
    public String backgroundParser(HttpResponse response) throws Exception {
        return byteArrayToString(response.raw().body().bytes());
    }

    @Override public String accept() {
        return IAccept.ACCEPT_TEXT;
    }
}
