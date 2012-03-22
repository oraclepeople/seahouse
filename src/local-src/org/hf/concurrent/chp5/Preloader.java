package org.hf.concurrent.chp5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.hf.concurrent.LaunderThrowable;

/**
 * Preloader
 *
 * Using FutureTask to preload data that is needed later
 *
 * @author Brian Goetz and Tim Peierls
 */

public class Preloader {
	
    ProductInfo loadProductInfo() throws DataLoadException {
         System.out.println("load product information");
         return null;
    }

    private final FutureTask<ProductInfo> future =
        new FutureTask<ProductInfo> (new Callable<ProductInfo>() {
            public ProductInfo call() throws DataLoadException {
                return loadProductInfo();
            }
    });
    
    private final Thread thread = new Thread(future);

    public void start() { thread.start(); }

    public ProductInfo get()  throws DataLoadException, InterruptedException {
        try {
        	System.out.println("Get information");
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException) cause;
            else
                throw LaunderThrowable.launderThrowable(cause);
        }
    }

    interface ProductInfo {}
    
    public static void main(String[] args) throws DataLoadException, InterruptedException {
		Preloader preloader = new Preloader();
		preloader.start();
		preloader.get();
		
	}
}

class DataLoadException extends Exception { }
