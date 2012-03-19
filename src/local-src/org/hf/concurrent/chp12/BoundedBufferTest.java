package org.hf.concurrent.chp12;

import junit.framework.TestCase;

public class BoundedBufferTest extends TestCase{

	private static final long LOCKUP_DETECT_TIMEOUT = 500;

	public void testIsEmptyWhenConstructed() {
		BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}

	public void testIsFullAfterPuts() throws InterruptedException {
		BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		for (int i = 0; i < 10; i++)
			bb.put(i);
		assertTrue(bb.isFull());
		assertFalse(bb.isEmpty());
	}
	
	public void testTakeBlocksWhenEmpty() {
	    final BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
	    Thread taker = new Thread() {
	        public void run() {
	            try {
	                bb.take();
	                fail();  // if we get here, it's an error
	            } catch (InterruptedException success) { }
	        }};
	    try {
	        taker.start();
	        Thread.sleep(LOCKUP_DETECT_TIMEOUT);
	        taker.interrupt();
	        taker.join(LOCKUP_DETECT_TIMEOUT);
	        assertFalse(taker.isAlive());
//	        assertTrue(taker.isInterrupted());
	    } catch (Exception unexpected) {
	        fail();
	    }
	}

}
