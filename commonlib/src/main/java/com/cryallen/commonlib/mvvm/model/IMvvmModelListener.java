package com.cryallen.commonlib.mvvm.model;

/***
 * 通用model
 * @author Allen
 * @DATE 2020-03-13
 ***/
public interface IMvvmModelListener<T> extends IBaseMvvmModelListener{
	/**
	 * 数据加载完成
	 *
	 * @param model model
	 * @param data 数据
	 */
	void onLoadFinish(BaseMvvmModel model, T data);

	/**
	 * 数据加载失败
	 *
	 * @param model model
	 * @param prompt 错误
	 */
	void onLoadFail(BaseMvvmModel model, String prompt);
}
