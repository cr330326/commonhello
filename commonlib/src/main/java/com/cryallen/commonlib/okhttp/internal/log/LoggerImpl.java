package com.cryallen.commonlib.okhttp.internal.log;

import com.cryallen.commonlib.okhttp.abstarct.AbsSingleton;
import com.cryallen.commonlib.okhttp.internal.interceptor.HttpLoggingInterceptor;
import com.cryallen.commonlib.utils.LoggerUtils;

/**
 * LoggerImpl
 */
public class LoggerImpl implements HttpLoggingInterceptor.Logger{
    private HttpLoggingInterceptor.Level mLevel;

    public static AbsSingleton<LoggerImpl> instance = new AbsSingleton<LoggerImpl>() {
        @Override protected LoggerImpl create() {
            return new LoggerImpl();
        }
    };

    public void setLevel(HttpLoggingInterceptor.Level level){
        mLevel = level;
    }

    @Override public void log(String message) {
        if (mLevel == HttpLoggingInterceptor.Level.NONE){
            return;
        }
        LoggerUtils.d(message);
    }
}
