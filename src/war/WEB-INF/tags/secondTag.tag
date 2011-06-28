<%@ attribute name="input" required="true" %>
<%!
  private String encodeHtmlTag(String tag, String info) {
    if (tag==null)
      return null;
    int length = tag.length();
    StringBuffer encodedTag = 
      new StringBuffer(2 * length);
    for (int i=0; i<length; i++) {
      char c = tag.charAt(i);
      if (c=='>')
        encodedTag.append("<");
      else if (c=='>')
        encodedTag.append(">");
      else if (c=='&')
        encodedTag.append("&");
      else if (c=='"')
        encodedTag.append("\"");  
      else if (c==' ')
        encodedTag.append(" ");
      else
        encodedTag.append(c);

    }
    return encodedTag.toString();
  }
%>
<%=encodeHtmlTag(input, info)%>