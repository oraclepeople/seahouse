package org.hf.nearsea.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public final class SessionController implements Controller{

	private static final Logger LOGGER = Logger.getLogger(SessionController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String attrName = "film";
		if(request.getParameter("name") != null && request.getParameter("name") !=""){
			HttpSession session = request.getSession(true);
			Object attribute = session.getAttribute(attrName);
			LOGGER.info("attribute film : value: " + attribute);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("sessionFilm", (String) attribute);
			return new ModelAndView("session2", map);
		}
		
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(10);
		session.setAttribute(attrName, "good film");
		return new ModelAndView("session1");
		
	}

}
