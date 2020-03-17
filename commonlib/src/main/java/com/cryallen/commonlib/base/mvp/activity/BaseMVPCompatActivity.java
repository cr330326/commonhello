package com.cryallen.commonlib.base.mvp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cryallen.commonlib.base.BaseCompatActivity;
import com.cryallen.commonlib.base.mvp.presenter.BaseMvpPresenter;
import com.cryallen.commonlib.utils.LogUtils;

/***
 *  Mvp Activity基类
 Created by chenran on 2018/6/30.
 ***/
public abstract class BaseMVPCompatActivity <P extends BaseMvpPresenter> extends BaseCompatActivity implements IBaseMvpActivity {

	/**
	 * presenter 具体的presenter由子类确定
	 */
	protected P mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(savedInstanceState);
	}

	private void init(@Nullable Bundle savedInstanceState) {
		setContentView(getLayoutId());
		initData();
		initView(savedInstanceState);
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
	 * 初始化数据
	 * <p>
	 * 子类可以复写此方法初始化子类数据
	 */
	protected void initData() {
		mPresenter = (P) initPresenter();
		if (mPresenter != null) {
			mPresenter.attachMV(this);
			LogUtils.d("Activity attach M V success.");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPresenter != null) {
			mPresenter.detachMV();
			LogUtils.d("Activity detach M V success.");
		}
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz) {
		startActivity(new Intent(this, clz));
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz, Intent intent) {
		intent.setClass(this, clz);
		startActivity(intent);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, clz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	@Override
	public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, clz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}
}
