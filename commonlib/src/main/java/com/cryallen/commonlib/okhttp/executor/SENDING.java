package com.cryallen.commonlib.okhttp.executor;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * SENDING
 */
public final class SENDING implements Executor {
    private final Handler handler;
    public SENDING(){
        if (Looper.myLooper() == null){
            throw new RuntimeException("The Looper of the current thread is null, please call Looper.prepare() on your thread.");
        }
        handler = new Handler(Looper.myLooper());
    }

    @Override
    public void execute(@NonNull Runnable command) {
        handler.post(command);
    }
}
