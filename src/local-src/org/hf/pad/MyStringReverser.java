package org.hf.pad;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MyStringReverser {

	ExecutorService executor = Executors.newFixedThreadPool(1);
	SlowStringReverser reverser = new SlowStringReverser();

	void doReverse (final String target) {
		
		Callable<String> callable = new Callable<String>() {
			public String call() throws Exception {
				return reverser.reverseString(target);
			}
		}; 

		FutureTask<String> future = new FutureTask<String> (callable);

		executor.execute(future);

		while (! future.isDone()) {
			System.out.println("Task not yet completed.");
			try
			{
				Thread.sleep(500);
			} catch (InterruptedException ie)
			{
				System.out.println("Will check after 1/2 sec.");
			}
		}
		
		try
		{
			System.out.println("Here is the result..." + future.get());
		} catch (ExecutionException e)	{
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		executor.shutdown();
		
	}
	
	public static void main(String[] args) {
		MyStringReverser stringReverser = new MyStringReverser();
		stringReverser.doReverse("hongfeng");
	}
}

class SlowStringReverser {

	StringBuffer orgString;

	StringBuffer reversedString;

	SlowStringReverser(String orgStr)
	{
		orgString = new StringBuffer(orgStr);
	}

	SlowStringReverser()
	{

	}

	public String reverseString(String str)	    {
		orgString = new StringBuffer(str);
		reversedString = new StringBuffer();
		for (int i = (orgString.length() - 1); i >= 0; i--)
		{

			reversedString.append(orgString.charAt(i));
			System.out.println("Reversing one character per second."
					+ reversedString);
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException ie)
			{
			}
		}
		return reversedString.toString();
	}

}