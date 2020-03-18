package com.cryallen.commonlib.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 *
 * Created by chenran3 on 2017/10/24.
 */

public class GlobalApplication extends Application {
	protected static Context context;
	protected static Handler handler;
	protected static int mainThreadId;
	private static GlobalApplication mApp;

	public static synchronized GlobalApplication getInstance() {
		return mApp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();
	}

	/**
	 * 获取上下文对象
	 *
	 * @return context
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * 获取全局handler
	 *
	 * @return 全局handler
	 */
	public static Handler getHandler() {
		return handler;
	}

	/**
	 * 获取主线程id
	 *
	 * @return 主线程id
	 */
	public static int getMainThreadId() {
		return mainThreadId;
	}


}
