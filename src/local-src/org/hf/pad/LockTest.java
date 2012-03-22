package org.hf.pad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
  
	private Lock lock = new ReentrantLock();
  
	Runnable run1 = new Runnable() {
		public void run() {
	
		int i = 0; 
		while (++i < 9) {
			try {
				lock.lock();
				System.out.println("Run1 get the lock. Now sleep 3 sec");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	};
	
	Runnable run2 = new Runnable() {
		public void run() {
	
		int i = 0; 
		while (++i < 9) {
			try {
				lock.tryLock(1, TimeUnit.SECONDS);
				System.out.println("run2 get the lock. Now sleep 3 sec");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.out.println("run2 did not get the lock.");
			} finally {
				try {
					 
					lock.unlock();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	};
	
	public static void main(String[] args) throws InterruptedException {
		LockTest test = new LockTest();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(test.run1);
		Thread.sleep(1000);
		executor.execute(test.run2);
		executor.shutdown();
	}
}
