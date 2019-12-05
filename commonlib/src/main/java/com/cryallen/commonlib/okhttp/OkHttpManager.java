package com.cryallen.commonlib.okhttp;

import com.cryallen.commonlib.okhttp.builder.DeleteBuilder;
import com.cryallen.commonlib.okhttp.builder.DownloadBuilder;
import com.cryallen.commonlib.okhttp.builder.GetBuilder;
import com.cryallen.commonlib.okhttp.builder.PatchBuilder;
import com.cryallen.commonlib.okhttp.builder.PostBuilder;
import com.cryallen.commonlib.okhttp.builder.PutBuilder;
import com.cryallen.commonlib.okhttp.builder.UploadBuilder;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * 封装好的网络请求管理类
 * Created by chenran3 on 2017/10/25.
 */
public class OkHttpManager {
	private OkHttpClient mOkHttpClient;
	private volatile static OkHttpManager instance;

	public OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}

	/**
	 * construct
	 */
	public OkHttpManager() {
		this(null);
	}

	/**
	 * construct
	 * @param okHttpClient custom okhttpclient
	 */
	public OkHttpManager(OkHttpClient okHttpClient) {
		if(mOkHttpClient == null) {
			synchronized (OkHttpManager.class) {
				if (mOkHttpClient == null) {
					if (okHttpClient == null) {
						mOkHttpClient = new OkHttpClient();
					} else {
						mOkHttpClient = okHttpClient;
					}
				}
			}
		}
	}

	/**
	 * 获取单例句柄
	 * @return
	 */
	public static void init(OkHttpClient okHttpClient) {
		if(null == okHttpClient){
			instance = new OkHttpManager(null);
		} else {
			instance = new OkHttpManager(okHttpClient);
		}
	}

	/**
	 * 获取单例句柄
	 * @return
	 */
	public static OkHttpManager getInstance() {
		return instance;
	}

	public GetBuilder get() {
		return new GetBuilder(this);
	}

	public PostBuilder post() {
		return new PostBuilder(this);
	}

	public PutBuilder put(){
		return new PutBuilder(this);
	}

	public PatchBuilder patch(){
		return new PatchBuilder(this);
	}

	public DeleteBuilder delete(){
		return new DeleteBuilder(this);
	}

	public UploadBuilder upload() {
		return new UploadBuilder(this);
	}

	public DownloadBuilder download() {
		return new DownloadBuilder(this);
	}

	/**
	 * do cacel by tag
	 * @param tag tag
	 */
	public void cancel(Object tag) {
		Dispatcher dispatcher = mOkHttpClient.dispatcher();
		for (Call call : dispatcher.queuedCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
		for (Call call : dispatcher.runningCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
	}
}
