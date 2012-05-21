package org.hf.annotation;

@MyDocumented(doTestDocument="Hello document")
@MyRetention (doTestRetention="Hello retention test")
public class AnnotatedTarget {

	public void doSomeTestRetention() {
		System.out.println("Testing annotation 'Retention'");
	}

	public void doSomeTestDocumented() {
		System.out.println("Testing annotation 'Documented'");
	}


}
