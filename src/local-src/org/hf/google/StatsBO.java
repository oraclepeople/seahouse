package org.hf.google;

import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;

public class StatsBO {
	
	public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("##.#%");
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("###,###.#"); 

	private String name;
	private List<Float> values = Lists.newArrayList();
	private boolean percent;
	
	public StatsBO(String name, List<Float> values, boolean percent) {
		super();
		this.name = name;
		this.values = values;
		this.percent = percent;
	}
	
	public boolean isPercent() {
		return percent;
	}

	public String getName() {
		return name;
	}
	public List<Float> getValues() {
		return values;
	}
	
	
	public float averageValue() {
		float avg = 0.0f;
		int j = 0;
		for (float f : values){
			avg += f;
			++j;
		}
		avg = avg / j;
		return avg;
	}

	public void prettyPrint() {
		
		List<String> list = Lists.newArrayList(name);
		String strFormat = "%1$-35s";
		
		int i = 0; 
        for (Float v : values) {
        	strFormat += " %" + (i + 2) + "$10s";
        	++i;
        	if(percent) {
        	   list.add(PERCENT_FORMAT.format(v));
        	} else { 
        	  list.add(NUMBER_FORMAT.format(v));
        	}
        }
        strFormat +="\n";
        
		System.out.format(strFormat, list.toArray());
	}	

//	private void List<String> growth) {
//	String strFormat = "%1$-35s";
//    for (int i = 1; i < growth.size(); ++i) {
//    	strFormat += " %" + (i + 1) + "$10s";
//    }
//    strFormat +="\n";
//    
//	System.out.format(strFormat, growth.toArray());
//}	



}
