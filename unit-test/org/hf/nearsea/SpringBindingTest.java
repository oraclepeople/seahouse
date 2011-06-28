package org.hf.nearsea;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * <period>
 * Spring inspect the parameter name, then check whether the binding Command has field with same name. 
 * If found, then inspect the type of the field. Spring then scan the registered editors to see whether 
 * there are editors tell it how to bind the parameter values.
 *
 *<period>
 * The test proves that two custom editors can have same class. Spring will know which editors to use 
 * by checking the field type and editor.
 * 
 * <period>
 * It also proves that two different format of parameter values can bind to same fields.
 * See {@link #testBindPerson()} and {@link #testBindPersonList()} 
 * 
 */

public final class SpringBindingTest extends TestCase  {

	private static final String BINDING_FORMAT = "dd/MM/yyyy";

	public void testBindDate() throws Exception {

		TestCommand cmd = new TestCommand();

		ServletRequestDataBinder binder = new ServletRequestDataBinder(cmd, "command");

		binder.registerCustomEditor(Date.class, 
				new CustomDateEditor(new SimpleDateFormat(BINDING_FORMAT), true,
						BINDING_FORMAT.length()));
		
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addParameter("date", "10/10/1900");
		
		binder.bind(req);
		
		assertEquals("10/10/1900", new SimpleDateFormat(BINDING_FORMAT).format(cmd.getDate()));
	}
	
	
	public void testBindPerson() {
		
		TestCommand cmd = new TestCommand();

		ServletRequestDataBinder binder = new ServletRequestDataBinder(cmd, "command");

		binder.registerCustomEditor(Person.class, new PersonEditor()); 
		
		MockHttpServletRequest req = new MockHttpServletRequest();
		
		req.addParameter("person", "John");
		req.addParameter("persons", "Evans");
		req.addParameter("persons", "David");

		binder.bind(req);
		
		assertEquals("John",  cmd.getPerson().getName());
		assertEquals(2,  cmd.getPersons().size());
		assertEquals("Evans",  cmd.getPersons().get(0).getName());
		assertEquals("David",  cmd.getPersons().get(1).getName());
	}
	
	
	public void testBindPersonList() {
		
		TestCommand cmd = new TestCommand();

		ServletRequestDataBinder binder = new ServletRequestDataBinder(cmd, "command");

		binder.registerCustomEditor(List.class, new PersonsListEditor()); 
		binder.registerCustomEditor(List.class, new StringListEditor()); 
		
		MockHttpServletRequest req = new MockHttpServletRequest();
		
		req.addParameter("persons", "Evans" + "\n" + "David");
		req.addParameter("names", "Evans" + "\n" + "David");

		binder.bind(req);
		
		assertEquals(2,  cmd.getPersons().size());
		assertEquals("Evans",  cmd.getPersons().get(0).getName());
		assertEquals("David",  cmd.getPersons().get(1).getName());
		
		assertEquals(2,  cmd.getNames().size());
		assertEquals("Evans",  cmd.getNames().get(0) );
		assertEquals("David",  cmd.getNames().get(1) );
		
		
	}


}
