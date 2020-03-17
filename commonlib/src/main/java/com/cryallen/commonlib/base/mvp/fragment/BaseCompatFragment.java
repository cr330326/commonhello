package com.cryallen.commonlib.base.mvp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryallen.commonlib.global.GlobalApplication;
import com.cryallen.commonlib.utils.AppUtils;

import me.yokeyword.fragmentation.SupportFragment;

/***
 Created by chenran on 2018/7/2.
 ***/
public abstract class BaseCompatFragment extends SupportFragment {
	protected Context mContext;
	protected Activity mActivity;
	protected GlobalApplication mApplication;

	@Override
	public void onAttach(Context context) {
		mActivity = (Activity) context;
		mContext = context;
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
			Bundle savedInstanceState) {
		if (getLayoutView() != null) {
			return getLayoutView();
		} else {
			return inflater.inflate(getLayoutId(), container, false);
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getBundle(getArguments());
		initData();
		initUI(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@LayoutRes
	public abstract int getLayoutId();

	public View getLayoutView() {
		return null;
	}

	/**
	 * 得到Activity传进来的值
	 */
	public void getBundle(Bundle bundle) {
	}

	/**
	 * 初始化UI
	 */
	public abstract void initUI(View view, @Nullable Bundle savedInstanceState);

	/**
	 * 在监听器之前把数据准备好
	 */
	public void initData() {
		mContext = AppUtils.getContext();
		mApplication = (GlobalApplication) mActivity.getApplication();
	}

	/**
	 * 处理回退事件
	 *
	 * @return true 事件已消费
	 * <p>
	 * false 事件向上传递
	 */
	@Override
	public boolean onBackPressedSupport() {
		if (getFragmentManager().getBackStackEntryCount() > 1) {
			//如果当前存在fragment>1，当前fragment出栈
			pop();
		} else {
			//已经退栈到root fragment，交由Activity处理回退事件
			return false;
		}
		return true;
	}
}
