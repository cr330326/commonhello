package com.cryallen.commonlib.okhttp.callback;


import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.exception.ParserException;

/**
 * GSONResponseHandler
 */
public abstract class GsonICallbackHandler<T> extends JsonICallbackHandler {

    @Override
    final public void onSuccess(JSON data, HttpResponse response) {
        if (data.jsonArray != null) {
            onSuccess(parser(data.jsonArray.toString()));
        }
        else if (data.jsonObject != null) {
            onSuccess(parser(data.jsonObject.toString()));
        }
        else {
            onFailure(response, new ParserException());
        }
    }

    @Override
    public void onFailure(HttpResponse response, Throwable throwable) {

    }

    /** parser Json to T */
    protected abstract T parser(String jsonString);

    public void onSuccess(T t){

    }

}
