package com.cryallen.commonlib.okhttp.interf;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description  : IAccept
 */
public interface IAccept {
    String EMPTY = "";
    String ACCEPT_JSON = "application/json;charset=utf-8";
    String ACCEPT_TEXT = "text/html;charset=utf-8";
    String ACCEPT_DATA = "application/octet-stream";
    String ACCEPT_IMAGE = "image/png,image/jpeg,image/*";
    String ACCEPT_FILE = "application/octet-stream";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            IAccept.EMPTY,
            IAccept.ACCEPT_JSON,
            IAccept.ACCEPT_TEXT,
            IAccept.ACCEPT_DATA,
            IAccept.ACCEPT_IMAGE,
            IAccept.ACCEPT_FILE
    })
    public @interface $Accept {
    }
}
