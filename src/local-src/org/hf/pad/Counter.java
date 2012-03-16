package org.hf.pad;


public class Counter {
	volatile int i = 0; 
	Object o = new Object();
	
	public  void increase(){
		synchronized (o) {
			++i;
		}
	}
	
	public int getCounter() {
		return i;
	}

}
