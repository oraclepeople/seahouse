package org.hf.pad;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/** 
 * Test the Future class
 * @author cuscab
 *
 */
public class FutureTest {
  public void getFutureResult() throws InterruptedException, ExecutionException {
	  FutureTask<String> task = new FutureTask<String>(new Callable<String>() {

		public String call() throws Exception {
		  Thread.sleep(20);
		  return "I am done";
		}
	});
	  
	  ExecutorService pool = Executors.newCachedThreadPool();
	  pool.submit(task);
	  
	  for ( int i = 0; i < 10; ++i) {
		  System.out.println( "loop " + i + " : " +  (task.isDone() ? task.get() :  " Not yet"));
		  Thread.sleep(5);
	  }
	  pool.shutdown();
  }
  
  public static void main(String[] args) throws InterruptedException, ExecutionException {
	 new FutureTest().getFutureResult();
}
  
}
