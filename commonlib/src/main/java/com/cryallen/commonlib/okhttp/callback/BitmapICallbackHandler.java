package com.cryallen.commonlib.okhttp.callback;

import android.graphics.Bitmap;

import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.abstarct.AbsICallbackHandler;
import com.cryallen.commonlib.okhttp.interf.IAccept;

/**
 * BitmapICallbackHandler
 */
public abstract class BitmapICallbackHandler extends AbsICallbackHandler<Bitmap> {
    @Override
    public void onSuccess(Bitmap bitmap, HttpResponse response) {
    }

    @Override
    public void onFailure(HttpResponse response, Throwable throwable) {

    }

    @Override
    public String accept() {
        return IAccept.ACCEPT_IMAGE;
    }

}
