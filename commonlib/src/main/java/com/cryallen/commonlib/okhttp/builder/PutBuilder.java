package com.cryallen.commonlib.okhttp.builder;

import com.cryallen.commonlib.okhttp.OkHttpManager;
import com.cryallen.commonlib.okhttp.callback.SimpleCallbackHandler;
import com.cryallen.commonlib.okhttp.interf.IResponseHandler;
import com.cryallen.commonlib.utils.LogUtils;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * put builder
 * Created by chenran3 on 2017/10/25.
 */
public class PutBuilder extends OkHttpRequestBuilder<PutBuilder> {

    public PutBuilder(OkHttpManager okHttpManager){
        super(okHttpManager);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if(mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            builder.put(RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), ""));

            Request request = builder.build();

            mOkHttpManager.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new SimpleCallbackHandler(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Put enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }
}