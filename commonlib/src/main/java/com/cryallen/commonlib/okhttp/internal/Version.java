package com.cryallen.commonlib.okhttp.internal;

import com.cryallen.commonlib.BuildConfig;

/**
 * Version
 */
public class Version {
    private Version(){
        throw new AssertionError();
    }

    public static String userAgent() {
        return moduleName().concat(BuildConfig.VERSION_NAME);
    }

    public static String moduleName() {
        return "OkHttpRequest";
    }
}
