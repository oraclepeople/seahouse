package org.hf.nearsea;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.util.StringUtils;

public final class StringListEditor extends PropertyEditorSupport {
	

    /**
     * @see java.beans.PropertyEditor#getAsText()
     */
	@SuppressWarnings("unchecked")
	public String getAsText() {
        List<String> list = (List<String>) getValue();
        String text = "";
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                text += "\n";
            }
            text += list.get(i);
        }

        return text;
    }


    /**
     * @see java.beans.PropertyEditor#setAsText(java.lang.String)
     */
    public void setAsText(final String value) {
        StringTokenizer tokenizer = new StringTokenizer(value, "\n", false);
        List<String> list = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            //do not trim the leading white space, which might be used in pair dropdown list
 			list.add(StringUtils.trimTrailingWhitespace(nextToken));
        }

        super.setValue(list);
    }
}
