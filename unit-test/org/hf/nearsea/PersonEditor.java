package org.hf.nearsea;

import java.beans.PropertyEditorSupport;

public final class PersonEditor extends PropertyEditorSupport {
	
	public String getAsText() {
		return ((Person)getValue()).getName();
	}

	public void setAsText( String value) {
		Person p = new Person(value);
		setValue(p);
	}
}
