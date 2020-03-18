package com.cryallen.commonlib.base.mvvm.view;

/***
 * 界面UI显示切换
 * @author Allen
 * @DATE 2020-03-13
 ***/
public interface IBasePagingMvvmView extends IBaseMvvmView {

	/**
	 * 加载更多失败
	 * */
	void onLoadMoreFailure(String message);

	/**
	 * 没有更多了
	 * */
	void onLoadMoreEmpty();
}
