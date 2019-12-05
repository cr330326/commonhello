package com.cryallen.commonlib.permission;

import androidx.annotation.Nullable;

import java.util.List;

/***
 * 权限申请回调接口
 * @author Allen
 * @DATE 2019-10-30
 ***/
public interface IPermissionCallBack {

    /**
     * 在请求曾经被拒绝过一次且没被标记「不再显示」的权限之前会调用此方法
     * 在申请权限前会先回调这个方法给你一个解释的机会
     * 在onResult之前
     * @param rationalList 需要解释的权限
     * @return 是否阻塞接下来的请求操作。
     */
    boolean onRational(@Nullable List<String> rationalList);

	/**
	 * 返回所有结果的列表list,包括通过的,允许提醒,拒绝的的三个内容,各个list有可能为空
	 * list中的元素为PermissionInfo,提供getName()[例如:android.permission.CAMERA]和getShortName()[例如:CAMERA]方法
	 * 在进行申请方法调用后,此方法一定会被调用返回此次请求后的权限申请的情况
	 *
	 * @param acceptList   成功的
	 * @param rationalList 拒绝了但仍可以解释在次申请的
	 * @param deniedList   完全拒绝了不会弹窗的
	 */
	void onResult(List<String> acceptList, List<String> rationalList, List<String> deniedList);
}
