package org.hf.lang;

import java.util.concurrent.Callable;

public class  SingTask<T> implements Callable<T>{
	
	private T t ;
	
	public SingTask(T t1){
		t = t1;
	}

	public T call() throws Exception {
		int n = 10;
		System.out.println("fun. I will sleep for " + n + " seconds");
		Thread.sleep(n * 1000);
		return t;
	}


}
