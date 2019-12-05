package com.cryallen.commonlib.okhttp.interf;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  IMethod
 */
public interface IMethod {
    String POST = "POST";
    String GET  = "GET";
    String HEAD = "HEAD";
    String DELETE = "DELETE";
    String PUT = "PUT";
    String PATCH = "PATCH";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({IMethod.POST, IMethod.GET, IMethod.HEAD, IMethod.DELETE, IMethod.PUT, IMethod.PATCH})
    @interface MethodType{}
}
