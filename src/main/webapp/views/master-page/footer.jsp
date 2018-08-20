<%--
 * footer.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="date" class="java.util.Date" />

<div class="toRight">
<b>Copyright &copy; <fmt:formatDate value="${date}" pattern="yyyy" /> Acme-CRM Co., Inc.</b>
<a href="term/termsAndConditions.do"><spring:message code="term.terms"/></a>
<a href="term/cookies.do"><spring:message code="term.cookie"/></a>
</div>
<div id="barraaceptacion">
    <div class="inner">
    	<spring:message code="term.cookie.banner"/>
        <a href="javascript:void(0);" class="ok" onclick="PonerCookie();"><b>OK</b></a> | 
        <a href="term/cookies.do"  class="info"><spring:message code="term.cookie"/></a>
    </div>
</div>

<script>
if(getCookie('avisocookie')!="1"){
    document.getElementById("barraaceptacion").style.display="block";
}
</script>