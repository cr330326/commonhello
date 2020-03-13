package com.cryallen.commonlib.mvvm.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.cryallen.commonlib.base.BaseCompatActivity;
import com.cryallen.commonlib.mvvm.viewmodel.IMvvmBaseViewModel;

/***
 * MVVM模式下的activity抽象基类
 * @author Allen
 * @DATE 2020-03-13
 ***/
public abstract class BaseMvvmCompatActivity <V extends ViewDataBinding, VM extends IMvvmBaseViewModel>
		extends BaseCompatActivity implements IBaseMvvmView {

	protected VM viewModel;

	protected V viewDataBinding;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initViewModel();
		performDataBinding();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != viewModel && viewModel.isUiAttach())
		{
			viewModel.detachUi();
		}
	}

	private void performDataBinding()
	{
		viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
		this.viewModel = viewModel == null ? getViewModel() : viewModel;
		if (getBindingVariable() > 0)
		{
			viewDataBinding.setVariable(getBindingVariable(), viewModel);
		}
		viewDataBinding.executePendingBindings();
	}

	private void initViewModel()
	{
		viewModel = getViewModel();
		if (null != viewModel)
		{
			viewModel.attachUi(this);
		}
	}

	/**
	 * 获取viewModel
	 */
	protected abstract VM getViewModel();

	/**
	 * 获取参数Variable
	 */
	protected abstract int getBindingVariable();

	/**
	 * 失败重试,重新加载事件
	 */
	protected abstract void onRetryBtnClick();
}
