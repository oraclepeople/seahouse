package org.hf.nearsea.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SwordExample {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/org/hf/nearsea/aop/sword.xml");
		Weapon sword = (Weapon) ctx.getBean("sword");
		sword.getWeaponType();
	}

}
