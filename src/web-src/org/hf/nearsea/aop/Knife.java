package org.hf.nearsea.aop;

public class Knife  implements Weapon{
	
	public String getWeaponType (){
		
		String st = "This is a sharp knife";
		System.out.println(st);
		return st;
	}

}
