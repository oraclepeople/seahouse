package org.hf.lang.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * The following example illustrates some refinements and idioms that may lead to better performance: 
 * RecursiveActions need not be fully recursive, so long as they maintain the basic divide-and-conquer approach. 
 * 
 * <br/>
 * Here is a class that sums the squares of each element of a double array, 
 * by subdividing out only the right-hand-sides of repeated divisions by two, 
 * and keeping track of them with a chain of next references. 
 * 
 * <br/>
 * It uses a dynamic threshold based on method getSurplusQueuedTaskCount, 
 * but counterbalances potential excess partitioning by 
 * directly performing leaf actions on unstolen tasks rather than further subdividing.
 * 
 * @author cuscab
 *
 */
public class Applyer2 extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	
	final double[] array;
	final int lo, hi;
	double result;
	Applyer2 next; // keeps track of right-hand-side tasks
	
	final static long start = System.nanoTime(); 

	Applyer2(double[] array, int lo, int hi, Applyer2 next) {
		this.array = array; this.lo = lo; this.hi = hi;
		this.next = next;
//		System.out.println(" Thread name: " + Thread.currentThread().getName() + ", constructor, " +  System.nanoTime() );
	}

	private double atLeaf(int l, int h) {
		double sum = 0;
		for (int i = l; i < h; ++i) // perform leftmost base step
			sum += array[i] * array[i];
		return sum;
	}

	protected void compute() {
		

		int l = lo;
		int h = hi;
		System.out.println("Thread name: " + Thread.currentThread().getName() + "[" + l + "," + h + "], enter, "  + ( System.nanoTime() - start) );
		Applyer2 right = null;

		while (h - l > 2 && getSurplusQueuedTaskCount() <= 3) {
			System.out.println("   Thread name: " + Thread.currentThread().getName() + "[" + l + "," + h + "], fork my right," +  ( System.nanoTime() - start) );
			//int mid = (l + h) >>> 1;
			int mid = (l + h) /2;
			right = new Applyer2(array, mid, h, right);
			right.fork();
			
			h = mid;
		}

		double sum = atLeaf(l, h);
		System.out.println("     " +  Thread.currentThread().getName() + "[" + l + "," + h + "] " + ( System.nanoTime() - start) +  " cal, (" + l + "," + h + ") sum is " + sum );
		while (right != null) {
			if (right.tryUnfork()) {// directly calculate if not stolen
				System.out.println("\n Thread name: " + Thread.currentThread().getName() + "[" + l + "," + h + "], right calculate, " +  ( System.nanoTime() - start) +  " (" + right.lo + "," + right.hi + ")");
				sum += right.atLeaf(right.lo, right.hi);
			}
			else {
				System.out.println("       Thread name: " + Thread.currentThread().getName() + "[" + l + "," + h + "], join start, " +  ( System.nanoTime() - start));
				right.join();
				sum += right.result;
			}
			right = right.next;
		}
		result = sum;

		System.out.println("               Result is " + result + " at "  + "; Thread name: " + Thread.currentThread().getName()  + "[" + l + "," + h  + "], " + ( System.nanoTime() - start));
	}

	public static void main(String[] args) {
		
		double[] array2 = new double[]{0, 1, 2, 3, 4, 5};
		Applyer2 task = new Applyer2(array2, 0, array2.length, null);
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.invoke(task);
		 
	}
}
