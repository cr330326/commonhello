package com.cryallen.commonlib.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import com.cryallen.commonlib.base.BasePresenter;
import com.cryallen.commonlib.base.IBaseFragment;
import com.cryallen.commonlib.base.activity.BaseCompatActivity;
import com.cryallen.commonlib.utils.ToastUtils;

import me.yokeyword.fragmentation.SupportFragment;

/***
 *  Mvp Fragment基类，实现IBaseView方法、绑定butterknife
 Created by chenran on 2018/7/2.
 ***/
public abstract class BaseMVPCompatFragment <P extends BasePresenter> extends
		BaseCompatFragment implements IBaseFragment {

	public P mPresenter;

	/**
	 * 在监听器之前把数据准备好
	 */
	@Override
	public void initData() {
		super.initData();

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

	@Override
	public void showWaitDialog(String msg) {
		showProgressDialog(msg);
	}

	@Override
	public void hideWaitDialog() {
		hideProgressDialog();
	}

	@Override
	public void showToast(String msg) {
		ToastUtils.showToast(mContext, msg, Toast.LENGTH_SHORT);
	}

	@Override
	public void back() {
		this.onBackPressedSupport();
	}

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
	public void hideKeybord() {
		hideSoftInput();
	}

	@Override
	public void setOnFragmentResult(int ResultCode, Bundle data) {
		setFragmentResult(ResultCode, data);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz) {
		((BaseCompatActivity) mActivity).startActivity(clz);
	}

	@Override
	public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
		((BaseCompatActivity) mActivity).startActivity(clz, bundle);
	}

	@Override
	public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
		((BaseCompatActivity) mActivity).startActivityForResult(clz, bundle, requestCode);
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
