package org.hf.nearsea.aop;

import org.aspectj.lang.ProceedingJoinPoint;


public class SwordWrap {
	
	public Object warp(ProceedingJoinPoint call) throws Throwable {
		 String aopStr = "AOP in action (wrap version): " +  call.proceed() + " !!!";
		 System.out.print( aopStr);
		 return aopStr;
	}

}
