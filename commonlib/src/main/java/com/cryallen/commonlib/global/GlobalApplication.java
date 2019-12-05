package com.cryallen.commonlib.global;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.cryallen.commonlib.utils.LogUtils;
import com.cryallen.commonlib.utils.LoggerUtils;


/**
 *
 * Created by chenran3 on 2017/10/24.
 */

public class GlobalApplication extends Application {
	private static final String LOG_TAG = "android_lib_logger";

	public static int SCREEN_WIDTH = -1;
	public static int SCREEN_HEIGHT = -1;
	public static float DIMEN_RATE = -1.0F;
	public static int DIMEN_DPI = -1;

	public static int COORDINATE_MAX_X = -1;
	public static int COORDINATE_MAX_Y = -1;

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
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();

		//默认打开日志开关
		LogUtils.setDebuggable(true);
		LogUtils.setTagName(LOG_TAG);
		LoggerUtils.init(true);

		initScreenSize();
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

	/**
	 * 初始化获取当前设备屏幕信息
	 */
	private void initScreenSize() {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		Display display = windowManager.getDefaultDisplay();
		display.getMetrics(dm);
		Point point = new Point();
		display.getSize(point);

		COORDINATE_MAX_X = point.x;
		COORDINATE_MAX_Y = point.y;

		DIMEN_RATE = dm.density;
		DIMEN_DPI = dm.densityDpi;
		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
		String screenInfo = "DIMEN_RATE:" + DIMEN_RATE + ",DIMEN_DPI:" + DIMEN_DPI
				+ ",SCREEN_WIDTH:" + SCREEN_WIDTH + ",SCREEN_HEIGHT:" + SCREEN_HEIGHT
				+ ",COORDINATE_MAX_X:" + COORDINATE_MAX_X + ",COORDINATE_MAX_Y:" + COORDINATE_MAX_Y;
		LogUtils.d(LOG_TAG,screenInfo);
	}
}
