package com.cryallen.commonlib.okhttp.response;

import java.io.File;

/**
 * 下载回调
 * Created by chenran3 on 2017/10/25.
 */
public abstract class DownloadResponseHandler {

    public void onStart(long totalBytes) { }

    public void onCancel() { }

    public abstract void onFinish(File downloadFile);
    public abstract void onProgress(long currentBytes, long totalBytes);
    public abstract void onFailure(String error_msg);
}