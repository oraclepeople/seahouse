package org.hf.pad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * My test of deadlock avoidance
 * 
 * @author cuscab
 *
 */
public class DeadLockAvoid {
	class Friend {
		private String name;
		Lock lock = new ReentrantLock();

		public Friend(String name) {
			this.name = name;
		}

		public  void bow (Friend a) {


			try {
				boolean myLock = lock.tryLock(20L, TimeUnit.MILLISECONDS);
				boolean yourLock = a.lock.tryLock(20L, TimeUnit.MILLISECONDS);
				if(!myLock || !yourLock){
					if(!yourLock && myLock){
						lock.unlock();
					}
					if(yourLock && !myLock){
						a.lock.unlock();
					}
                   System.out.println(name + " says: I did not get all locks");
				}else {
					System.out.println( name  + " says: **** " + a.name + " bow to me. I need to bow back");
					a.bowBack(this);
					lock.unlock();
					a.lock.unlock();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			
			}
		}

		public synchronized void bowBack (Friend b) {
			boolean bowBack = true;
			System.out.println( name + " says: " + b.name + " bowBack to me " + bowBack);
		}

	}

	public static void main(String[] args) {
		DeadLockAvoid lockAvoid = new DeadLockAvoid();
		final Friend fa = lockAvoid.new  Friend("a");
		final Friend fb = lockAvoid.new  Friend("b");


		ExecutorService pool = Executors.newCachedThreadPool();
		pool.submit(new Runnable() {
			public void run() {
				for (int i = 0; i < 10; ++i)
				fa.bow(fb);
			}
		});

		pool.submit(new Runnable() {
			public void run() {
				for (int i = 0; i < 10; ++i)
				fb.bow(fa);

			}
		});


	}
}
