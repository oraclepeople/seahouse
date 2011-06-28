package org.hf.nearsea.utils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class HouseTag extends TagSupport{
	
	private int distance;
	
	/** No need to set getXX and setXX */
	private Status status;
	
	private static final long serialVersionUID = -3135328570862109587L;

	private static final String STATUS_VARIABLE_NAME = "status";

	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspException {
		
		status = new Status(distance);
		pageContext.setAttribute(STATUS_VARIABLE_NAME, this.status, PageContext.REQUEST_SCOPE);
		
		return EVAL_BODY_INCLUDE;
	}

	public final int getDistance() {
		return distance;
	}

	public final void setDistance(int location) {
		this.distance = location;
	}
	

}
