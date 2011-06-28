package org.hf.lang;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SingExecutor extends  AbstractExecutorService {

	public void execute(Runnable task) {
		task.run();
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

//		SingExecutor executor = new SingExecutor();
//		for(int i = 0; i < 10; ++i) {
//			Future<String> future = executor.submit(new SingTask<String>("jack"));
//
//			String string = future.get();
//			System.out.println(string);
//
//		}
		
		
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 20; ++i) {
			threadPool.submit(new SingTask<String>("Tom"));
		}

	}


	public boolean awaitTermination(long timeout, TimeUnit unit)
	throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}


	public void shutdown() {
		// TODO Auto-generated method stub

	}


	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

}
