package org.hf.pad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadAdd implements Runnable{
	
	int counter = 0;
	Counter c  ;
	
	public MultithreadAdd(Counter c) {
		this.c = c;
	}

	public void run() {
		c.increase();
	}


	public static void main(String[] args) {
		int loop = 10000;
		
		ExecutorService exec = Executors.newCachedThreadPool();
		Counter c = new Counter();
		
		
		long start = System.nanoTime();
		MultithreadAdd command = new MultithreadAdd(c);
		for (int i = 0; i < loop; ++i) {
//			(new Thread(command)).start();
			exec.execute(command);
		}
		System.out.println(c.getCounter() + " : " + (System.nanoTime() - start) );
		
//		// The normal class run faster
//		Counter c2 = new Counter();
//		long start2 = System.nanoTime();
//		MultithreadAdd multithreadAdd = new MultithreadAdd(c2);
//		for (int i = 0; i < loop; ++i) {
//			multithreadAdd.run();
//		}
//		System.out.println(c2.getCounter() + " : " + (System.nanoTime() - start2) );
		
		exec.shutdown();
	}
	
 

}
