package com.cryallen.commonlib.okhttp.interf;

import java.util.concurrent.Executor;
import com.cryallen.commonlib.okhttp.executor.BACKGROUND;
import com.cryallen.commonlib.okhttp.executor.MAIN;
import com.cryallen.commonlib.okhttp.executor.SENDING;

/**
 * ThreadMode
 */
public enum ThreadMode {
    /**
     * ICallback will be called in the same thread, which is sending the request.
     */
    SENDING{
        @Override public Executor executor() {
            return new SENDING();
        }
    },
    /**
     * ICallback will be called in Android's main thread (UI thread).
     */
    MAIN{
        @Override public Executor executor() {
            return new MAIN();
        }
    },

    /**
     * ICallback will be called in a background thread. That is, work on the request thread(okhttp thread).
     */
    BACKGROUND{
        @Override public Executor executor() {
            return new BACKGROUND();
        }
    };

    public abstract Executor executor();
}
