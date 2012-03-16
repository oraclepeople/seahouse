package org.hf.pad;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RecursiveActionTest extends RecursiveAction {

	private static final long serialVersionUID = 1L;

	public static final int[] nums = new int[] {1,2,3,4,5,6,7,8, 9};
	public static int[] negNum = new int[nums.length];

	public int[] getNegNum() {
		return negNum;
	}

	int start = 0;
	int end = nums.length;
	RecursiveActionTest next ;


	public RecursiveActionTest(int start, int end, RecursiveActionTest next) {
		this.start = start;
		this.end = end;
		this.next = next;
	}

	protected void compute() {

		int l = start;
		int h = end;

		RecursiveActionTest right = null;
		while ( (h - l > 2) && getSurplusQueuedTaskCount() < 3 ) {
			int mid = (h - l) / 2;
			right = new RecursiveActionTest(mid, h, right);
			right.fork();
			h = mid;
		}

		doIt(l,h);
		while(right != null) {
//		  if(right.tryUnfork()) {
//			  right.doIt(right.start, right.end);
//		  }else {
			right.join();
//		  }
		  right = right.next;
		}

	}


	private void doIt(int l, int h) {
		for (int i = l; i < h; ++i){
			negNum[i] = 0 - nums [i]; 
		}
	}

	public static void main(String[] args) {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		RecursiveActionTest task = new RecursiveActionTest(0, RecursiveActionTest.nums.length, null);
		forkJoinPool.invoke(task);

		for ( int i : task.getNegNum()) {
			System.out.println(i);
		}

	}



}
