package com.cryallen.commonlib.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.cryallen.commonlib.R;
import com.cryallen.commonlib.base.BasePresenter;


/***
 * 带RecycleView加载状态view的fragment，主要用于显示加载中、空界面、加载失败等状态界面显示
 Created by chenran on 2018/7/2.
 ***/
public abstract class BaseRecycleFragment <P extends BasePresenter> extends
		BaseMVPCompatFragment<P> {

	/**
	 * 网络异常View
	 */
	protected View errorView;
	/**
	 * loadingView
	 */
	protected View loadingView;
	/**
	 * 没有内容view
	 */
	protected View emptyView;

	@Override
	public void onLazyInitView(@Nullable Bundle savedInstanceState) {
		super.onLazyInitView(savedInstanceState);
		showLoading();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
			Bundle savedInstanceState) {
		errorView = inflater.inflate(R.layout.view_network_error, container, false);
		loadingView = inflater.inflate(R.layout.view_loading, container, false);
		emptyView = inflater.inflate(R.layout.view_empty, container, false);
		errorView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showLoading();
				onErrorViewClick(v);
			}
		});
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 网络异常view被点击时触发，由子类实现
	 *
	 * @param view view
	 */
	protected abstract void onErrorViewClick(View view);

	/**
	 * 显示加载中view，由子类实现
	 */
	protected abstract void showLoading();
}
