package com.cryallen.commonlib.base.mvvm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.cryallen.commonlib.base.mvvm.IBaseMvvmView;
import com.cryallen.commonlib.base.mvvm.viewmodel.IMvvmBaseViewModel;

/***
 * 基类fragment
 * @author Allen
 * @DATE 2020-03-13
 ***/
public abstract class BaseMvvmCompatFragment<V extends ViewDataBinding, VM extends IMvvmBaseViewModel>
		extends Fragment implements IBaseMvvmView {

	protected V viewDataBinding;

	protected VM viewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initParameters();
	}

	/**
	 * 初始化参数
	 */
	protected void initParameters() { }

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
		return viewDataBinding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = getViewModel();
		if (null != viewModel)
		{
			viewModel.attachUi(this);
		}
		if (getBindingVariable() > 0)
		{
			viewDataBinding.setVariable(getBindingVariable(), viewModel);
			viewDataBinding.executePendingBindings();
		}
	}



	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != viewModel && viewModel.isUiAttach())
		{
			viewModel.detachUi();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (null != viewModel && viewModel.isUiAttach()) {
			viewModel.detachUi();
		}
	}

	/**
	 * 获取参数
	 */
	public abstract int getBindingVariable();

	/**
	 * 获取ViewModel
	 */
	protected abstract VM getViewModel();

	@LayoutRes
	protected abstract int getLayoutId();

	/**
	 * 失败重试,加载事件
	 */
	protected abstract void onRetryBtnClick();
}
