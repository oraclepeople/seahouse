package org.hf.lang;

import java.util.concurrent.TimeUnit;

public class StopThread3 {
	private static volatile boolean stopRqueted;

	public static void main(String[] args)  throws InterruptedException{
		Thread backgroundThread = new Thread (new Runnable() {

			public void run () {
				int i = 0;
				while ( !stopRqueted) {
					i++;
				}
			}
		});

		backgroundThread.start();
		
		TimeUnit.SECONDS.sleep(1);
		stopRqueted = true;
	}
}
