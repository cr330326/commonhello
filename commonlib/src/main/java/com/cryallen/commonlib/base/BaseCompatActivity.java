/**
 * BaseCompatActivity 2017-02-04
 * Copyright (c) 2017 TYYD Co.Ltd. All right reserved
 */
package com.cryallen.commonlib.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatDelegate;
import com.cryallen.commonlib.global.GlobalApplication;
import com.cryallen.commonlib.manager.AppManager;
import com.cryallen.commonlib.utils.AppUtils;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 与业务无关的Activity基类
 * @author ChenRan
 * @since 2017-02-04
 * @version 1.0.0
 */
public abstract class BaseCompatActivity extends SupportActivity {
	protected GlobalApplication mApplication;
	protected Context mContext;

	static {
		//5.0以下兼容vector
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = AppUtils.getContext();
		mApplication = (GlobalApplication) getApplication();
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		//fragment切换使用默认Vertical动画
		return new DefaultVerticalAnimator();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				View view = getCurrentFocus();
				//调用方法判断是否需要隐藏键盘
				AppUtils.hideKeyboard(ev, view, this);
				break;

			default:
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 获取当前layouty的布局ID,用于设置当前布局
	 * <p>
	 * 交由子类实现
	 *
	 * @return layout Id
	 */
	@LayoutRes
	protected abstract int getLayoutId();

	@Override
	public void finish() {
		super.finish();
	}
}