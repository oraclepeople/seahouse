package org.hf.nearsea.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public  class SwordAdvice implements MethodInterceptor{

	public Object invoke(MethodInvocation invocation) throws Throwable {
		 String aopStr = "AOP in action: " + invocation.proceed() + " !!!";
		 System.out.print( aopStr);
		 return aopStr;
	}

}
