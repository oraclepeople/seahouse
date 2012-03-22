package org.hf.annotation;

import java.lang.annotation.Annotation;

public class CarAnnotationsTest {

	public static void main(String[] args) {

		Class[] classes = {AbstractBMWCar.class, BMWRoadsterCar.class};

		for (Class classObj : classes) {
			Annotation[] annotations = classObj.getAnnotations();
			System.out.println("No. of annotations: " + annotations.length);

			for (Annotation annotation : annotations) {
				CarAnnotation carAnnotation = (CarAnnotation)annotation;
				System.out.println(carAnnotation.maker());

			}
		}
	}
}
