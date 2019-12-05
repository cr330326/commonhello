package com.cryallen.commonlib.okhttp.callback;

import com.cryallen.commonlib.okhttp.abstarct.AbsICallbackHandler;
import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.interf.IAccept;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSONResponseHandler
 */
public class JsonICallbackHandler extends AbsICallbackHandler<JsonICallbackHandler.JSON> {

    @Override
    public void onSuccess(JSON data, HttpResponse response) {

    }

    @Override
    public void onFailure(HttpResponse response, Throwable throwable) {

    }

    @Override
    public JSON backgroundParser(HttpResponse response) throws Exception {
        final byte[] body = response.raw().body().bytes();
        final String bodyString = byteArrayToString(body);
        JSON json = new JSON();

        if (bodyString != null){
            try {
                json.jsonObject = new JSONObject(bodyString);
            } catch (JSONException e) {
                json.jsonArray = new JSONArray(bodyString);
            }
        }
        return json;
    }

    @Override
    public String accept() {
        return IAccept.ACCEPT_JSON;
    }

    public static class JSON {
        public JSONObject jsonObject;
        public JSONArray jsonArray;
    }
}
