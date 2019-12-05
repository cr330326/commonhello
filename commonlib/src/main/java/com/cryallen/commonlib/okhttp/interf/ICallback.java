package com.cryallen.commonlib.okhttp.interf;

import com.cryallen.commonlib.okhttp.OkHttpRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Description  : ICallback
 */
public interface ICallback extends okhttp3.Callback {

    ICallback EMPTY = new ICallback() {
        @Override
        public void initialize(OkHttpRequest request) { }

        @Override
        public String accept() {
            return IAccept.EMPTY;
        }

        @Override
        public void onFailure(Call call, IOException e) {}

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            response.close();
        }
    };

    /**
     * Initialize the callback.
     */
    void initialize(OkHttpRequest request);

    /**
     * Request accept.
     */
    String accept();

}
