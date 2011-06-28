<%@ tag import="java.util.Date"   import="java.text.DateFormat"%>
<% 
  DateFormat dateFormat = 
    DateFormat.getDateInstance(DateFormat.LONG);
  Date now = new Date(System.currentTimeMillis());
  out.println(dateFormat.format(now));
%>

<br/>

<p>
Shows the use of the include directive. 
The first include directive demonstrates how you can include
a static resource called included.html.
<br/>
Here is the content of included.html:
<%@ include file="included.html" %>
<br/>
<br/>
The second include directive includes another 
dynamic resource: included.tagf.
<br/>
<%@ include file="included.tagf" %>