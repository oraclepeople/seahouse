package org.hf.nearsea.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AspectJKnifeAdvice {

	@Around("execution(* org.hf.nearsea.aop.Knife.*())")
	public Object doAdvice(ProceedingJoinPoint pjp) throws Throwable { 
			 return "AspectJ in Action " + pjp.proceed() + " !!";
	}


}
