package com.cryallen.commonlib.mvvm.model;

import java.lang.ref.WeakReference;

/***
 * 通用的基类model
 * @author Allen
 * @DATE 2020-03-13
 ***/
public abstract class BaseMvvmModel<T> extends SuperBaseMvvmModel<T> {

	/**
	 * 网络数据加载成功,通知所有注册者加载数据
	 *
	 * @param data 数据bean
	 */
	public void loadSuccess(T data) {
		synchronized (this){
			mUiHandler.postDelayed(() -> {
				for (WeakReference<IBaseMvvmModelListener> weakListener : mWeakReferenceDeque) {
					if (weakListener.get() instanceof IMvvmModelListener) {
						IMvvmModelListener listenerItem = (IMvvmModelListener)weakListener.get();
						if (null != listenerItem) {
							listenerItem.onLoadFinish(BaseMvvmModel.this, data);
						}
					}
				}
			}, 0);
		}
	}

	/**
	 * 加载数据失败,通知所有注册者
	 */
	protected void loadFail(String prompt){
		synchronized (this) {
			mUiHandler.postDelayed(() -> {
				for (WeakReference<IBaseMvvmModelListener> weakListener : mWeakReferenceDeque) {
					if (weakListener.get() instanceof IMvvmModelListener) {
						IMvvmModelListener listenerItem = (IMvvmModelListener)weakListener.get();
						if (null != listenerItem) {
							listenerItem.onLoadFail(BaseMvvmModel.this, prompt);
						}
					}
				}
			}, 0);
		}
	}

	@Override
	protected void notifyCacheData(T data) {
		loadSuccess(data);
	}
}
