package org.hf.nearsea;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class TestCommand {
	
	private Date date = new Date();
	
	private Person person ;
	
	List<Person> persons = new ArrayList<Person>();
	
	List<String> names;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

}
