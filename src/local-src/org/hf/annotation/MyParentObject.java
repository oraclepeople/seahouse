package org.hf.annotation;

import java.lang.annotation.Inherited;

@Inherited

public @interface MyParentObject { 
   boolean isInherited() default true;
   String doSomething() default "Do what?";
}