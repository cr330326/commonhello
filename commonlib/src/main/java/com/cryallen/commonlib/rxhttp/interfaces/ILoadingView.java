package com.cryallen.commonlib.rxhttp.interfaces;

/**
 * 接口化处理loadingView，突破之前只能用dialog的局限
 * @author Allen
 * @DATE 2019-09-20
 */
public interface ILoadingView {
    /**
     * 显示loadingView
     */
    void showLoadingView();

    /**
     * 隐藏loadingView
     */
    void hideLoadingView();
}
