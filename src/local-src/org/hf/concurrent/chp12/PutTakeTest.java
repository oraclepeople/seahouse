package org.hf.concurrent.chp12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class PutTakeTest {

	private static final ExecutorService pool = Executors.newCachedThreadPool();
	protected final AtomicInteger putSum = new AtomicInteger(0);
	protected final AtomicInteger takeSum = new AtomicInteger(0);
	
	protected final CyclicBarrier barrier;
	protected final BoundedBuffer<Integer> bb;
	protected final int nTrials, nPairs;

	public static void main(String[] args) {
		new PutTakeTest(10, 10, 100000).test(); // sample parameters
		pool.shutdown();
	}

	PutTakeTest(int capacity, int npairs, int ntrials) {
		this.bb = new BoundedBuffer<Integer>(capacity);
		this.nTrials = ntrials;
		this.nPairs = npairs;
		this.barrier = new CyclicBarrier(npairs*  2 + 1);
	}

	void test() {
		try {
			for (int i = 0; i <  nPairs; i++) {
				pool.execute(new Producer());
				pool.execute(new Consumer());
			}
			
			barrier.await(); // wait for all threads to be ready
			
			barrier.await(); // wait for all threads to finish
//			assertEquals(putSum.get(), takeSum.get());
			assert putSum.get() != takeSum.get(); ;
		} catch (Throwable e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	static int xorShift(int y) {
		y ^= (y << 6);
		y ^= (y >>> 21);
		y ^= (y << 7);
		return y;
	}

	/* inner classes of PutTakeTest (Listing 12.5)  */
	class Producer implements Runnable {
		public void run() {
			try {
				int seed = (this.hashCode() ^ (int)System.nanoTime());
				int sum = 0;
				barrier.await();
				
				for (int i = nTrials; i > 0; --i) {
					bb.put(seed);
					sum += seed;
					seed = xorShift(seed);
				}
				
				putSum.getAndAdd(sum);
				barrier.await();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	class Consumer implements Runnable {
		public void run() {
			try {
				barrier.await();
				int sum = 0;
				for (int i = nTrials; i > 0; --i) {
					sum += bb.take();
				}
				takeSum.getAndAdd(sum);
				barrier.await();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

