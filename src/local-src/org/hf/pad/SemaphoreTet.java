package org.hf.pad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTet {
	
	private Semaphore semaphore = new Semaphore(5);
	
	Runnable run1 = new Runnable() {
		public void run() {
	
		int i = 0; 
		while (++i < 9) {
			try {
				System.out.println("get one " + semaphore.availablePermits());
				semaphore.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	};
	
	Runnable run2 = new Runnable() {
		public void run() {
			int i = 0;
			while (++i < 10) {
				System.out.println("release one " + semaphore.availablePermits());
				semaphore.release();
			}
		}
	};

	public static void main(String[] args) throws InterruptedException {
		SemaphoreTet test = new SemaphoreTet();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(test.run1);
		Thread.sleep(5000);
		executor.execute(test.run2);
		executor.shutdown();
		
	}

}
