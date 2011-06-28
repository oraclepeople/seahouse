package org.hf.nearsea.utils;

public class Status {

	private String rate;
	
	public Status(int location) {
		
		if(location < 10){
			rate = "near";
		}else{
			rate = "far";
		}
	}

	public final String getRate() {
		return rate;
	}

	public final void setRate(String rate) {
		this.rate = rate;
	}
	
	
	

}
