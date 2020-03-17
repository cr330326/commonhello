package com.cryallen.commonlib.base.mvvm.viewmodel;

import androidx.lifecycle.ViewModel;

import com.cryallen.commonlib.base.mvvm.model.SuperBaseMvvmModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/***
 * 管理 v M
 * @author Allen
 * @DATE 2020-03-13
 ***/
public abstract class MvmBaseViewModel<V, M extends SuperBaseMvvmModel> extends ViewModel
		implements IMvvmBaseViewModel<V> {

	private Reference<V> mUiRef;

	protected M model;

	@Override
	public void attachUi(V view) {
		mUiRef = new WeakReference<>(view);
	}

	@Override
	public V getPageView() {
		if (null == mUiRef) {
			return null;
		}
		if (null != mUiRef.get()) {
			return mUiRef.get();
		}
		return null;
	}

	@Override
	public boolean isUiAttach() {
		return null != mUiRef && null != mUiRef.get();
	}

	@Override
	public void detachUi() {
		if (null != mUiRef) {
			mUiRef.clear();
			mUiRef = null;
		}
		if (null != model) {
			model.cancel();
		}
	}

	protected void loadData(){ }

	protected  abstract void initModel();
}
