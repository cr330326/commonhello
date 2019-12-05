package com.cryallen.commonlib.base;

import androidx.annotation.NonNull;

import com.cryallen.commonlib.manager.RxManager;

import java.lang.ref.WeakReference;

/***
 Created by chenran on 2018/6/29.
 ***/
public abstract class BasePresenter <M, V> {

	protected M mIModel;
	//解决MVP模式中，Presenter持有IView层容易引起的内存泄漏问题，用弱引用来解决
	protected WeakReference<V> weakRefView;
	protected RxManager mRxManager = new RxManager();

	/**
	 * 返回presenter想持有的Model引用
	 *
	 * @return presenter持有的Model引用
	 */
	protected abstract M getModel();

	/**
	 * 绑定IModel和IView的引用
	 *
	 * @param v view
	 */
	public void attachMV(@NonNull V v) {
		this.mIModel = getModel();
		this.weakRefView = new WeakReference<V>(v);
		this.onStart();
	}

	/**
	 * 解绑IModel和IView
	 */
	public void detachMV() {
		mRxManager.unSubscribe();
		mIModel = null;
		if(isAttach())
		{
			weakRefView.clear();
			weakRefView = null;
		}
	}

	/**
	 * IView和IModel绑定完成立即执行
	 * <p>
	 * 实现类实现绑定完成后的逻辑,例如数据初始化等,界面初始化, 更新等
	 */
	public abstract void onStart();

	public V obtainView(){
		return isAttach() ? weakRefView.get():null;
	}

	public boolean isAttach()
	{
		return weakRefView != null && weakRefView.get() != null;
	}
}
