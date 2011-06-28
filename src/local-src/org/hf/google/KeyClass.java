package org.hf.google;

import org.hf.google.DateValue.Period;


public class KeyClass {
	private String name;
	private Period period;

	public KeyClass(String name, Period period) {
		this.name = name;
		this.period = period;
	}
	public String getName() {
		return name;
	}

	public Period getPeriod() {
		return period;
	}



	public String toString() {
		return name + "(" + period + ")";
	}
	
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((period == null) ? 0 : period.hashCode());
		return result;
	}
	 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		KeyClass other = (KeyClass) obj;

	    return (name.equals(other.name)) && ( period.equals(other.period));
		
	}
}
