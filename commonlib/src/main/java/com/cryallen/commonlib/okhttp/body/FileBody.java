package com.cryallen.commonlib.okhttp.body;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.cryallen.commonlib.okhttp.utils.MediaTypeUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * FileBody类
 */
public final class FileBody extends RequestBody{
    private final static MediaType CONTENT_TYPE = MediaTypeUtils.OCTET;
    private final MediaType contentType;
    private final File file;

    public FileBody(File file){
        this(file, null);
    }

    public FileBody(File file, String contentType){
        this.file = file;
        this.contentType = TextUtils.isEmpty(contentType) ? CONTENT_TYPE : MediaType.parse(contentType);
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            sink.writeAll(source);
        } finally {
            Util.closeQuietly(source);
        }
    }
}
