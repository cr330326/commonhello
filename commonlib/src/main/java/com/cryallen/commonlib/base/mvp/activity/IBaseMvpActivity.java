package com.cryallen.commonlib.base.mvp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.cryallen.commonlib.base.mvp.view.IBaseMvpView;

/***
 * @author chenran
 * @date 2018/6/30
 BaseActivity接口
 ***/
public interface IBaseMvpActivity extends IBaseMvpView {
	/**
	 * 跳往新的Activity
	 *
	 * @param clz 要跳往的Activity
	 */
	void startNewActivity(@NonNull Class<?> clz);

	/**
	 * 跳往新的Activity
	 *
	 * @param clz 要跳往的Activity
	 * @param intent 携带的Intent数据
	 */
	void startNewActivity(@NonNull Class<?> clz, Intent intent);

	/**
	 * 跳往新的Activity
	 *
	 * @param clz    要跳往的Activity
	 * @param bundle 携带的bundle数据
	 */
	void startNewActivity(@NonNull Class<?> clz, Bundle bundle);

	/**
	 * 跳往新的Activity
	 * @param clz 要跳转的Activity
	 * @param bundle bundel数据
	 * @param requestCode requestCode
	 */
	void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode);
}
