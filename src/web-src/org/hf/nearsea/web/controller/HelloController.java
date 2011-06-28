package org.hf.nearsea.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hf.nearsea.aop.Weapon;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public final class HelloController implements Controller{

	private String helloView;
	
	private Weapon weapon;
	 

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	   throws Exception {
		
		Map<String, String> model = new HashMap<String, String>();
		
		model.put("helloValue", "yilin");
		
		if (weapon != null) {
		  model.put("weaponType", weapon.getWeaponType());
		}
		
		model.put("info", "info can be displayed ?");
		ModelAndView mv = new ModelAndView(helloView, model);
		return mv;
	}

	 

	public void setHelloView(String helloView) {
		this.helloView = helloView;
	}



	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
}
