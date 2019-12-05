package com.cryallen.commonlib.okhttp.abstarct;

/***
 * 抽象的单例类
 * @author Allen
 * @DATE 2019-09-20
 ***/
public abstract class AbsSingleton<T> {
	private T instance;
	protected abstract T create();

	public T get(){
		if (instance == null){
			synchronized (this){
				if (instance == null){
					instance = create();
				}
			}
		}

		return instance;
	}
}
