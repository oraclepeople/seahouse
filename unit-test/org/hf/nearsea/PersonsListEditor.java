package org.hf.nearsea;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.util.StringUtils;

public class PersonsListEditor  extends PropertyEditorSupport {

	@SuppressWarnings("unchecked")
	public String getAsText() {
		List<Person> list = (List<Person>) getValue();
		String text = "";
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				text += "\n";
			}
			text += list.get(i).getName();
		}

		return text;
	}

	public void setAsText( String value) {
		List<Person> list = new ArrayList<Person>();
		
		StringTokenizer tokenizer = new StringTokenizer(value, "\n", false);
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			//do not trim the leading white space, which maight be used in pair dropdown list
			list.add( new Person(StringUtils.trimTrailingWhitespace(nextToken)));
		}

		super.setValue(list);
	}
}
