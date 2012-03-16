package org.hf.lang.forkjoin;

public class SortMessage implements Comparable<SortMessage>{

	long time;
	String msg;
	
	public long getTime() {
		return time;
	}
	public SortMessage(String message, long t) {
		msg = message;
		time = t;
	}
	
	@Override
	public int compareTo(SortMessage o) {
		return new Long(time).compareTo(o.getTime());
	}
	public String getMessage() {
		// TODO Auto-generated method stub
		return msg;
	}

}
