I am testing HttpSession. <br/>

<c:url value="/nearsea/session.htm" var="myUrl" >
	<c:param name="name" value="film"/>
</c:url>
<a href='<c:out value="${myUrl}"/>'>Clicke me</a>

