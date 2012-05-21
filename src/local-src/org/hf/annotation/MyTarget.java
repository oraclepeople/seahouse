package org.hf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface MyTarget {
   public String doTestTarget();
}

