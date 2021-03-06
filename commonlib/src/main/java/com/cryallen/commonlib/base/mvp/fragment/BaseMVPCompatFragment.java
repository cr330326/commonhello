package com.cryallen.commonlib.base.mvp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cryallen.commonlib.base.BaseCompatFragment;
import com.cryallen.commonlib.base.mvp.activity.BaseMVPCompatActivity;
import com.cryallen.commonlib.base.mvp.presenter.BaseMvpPresenter;
import com.orhanobut.logger.Logger;

import me.yokeyword.fragmentation.SupportFragment;

/***
 *  Mvp Fragment基类，实现IBaseView方法、绑定butterknife
 Created by chenran on 2018/7/2.
 ***/
public abstract class BaseMVPCompatFragment <P extends BaseMvpPresenter> extends BaseCompatFragment implements IBaseMvpFragment {

	public P mPresenter;

	/**
	 * 在监听器之前把数据准备好
	 */
	protected void initData() {
		mPresenter = (P) initPresenter();
		if (mPresenter != null) {
			mPresenter.attachMV(this);
			Logger.d("Fragment attach M V success.");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPresenter != null) {
			mPresenter.detachMV();
			Logger.d("Fragment detach M V success.");
		}
	}

	/**
	 * 初始化UI
	 */
	public abstract void initUI(View view, @Nullable Bundle savedInstanceState);

	@Override
	public void startNewFragment(@NonNull SupportFragment supportFragment) {
		start(supportFragment);
	}

	@Override
	public void startNewFragmentWithPop(@NonNull SupportFragment supportFragment) {
		startWithPop(supportFragment);
	}

	@Override
	public void startNewFragmentForResult(@NonNull SupportFragment supportFragment, int
			requestCode) {
		startForResult(supportFragment, requestCode);
	}

	@Override
	public void popToFragment(Class<?> targetFragmentClass, boolean includeTargetFragment) {
		popTo(targetFragmentClass, includeTargetFragment);
	}

	@Override
	public void setOnFragmentResult(int ResultCode, Bundle data) {
		setFragmentResult(ResultCode, data);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz) {
		((BaseMVPCompatActivity) mActivity).startNewActivity(clz);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
		((BaseMVPCompatActivity) mActivity).startNewActivity(clz, bundle);
	}

	@Override
	public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
		((BaseMVPCompatActivity) mActivity).startNewActivityForResult(clz, bundle, requestCode);
	}

	@Override
	public boolean isVisiable() {
		return isSupportVisible();
	}

	@Override
	public Activity getBindActivity() {
		return mActivity;
	}
}
