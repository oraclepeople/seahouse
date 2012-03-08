package org.hf.effect.item67;

import java.util.ArrayList;
import java.util.List;

public class TestRemoveElementWhileIteration {
	
	public static void main(String[] args) {
		
	  	List<SetObserver<Integer>> set = new ArrayList<SetObserver<Integer>>();
	  	for ( int i = 0; i < 10; ++i) {
	  		set.add(new SetObserver<Integer>() {
				
				@Override
				public void added(ObservableSet<Integer> set, Integer element) {
					// TODO Auto-generated method stub
					
				}
			});
	  	}
	  	
	  	// Cannot remove element while iterating
//	  	for(Integer j : set) {
//	  		if (j == 5) {
//	  			set.remove(j);
//	  		}
//	  	}
	  	
	  	 ArrayList<SetObserver<Integer>> snapshot = new ArrayList<SetObserver<Integer>>(set);
	  	
	  	 int j = 0;
	  	for(SetObserver<Integer> i : snapshot) {
	  		if (j++ == 5) {
	  			set.remove(i);
	  		}
	  	}
	  	
	  	System.out.println("set size: " + set.size());
	  	System.out.println("snap size: " + snapshot.size());
	}

}
