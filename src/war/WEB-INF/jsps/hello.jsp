<html>
<%@ page session="false"%>

<%@ taglib prefix="seaweave" uri="/WEB-INF/tld/seaweave.tld" %>

<body>

<p>Hello ${helloValue}

<p><b>Test AOP</b> </p> 
<p>The weapon type : ${weaponType} </p>

<p><b>Test Tag library </b></p>
<seaweave:house distance="0">
<p>What's the rating of the house ? ${status.rate}</p>
</seaweave:house>

<p><b>Test <a href="http://today.java.net/pub/a/today/2003/11/14/tagfiles.html">Tag files</a></b></p>
<p>Today is <easy:firstTag/></p>

<p><b> Test tag include attribute</b></p>
<p> <easy:secondTag input="<br/> means changing line" /></p>


<p><b> Test tag variable attribute</b></p>
<p> Tag variable exposes its variable to be used in calling jsp</p>
<easy:varDemo>
In long format: ${longDate}
<br/>
In short format: ${shortDate}
</easy:varDemo>


</body>
</html>