package com.cryallen.commonlib.base.mvp;


import androidx.annotation.NonNull;

import com.cryallen.commonlib.base.IBaseView;

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
}
