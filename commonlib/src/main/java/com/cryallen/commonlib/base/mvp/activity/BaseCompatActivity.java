/**
 * BaseCompatActivity 2017-02-04
 * Copyright (c) 2017 TYYD Co.Ltd. All right reserved
 */
package com.cryallen.commonlib.base.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
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
		init(savedInstanceState);
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
				AppUtils.hideKeyboard(ev, view, this);//调用方法判断是否需要隐藏键盘
				break;

			default:
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void init(@Nullable Bundle savedInstanceState) {
		setContentView(getLayoutId());
		initData();
		initView(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
	}

	/**
	 * 初始化数据
	 * <p>
	 * 子类可以复写此方法初始化子类数据
	 */
	protected void initData() {
		mContext = AppUtils.getContext();
		mApplication = (GlobalApplication) getApplication();
	}

	/**
	 * 初始化view
	 * <p>
	 * 子类实现 控件绑定、视图初始化等内容
	 *
	 * @param savedInstanceState savedInstanceState
	 */
	protected abstract void initView(Bundle savedInstanceState);

	/**
	 * 获取当前layouty的布局ID,用于设置当前布局
	 * <p>
	 * 交由子类实现
	 *
	 * @return layout Id
	 */
	@LayoutRes
	protected abstract int getLayoutId();

	/**
	 * [页面跳转]
	 *
	 * @param clz 要跳转的Activity
	 */
	public void startActivity(Class<?> clz) {
		startActivity(new Intent(this, clz));
	}

	/**
	 * [页面跳转]
	 *
	 * @param clz    要跳转的Activity
	 * @param intent intent
	 */
	public void startActivity(Class<?> clz, Intent intent) {
		intent.setClass(this, clz);
		startActivity(intent);
	}

	/**
	 * [携带数据的页面跳转]
	 *
	 * @param clz    要跳转的Activity
	 * @param bundle bundel数据
	 */
	public void startActivity(Class<?> clz, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, clz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * [含有Bundle通过Class打开编辑界面]
	 *
	 * @param clz         要跳转的Activity
	 * @param bundle      bundel数据
	 * @param requestCode requestCode
	 */
	public void startActivityForResult(Class<?> clz, Bundle bundle,
	                                   int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, clz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);

	}

	@Override
	public void finish() {
		super.finish();
	}
}