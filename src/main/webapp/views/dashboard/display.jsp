
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<div class="seccion w3-light-gray">

<h3>
	<spring:message code="administrator.rankingTop3Users" />
</h3>
<jstl:forEach var="c" items="${rankingTop3Users}" varStatus="loop" end="3">
	<jstl:out value="${c[0]}" /> <jstl:out value="${c[1]}" />:
	<jstl:out value="${c[2]}"/>
	<jstl:if test="${!loop.last}">, </jstl:if>
</jstl:forEach>
<br />

<h3>
	<spring:message code="administrator.rankingTop3Technicians" />
</h3>
<jstl:forEach var="c" items="${rankingTop3Technicians}" varStatus="loop"
	end="3">
	<jstl:out value="${c[0]}" /> <jstl:out value="${c[1]}" />:
		<jstl:out value="${c[2]}"></jstl:out>
	<jstl:if test="${!loop.last}">, </jstl:if>
</jstl:forEach>
<br />

<h3>
	<spring:message code="administrator.percMessagesSenderByActor" />
</h3>
<jstl:forEach var="c" items="${percMessagesSenderByActor}"
	varStatus="loop">
	<jstl:out value="${c[1]}" />:
		<jstl:out value="${c[0]}"></jstl:out>
	<jstl:if test="${!loop.last}">, </jstl:if>
</jstl:forEach>
<br />

<h3>
	<spring:message code="administrator.avgRequestByResponsible" />
</h3>
<jstl:out value="${avgRequestByResponsable}"></jstl:out>
<br />

<h3>
	<spring:message code="administrator.minRequestByResponsible" />
</h3>
<jstl:out value="${minRequestByResponsable}"></jstl:out>
<br />

<h3>
	<spring:message code="administrator.maxRequestByResponsible" />
</h3>
<jstl:out value="${maxRequestByResponsable}"></jstl:out>
<br />

<h3>
	<spring:message code="administrator.desvRequestByResponsible" />
</h3>
<jstl:out value="${stddevRequestByResponsable}"></jstl:out>
<br />


</div>