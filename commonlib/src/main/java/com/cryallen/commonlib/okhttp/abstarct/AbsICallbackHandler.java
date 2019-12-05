package com.cryallen.commonlib.okhttp.abstarct;


import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cryallen.commonlib.okhttp.OkHttpRequest;
import com.cryallen.commonlib.okhttp.interf.Converter;
import com.cryallen.commonlib.okhttp.interf.IAccept;
import com.cryallen.commonlib.okhttp.interf.ICallback;
import com.cryallen.commonlib.okhttp.interf.ThreadMode;
import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.exception.ParserException;
import com.cryallen.commonlib.okhttp.exception.ResponseFailException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.internal.Util;


/**
 * <p>Description  : AbsResponseHandler.
 *                   ICallback lifecycle as follow:
 *                                              onStart()
 *                         -------------------------------------------------------
 *                              |
 *                              |
 *                         is canceled --- Y --- onCancel()
 *                              |
 *                              N
 *                              |
 *                          onFinish()
 *                              |
 *                              |
 *                        is successful --- N --- onFailure() ------------------
 *                              |                                                 |
 *                              Y                                                 |
 *                              |                                                 |
 *                        backgroundParser() --is download-- onProgress()         |
 *                              |                                     |           |
 *                              |                                     |           |
 *                          onSuccess()                           onSuccess()     |
 *                              |                                     |           |
 *                              |                                     |           |
 *                        ---------------------------------------------------------
 *                                             onFinally()
 *                          </p>
 */
public abstract class AbsICallbackHandler<Parser_Type> implements ICallback, Converter<HttpResponse, Parser_Type> {
    private final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool(Util.threadFactory("OkHttp", false));

    public final static String  DEFAULT_CHARSET     = "UTF-8";

    private OkHttpRequest request;
    private String responseCharset = DEFAULT_CHARSET;
    private boolean isFinished;
    private boolean isCanceled;

    private Executor mExecutor;

    /** Working thread depends on {@link #mExecutor}, default UI. */
    public abstract void onSuccess(Parser_Type data, HttpResponse response);
    /** Working thread depends on {@link #mExecutor}, default UI. */
    public abstract void onFailure(HttpResponse response, Throwable throwable);
    /** Work on the request thread, that is okhttp thread. */
    public Parser_Type backgroundParser(HttpResponse response) throws Exception{
        return null;
    }
    /** Work on the request thread, that is okhttp thread. */
    @Override
    public Parser_Type apply(HttpResponse response) throws Exception {
        return backgroundParser(response);
    }

    /** Working thread depends on {@link #mExecutor}, default UI. */
    public void onStart(){}
    /** Working thread depends on {@link #mExecutor}, default UI. */
    public void onCancel(){}
    /** Working thread depends on {@link #mExecutor}, default UI. */
    public void onProgress(int bytesWritten, int bytesTotal){}
    /** Working thread depends on {@link #mExecutor}, default UI. */
    public void onUploadProgress(int bytesWritten, int bytesTotal){}
    /** Working thread depends on {@link #mExecutor}, default UI. */
    public void onFinish(){}
    /** Working thread depends on {@link #mExecutor}, default UI. */
    public void onFinally(HttpResponse response){}


    @Override
    final public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (call.isCanceled()){
            sendCancelEvent();
            return;
        }
        sendFinishEvent();

        final OkHttpRequest req = request;
        HttpResponse response = HttpResponse.error(req,
                HttpResponse.IO_EXCEPTION_CODE,
                e.getMessage());

        sendFailureEvent(response, e);
        sendFinallyEvent(response);
    }

    @Override
    final public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
        if (call.isCanceled()){
            response.close();
            sendCancelEvent();
            return;
        }
        sendFinishEvent();

        final OkHttpRequest req = request;
        HttpResponse okResponse;
        if (response.isSuccessful()) {
            try {
                okResponse = HttpResponse.newResponse(req, response);
                Parser_Type data = apply(okResponse);
                sendSuccessEvent(data, okResponse);
            } catch (Exception e) {
                sendFailureEvent(okResponse = HttpResponse.newResponse(req, response), new ParserException());
            }
        } else {
            sendFailureEvent(okResponse = HttpResponse.newResponse(req, response), new ResponseFailException());
        }
        // todo response.close()
        sendFinallyEvent(okResponse);
    }

    public AbsICallbackHandler(){}

    @Override
    public void initialize(OkHttpRequest request){
        isFinished = false;
        isCanceled = false;
        this.request = request;
        this.mExecutor = request.callbackExecutor();
        if (this.mExecutor == null){
            this.mExecutor = request.callbackThreadMode().executor();
        }
    }

    public final boolean isFinished(){
        return isFinished;
    }

    /**
     * Sets the charset for the response string. If not set, the default is UTF-8.
     */
    public final void setCharset(@NonNull final String charset) {
        this.responseCharset = charset;
    }

    /**
     * subclass can override this method to change charset.
     */
    public String charset() {
        return TextUtils.isEmpty(responseCharset) ? DEFAULT_CHARSET : responseCharset;
    }

    /**
     * @return request accept
     */
    @Override
    public String accept(){
        return IAccept.EMPTY;
    }

    protected final void print(String message){
        Log.d(AbsICallbackHandler.class.getSimpleName(), message);
    }

    @Nullable
    protected final String byteArrayToString(byte[] bytes){
        try {
            return bytes == null ? null : new String(bytes, charset());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    protected final OkHttpRequest getRequest(){
        return this.request;
    }

    public final void sendUploadProgressEvent(final int bytesWritten, final int bytesTotal) {
        execute(()->{
            try {
                onUploadProgress(bytesWritten, bytesTotal);
            } catch (Throwable t) {
                //Silent
            }
        });
    }

    public final void sendProgressEvent(final int bytesWritten, final int bytesTotal) {
        execute(()->{
            try {
                onProgress(bytesWritten, bytesTotal);
            } catch (Throwable t) {
                //Silent
            }
        });
    }

    public final void sendSuccessEvent(final Parser_Type data, final HttpResponse response) {
        execute(() -> onSuccess(data, response));
    }

    public final void sendFailureEvent(final HttpResponse response, @Nullable final Throwable throwable) {
        execute(() -> onFailure(response, throwable));
    }

    public final void sendStartEvent() {
        if (request.callbackThreadMode() == ThreadMode.BACKGROUND){
            DEFAULT_EXECUTOR_SERVICE.execute(this::onStart);
        } else {
            execute(this::onStart);
        }
    }

    public final void sendFinishEvent() {
        execute(() -> {
            AbsICallbackHandler.this.isFinished = true;
            onFinish();
        });
    }

    public final void sendFinallyEvent(final HttpResponse response) {
        execute(() -> onFinally(response));
    }

    public final synchronized void sendCancelEvent() {
        if (isCanceled){
            return;
        }
        execute(() -> {
            AbsICallbackHandler.this.isCanceled = true;
            onCancel();
        });
    }

    private void execute(Runnable command){
        if (mExecutor == null || threadInterrupted()){
            return;
        }

        mExecutor.execute(command);
    }

    private boolean threadInterrupted(){
        return Thread.currentThread().isInterrupted();
    }
}
