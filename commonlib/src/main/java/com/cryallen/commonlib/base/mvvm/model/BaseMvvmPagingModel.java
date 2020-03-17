package com.cryallen.commonlib.base.mvvm.model;

import java.lang.ref.WeakReference;

/***
 * 适用于需要加载分页的model
 * @author Allen
 * @DATE 2020-03-13
 ***/
public abstract class BaseMvvmPagingModel<T> extends SuperBaseMvvmModel<T> {

	/**
	 * 是否还有下一页
	 */
	protected boolean hasNextPage = false;

	/**
	 * 下一页的url
	 */
	protected String nextPageUrl = "";

	/**
	 * 上拉刷新 true or 下拉加载 false
	 */
	protected boolean isRefresh = true;

	/**
	 * 数据加载成功
	 *
	 * @param data 数据
	 * @param isEmpty 数据是否为空
	 * @param isFirstPage 当前是否是第一页
	 */
	public void loadSuccess(T data, boolean isEmpty,boolean isFirstPage) {
		synchronized (this) {
			mUiHandler.postDelayed(() -> {
				for (WeakReference<IBaseMvvmModelListener> weakListener : mWeakReferenceDeque) {
					if (weakListener.get() instanceof IPagingMvvmModelListener) {
						IPagingMvvmModelListener listenerItem = (IPagingMvvmModelListener)weakListener.get();
						if (null != listenerItem) {
							listenerItem.onLoadFinish(
									BaseMvvmPagingModel.this,
									data,
									isEmpty,isFirstPage);
						}
					}
				}
			}, 0);
		}
	}

	/**
	 * @param prompt msg
	 * @param isFirstPage 是否是第一页
	 * */
	public void loadFail(String prompt,boolean isFirstPage) {
		synchronized (this) {
			mUiHandler.postDelayed(() -> {
				for (WeakReference<IBaseMvvmModelListener> weakListener : mWeakReferenceDeque) {
					if (weakListener.get() instanceof IPagingMvvmModelListener) {
						IPagingMvvmModelListener listenerItem = (IPagingMvvmModelListener)weakListener.get();
						if (null != listenerItem) {
							listenerItem.onLoadFail(
									BaseMvvmPagingModel.this,
									prompt,
									isRefresh);
						}
					}
				}
			}, 0);
		}
	}

	@Override
	protected void notifyCacheData(T data)
	{
		loadSuccess(data, false,true);
	}
}
