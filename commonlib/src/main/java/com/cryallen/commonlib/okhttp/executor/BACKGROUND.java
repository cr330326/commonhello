package com.cryallen.commonlib.okhttp.executor;


import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * BACKGROUND
 */
public final class BACKGROUND implements Executor {
    @Override
    public void execute(@NonNull Runnable command) {
        command.run();
    }
}
