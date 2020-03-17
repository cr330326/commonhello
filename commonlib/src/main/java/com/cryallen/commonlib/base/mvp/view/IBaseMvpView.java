package com.cryallen.commonlib.base.mvp.view;


import androidx.annotation.NonNull;

import com.cryallen.commonlib.base.IBaseView;
import com.cryallen.commonlib.base.mvp.presenter.BaseMvpPresenter;

/***
 * fragment base view接口
 Created by chenran on 2018/6/29.
 ***/
public interface IBaseMvpView extends IBaseView {

	/**
	 * 初始化presenter
	 * <p>
	 * 此方法返回的presenter对象不可为空
	 */
	@NonNull
	BaseMvpPresenter initPresenter();

	/**
	 * 显示等待dialog
	 */
	void showWaitDialog();

	/**
	 * 隐藏等待dialog
	 */
	void hideWaitDialog();
}
