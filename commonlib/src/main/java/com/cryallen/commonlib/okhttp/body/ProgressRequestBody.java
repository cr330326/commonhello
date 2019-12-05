package com.cryallen.commonlib.okhttp.body;

import androidx.annotation.NonNull;

import com.cryallen.commonlib.okhttp.abstarct.AbsICallbackHandler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * ProgressRequestBody类
 */
public class ProgressRequestBody extends RequestBody {

    private final RequestBody mRequestBody;
    private final AbsICallbackHandler mCallback;
    private BufferedSink mBufferedSink;

    public ProgressRequestBody(RequestBody requestBody, AbsICallbackHandler callback){
        this.mRequestBody = requestBody;
        this.mCallback = callback;
    }

    @Override public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override public void writeTo(@NonNull BufferedSink sink) throws IOException {
        if (mCallback == null){
            mRequestBody.writeTo(sink);
            return;
        }
        if (mBufferedSink == null){
            mBufferedSink = Okio.buffer(forward(sink));
        }

        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink forward(Sink sink){
        return new ForwardingSink(sink) {
            private long bytesWritten = 0L;
            private long contentLength = 0L;

            @Override public void write(@NonNull Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;

                mCallback.sendUploadProgressEvent((int) bytesWritten, (int) contentLength);
            }
        };
    }
}
