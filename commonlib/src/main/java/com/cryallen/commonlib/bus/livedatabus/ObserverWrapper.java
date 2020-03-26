package com.cryallen.commonlib.bus.livedatabus;

import androidx.lifecycle.Observer;

/***
 * 应用模块: liveData，类描述: Observer 包装类
 *
 * @author Allen
 * @DATE 2020-03-13
 ***/
public class ObserverWrapper <T> implements Observer<T> {
	private Observer<T> observer;

	public ObserverWrapper(Observer<T> observer) {
		this.observer = observer;
	}

	@Override
	public void onChanged(T t) {
		if (observer != null){
			if (isCallOnObserve()){
				return;
			}
			observer.onChanged(t);
		}
	}

	private boolean isCallOnObserve() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace != null && stackTrace.length > 0) {
			for (StackTraceElement element : stackTrace) {
				if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) && "observeForever".equals(element.getMethodName())) {
					return true;
				}
			}
		}
		return false;
	}
}
