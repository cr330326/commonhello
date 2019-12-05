package com.cryallen.commonlib.okhttp.callback;

import com.cryallen.commonlib.okhttp.abstarct.AbsICallbackHandler;
import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.interf.IAccept;

/**
 * BinaryResponseHandler
 */
public class BinaryICallbackHandler extends AbsICallbackHandler<byte[]> {

    @Override
    public void onSuccess(byte[] data, HttpResponse response) {

    }

    @Override
    public void onFailure(HttpResponse response, Throwable throwable) {

    }

    @Override
    public byte[] backgroundParser(HttpResponse response) throws Exception {
        return response.raw().body().bytes();
    }

    @Override
    public String accept() {
        return IAccept.ACCEPT_DATA;
    }
}
