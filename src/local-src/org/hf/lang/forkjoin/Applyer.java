package org.hf.lang.forkjoin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Applyer extends RecursiveAction {

//	List<SortMessage> list = Collections.synchronizedList(new  ArrayList<SortMessage>());

	List<SortMessage> list = new  ArrayList<SortMessage>();
	public List<SortMessage> getList() {
		return list;
	}

	final double[] array;
	final int lo, hi;
	double result;
	Applyer next; // keeps track of right-hand-side tasks

	Applyer(double[] array, int lo, int hi, Applyer next) {
		this.array = array; this.lo = lo; this.hi = hi;
		this.next = next;
		list.add( new SortMessage(" Thread name: " + Thread.currentThread().getName() + ", 000", System.nanoTime() ));
	}

	private double atLeaf(int l, int h) {
		double sum = 0;
		for (int i = l; i < h; ++i) // perform leftmost base step
			sum += array[i] * array[i];
		return sum;
	}

	protected void compute() {
		list.add( new SortMessage(" Thread name: " + Thread.currentThread().getName() + ", aaa", System.nanoTime() ));

		int l = lo;
		int h = hi;
		Applyer right = null;

		while (h - l > 1 && getSurplusQueuedTaskCount() <= 3) {
			list.add(new SortMessage(" Thread name: " + Thread.currentThread().getName() + ", bbb",  System.nanoTime() ));
			int mid = (l + h) >>> 1;
			right = new Applyer(array, mid, h, right);
			right.fork();
			h = mid;
		}

		double sum = atLeaf(l, h);
		while (right != null) {
			list.add(new SortMessage(" Thread name: " + Thread.currentThread().getName() + ", ccc", System.nanoTime()));
			if (right.tryUnfork()) // directly calculate if not stolen
				sum += right.atLeaf(right.lo, right.hi);
			else {
				right.join();
				sum += right.result;
			}
			right = right.next;
		}
		result = sum;

		list.add(new SortMessage("Result is " + result + " at "  + "; Thread name: " + Thread.currentThread().getName()  , + System.nanoTime()));
	}

	public static void main(String[] args) {
		double[] array2 = new double[]{1, 2, 3, 4, 5, 6, 7,8,9};
		Applyer task = new Applyer(array2, 0, array2.length, null);
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		forkJoinPool.invoke(task);
		Collections.sort(task.getList());

		synchronized (task.getList()) {
		for (SortMessage msg : task.getList()) {
			System.out.println(msg.getMessage() + ", time : " + msg.getTime());
		}
		}
	}
}
