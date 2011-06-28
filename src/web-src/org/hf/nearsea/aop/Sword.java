package org.hf.nearsea.aop;

public  class Sword implements Weapon{
	
	public String getWeaponType (){
		String st = "This is a sharp sword";
		System.out.println(st);
		return st;
	}

}
