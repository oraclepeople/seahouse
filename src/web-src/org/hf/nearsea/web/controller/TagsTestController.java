package org.hf.nearsea.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public final class TagsTestController extends MultiActionController{
	
	private String searchEngine;
	
	private String viewReferer;
	
	private String mainView;
	
	public ModelAndView  searchEngine(HttpServletRequest req, HttpServletResponse res){
		return new ModelAndView(searchEngine);
	}
	
	public ModelAndView  viewReferer(HttpServletRequest req, HttpServletResponse res){
		return new ModelAndView(viewReferer);
	}

	public ModelAndView  main(HttpServletRequest req, HttpServletResponse res){
		return new ModelAndView(mainView);
	}

	public  void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	public  void setViewReferer(String viewReferer) {
		this.viewReferer = viewReferer;
	}

	public  void setMainView(String main) {
		this.mainView = main;
	}

}
