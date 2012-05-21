package org.hf.annotation;

import java.lang.annotation.Annotation;

public class TestAnnotation {
	 

	public static void main(String arg[]) {
		AnnotatedTarget ta = new AnnotatedTarget();
		Annotation[] annotations = ta.getClass().getAnnotations();
		
		MyRetention annotation = ta.getClass().getAnnotation(MyRetention.class);
		System.out.println(annotation.doTestRetention());

		MyDocumented annotation2 = ta.getClass().getAnnotation(MyDocumented.class);
		System.out.println(annotation2.doTestDocument());
	}

}
