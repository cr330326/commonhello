package com.cryallen.commonlib.threadpool;

/**
 * Created by ranchen on 2018/7/14.
 */
public class PriorityRunnable implements Runnable {

    private final Priority priority;

    public PriorityRunnable(Priority priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        // nothing to do here.
    }

    public Priority getPriority() {
        return priority;
    }

}