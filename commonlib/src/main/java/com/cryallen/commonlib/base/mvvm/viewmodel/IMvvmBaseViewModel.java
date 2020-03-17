package com.cryallen.commonlib.base.mvvm.viewmodel;

/***
 * 应用模块: viewModel，类描述: 定义viewModel与 V 的关联
 * @author Allen
 * @DATE 2020-03-13
 ***/
public interface IMvvmBaseViewModel<V> {

	/**
	 * 关联View
	 * */
	void attachUi(V view);

	/**
	 * 获取View
	 * */
	V getPageView();

	/**
	 * 是否已经关联View
	 * */
	boolean isUiAttach();

	/**
	 * 解除关联
	 * */
	void detachUi();
}
