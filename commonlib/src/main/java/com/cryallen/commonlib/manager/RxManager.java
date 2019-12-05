package com.cryallen.commonlib.manager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/***
 * 用于管理Rxjava 注册订阅和取消订阅
 Created by chenran on 2018/6/29.
 ***/
public class RxManager {
	private CompositeDisposable mCompositeDisposable = new CompositeDisposable();// 管理订阅者者

	public void register(Disposable d) {
		mCompositeDisposable.add(d);
	}

	public void unSubscribe() {
		mCompositeDisposable.dispose();// 取消订阅
	}
}
