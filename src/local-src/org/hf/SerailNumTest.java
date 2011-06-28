package org.hf;

public class SerailNumTest  implements Runnable{
    
	// The next serial number to be assigned
    private static int nextSerialNum = 0;

    private static ThreadLocal<Integer> serialNum = new ThreadLocal<Integer>() {
        protected synchronized Integer initialValue() {
            return new Integer(nextSerialNum++);
        }
    };

    public static int get() {
        return serialNum.get().intValue();
    }

	public void run() {
		System.out.println("Hello from a thread!" + get());
        serialNum.set(new Integer(get() * 10));
        System.out.println("Hello afer a thread!" + get());
	}
	
	public static void main(String args[]) {
        SerailNumTest serailNumTest = new SerailNumTest();
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; ++i) {
        	threads[i] = new Thread(serailNumTest);
        	threads[i].start();
		}
    }

}
