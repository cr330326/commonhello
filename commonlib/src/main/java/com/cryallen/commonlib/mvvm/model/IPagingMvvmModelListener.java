package com.cryallen.commonlib.mvvm.model;

/***
 * 分页通用的Model
 * @author Allen
 * @DATE 2020-03-13
 ***/
public interface IPagingMvvmModelListener<T> extends IBaseMvvmModelListener {
	/**
	 * 数据加载完成
	 *
	 * @param model model
	 * @param data 数据
	 * @param isEmpty 数据是否为空
	 * @param isFirstPage 是否是第一页
	 */
	void onLoadFinish(BaseMvvmPagingModel model, T data, boolean isEmpty, boolean isFirstPage);

	/**
	 * 数据加载失败
	 *
	 * @param model model
	 * @param prompt 错误
	 * @param isFirstPage 是否是第一页
	 */
	void onLoadFail(BaseMvvmPagingModel model, String prompt, boolean isFirstPage);
}

