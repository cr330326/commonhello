package com.cryallen.commonlib.base;


import androidx.annotation.NonNull;

/***
 * fragment base view接口
 Created by chenran on 2018/6/29.
 ***/
public interface IBaseView {
	/**
	 * 初始化presenter
	 * <p>
	 * 此方法返回的presenter对象不可为空
	 */
	@NonNull
	BasePresenter initPresenter();

	/**
	 * 显示toast消息
	 *
	 * @param msg 要显示的toast消息字符串
	 */
	void showToast(String msg);

	/**
	 * 显示等待dialog
	 *
	 * @param waitMsg 等待消息字符串
	 */
	void showWaitDialog(String waitMsg);

	/**
	 * 隐藏等待dialog
	 */
	void hideWaitDialog();

	/**
	 * 隐藏键盘
	 */
	void hideKeybord();

	/**
	 * 回退
	 */
	void back();
}
