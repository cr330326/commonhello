package com.cryallen.commonlib.base.mvp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

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

	/**
	 * 初始化数据
	 * <p>
	 * 子类可以复写此方法初始化子类数据
	 */
	@Override
	protected void initData() {
		super.initData();
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
		startActivity(clz);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz, Intent intent) {
		startActivity(clz,intent);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
		startActivity(clz, bundle);
	}

	@Override
	public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
		startActivityForResult(clz, bundle, requestCode);
	}
}
