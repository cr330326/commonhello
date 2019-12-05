package com.cryallen.commonlib.okhttp.callback;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cryallen.commonlib.okhttp.abstarct.AbsICallbackHandler;
import com.cryallen.commonlib.okhttp.network.HttpResponse;
import com.cryallen.commonlib.okhttp.interf.IAccept;
import okhttp3.internal.Util;

/**
 * FileResponseHandler
 */
public class FileICallbackHandler extends AbsICallbackHandler<File> {
    final private File file;
    final private static int BUFFER_SIZE = 4096;

    public FileICallbackHandler(Context context){
        this.file = getTempFile(context);
    }

    public FileICallbackHandler(File file){
        this.file = file;
    }

    protected File getFile(){
        return file;
    }

    @Override
    public void onSuccess(File file, HttpResponse response){

    }

    @Override
    public void onFailure(HttpResponse response, Throwable throwable) {

    }

    @Override public File backgroundParser(HttpResponse response) throws IOException{
        writeFile(response.raw(), file);
        return file;
    }

    @Override public String accept() {
        return IAccept.ACCEPT_FILE;
    }

    private File getTempFile(Context context){
        try {
            return File.createTempFile("temp", "_handled", context.getCacheDir());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * write file , send progress message
     */
    protected void writeFile(okhttp3.Response response, File file) throws IOException {
        if (file == null){
            throw new IllegalArgumentException("File == null");
        }
        InputStream instream = response.body().byteStream();
        long contentLength = response.body().contentLength();
        FileOutputStream buffer = new FileOutputStream(file);
        if (instream != null) {
            try {
                byte[] tmp = new byte[BUFFER_SIZE];
                int l, count = 0;
                while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                    count += l;
                    buffer.write(tmp, 0, l);

                    sendProgressEvent(count, (int) contentLength);
                }
            } finally {
                Util.closeQuietly(instream);
                buffer.flush();
                Util.closeQuietly(buffer);
            }
        }
    }
}
