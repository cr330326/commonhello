package com.cryallen.commonlib.base.mvvm;

import com.cryallen.commonlib.base.IBaseView;

/***
 * 界面UI显示切换
 * @author Allen
 * @DATE 2020-03-13
 ***/
public interface IBaseMvvmView extends IBaseView {
	/**
	 * 显示内容
	 */
	void showContent();

	/**
	 * 显示加载提示
	 */
	void showLoading();

	/**
	 * 显示空页面
	 */
	void showEmpty();

	/**
	 * 刷新失败
	 */
	void showFailure(String message);

	/**
	 * 加载更多失败
	 * */
	void onLoadMoreFailure(String message);

	/**
	 * 没有更多了
	 * */
	void onLoadMoreEmpty();
}
