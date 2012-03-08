package org.hf.lang;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** Simple framework for timing concurrent execution */
public class ConcurrentTimer {
	
    private ConcurrentTimer() { } // Noninstantiable

    public static long time(Executor executor, int concurrency,  final Runnable action) throws InterruptedException {
    	
        final CountDownLatch ready = new CountDownLatch(concurrency);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
        	
        	final int j = i;
            executor.execute(new Runnable() {
                public void run() {
                	System.out.println(" ready " + j + " : countDown " + System.nanoTime());
                    ready.countDown(); // Tell timer we're ready
                    try {
                    	Thread.sleep(1000);
                    	System.out.println(" start " + j + " : await " + System.nanoTime());
                        start.await(); // Wait till peers are ready
                        System.out.println(" action " + j + " : run " +  System.nanoTime());
                        action.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                    	System.out.println(" Down " + j + " : countDown " +  System.nanoTime());
                        done.countDown();  // Tell timer we're done
                    }
                }
            });
        }

        System.out.println("Ready " +  System.nanoTime());
        ready.await();     // Wait for all workers to be ready
        long startNanos = System.nanoTime();
        
        System.out.println("Start " +  System.nanoTime());
        start.countDown(); // And they're off!
        
        System.out.println("Done await " +  System.nanoTime());
        done.await();      // Wait for all workers to finish
        return System.nanoTime() - startNanos;
    }
    
    public static void main(String[] args) throws InterruptedException {
		time (Executors.newFixedThreadPool(5),3, new Runnable() {
			
			public void run() {
				System.out.println(" my name: " + Thread.currentThread().getName() + " " + System.nanoTime());
			}
		});
	}
}
