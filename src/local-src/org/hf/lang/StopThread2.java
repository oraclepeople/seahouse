package org.hf.lang;

import java.util.concurrent.TimeUnit;

/**
 * If only the <code>synchronized</code> modifier in the first method is commented out, the thread will stop after specified seconds.
 * If the <code>synchronized</code> in both method are commented out as well, the thread will loops forever.
 * If only the <code>synchronized</code> in the second method is commented out, the thread will loops forever.
 * 
 */

public class StopThread2 {
	
	private static boolean stopRqueted;
	
	private static synchronized void requestStop() {
		stopRqueted = true;
	}
	
	private static  /*synchronized*/  boolean stopRequested() {
		return stopRqueted ;
	}
	
	public static void main(String[] args)  throws InterruptedException{
		Thread backgroundThread = new Thread (new Runnable() {

			public void run () {
				int i = 0;
				while ( !stopRequested()) {
					i++;
				}
			}
		});

		backgroundThread.start();
		TimeUnit.SECONDS.sleep(3);
		requestStop();
	}

}
