
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

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Dashboard D09 6.2.1 The best-selling services. -->
<h3>
	<spring:message code="dashboard.best.selling.services" />
</h3>

<display:table class="flat-table0" name="bestSellinServices" id="row">

	<spring:message code="dashboard.service" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<spring:message code="label.requests" var="titleRequests" />
	<display:column title="${titleRequests}">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>


<!-- Dashboard D09 6.2.2 The managers who provide more services than the average. -->
<h3>
	<spring:message code="dashboard.services.manages.over.average" />
</h3>

<display:table class="flat-table0"
	name="managersWhitCountServicesOverAverage" id="row">

	<spring:message code="dashboard.manager" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<spring:message code="dashboard.average" var="titleAverage" />
	<display:column title="${titleAverage}">
		<jstl:out value="${row[2]}" />
	</display:column>
	<spring:message code="dashboard.services.number" var="titleNumber" />
	<display:column title="${titleNumber}">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>

<!-- Dashboard D09 6.2.3 The managers who have got more services cancelled. -->
<h3>
	<spring:message code="dashboard.services.cancelled.manager" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey"
	name="managersWhitMoreServicesCancelled" id="row">

	<spring:message code="dashboard.manager" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<spring:message code="dashboard.services.cancelled.number"
		var="titleNumber" />
	<display:column title="${titleNumber}">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>

<!-- Dashboard D09 11.2.1 The average number of categories per rendezvous. -->
<h3>
	<spring:message code="dashboard.avg.category.per.rendezvous" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="distinctCartoriesOfRequestedServicesAndNumberOfRendezvouses"
	id="row">

	<spring:message code="dashboard.average" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0]/row[1]}" />
	</display:column>
	<spring:message code="dashboard.rendezvus.number" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[1]}" />
	</display:column>
	<spring:message
		code="dashboard.distinct.categoriesof.requested.services.number"
		var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0]}" />
	</display:column>

</display:table>

<!-- Dashboard D09 11.2.2 The average ratio of services in each category -->
<h3>
	<spring:message code="dashboard.avg.ratio.services.per.category" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="serviceNumberAndCategoryNumber"
	id="row">

	<spring:message code="dashboard.average" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0]/row[1]}" />
	</display:column>
	<spring:message code="dashboard.services.number" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[1]}" />
	</display:column>
	<spring:message
		code="dashboard.categories.number"
		var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0]}" />
	</display:column>

</display:table>

<!-- Dashboard D09 11.2.3 The average, the minimum, the maximum, and the standard deviation of services requested per rendezvous. -->
<h3>
	<spring:message code="dashboard.avg.max.min.requested.services.per.rendezvous" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="avgMaxMinOfRequestPerRendezvous"
	id="row">

	<spring:message code="dashboard.average" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0]}" />
	</display:column>
	<spring:message code="dashboard.maximum" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[1]}" />
	</display:column>
	<spring:message code="dashboard.minimum" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[2]}" />
	</display:column>
<!-- stdev(x) = sqrt(sum(x*x)/count(x) - avg(x)*avg(x)) -->
	<spring:message code="dashboard.deviation" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${stdevOfServicesPerRendezvous}" />
	</display:column>

</display:table>
<!-- Dashboard D09 11.2.3 The top-selling services. -->
<h3>
	<spring:message code="dashboard.top.selling.services" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="bestSellinServices" id="row">

	<spring:message code="dashboard.service" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<spring:message code="label.requests" var="titleRequests" />
	<display:column title="${titleRequests}">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>


<h3>
	<spring:message code="dashboard.rendezvouses.by.user" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />: ${dashboard1[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard1[1]}</li>
</ul>

<h3>
	<spring:message code="dashboard.rendezvouses.ratio.creation" />
</h3>

<ul>
	<li><spring:message code="dashboard.ratio" />: ${dashboard2[0]} %</li>
</ul>

<h3>
	<spring:message code="dashboard.users.per.rendezvous" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />: ${dashboard3[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard3[1]}</li>
</ul>


<h3>
	<spring:message code="dashboard.rendezvouses.rsvp" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />: ${dashboard4[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard4[1]}</li>
</ul>


<h3>
	<spring:message code="dashboard.rendezvouses.top10" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="dashboard5" id="row">

	<spring:message code="rendezvous.name" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<display:column title="Nº">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>


<h3>
	<spring:message code="dashboard.announcements.ratio" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />: ${dashboard6[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard6[1]}</li>
</ul>



<h3>
	<spring:message code="dashboard.announcements.above.75" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="dashboard7" id="row">

	<spring:message code="rendezvous.name" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<display:column title="Nº">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>

<h3>
	<spring:message code="dashboard.rendezvouses.linked" />
</h3>

<display:table class="flat-table0 flat-table-1 w3-light-grey" name="dashboard8" id="row">

	<spring:message code="rendezvous.name" var="titleName" />
	<display:column title="${titleName}">
		<jstl:out value="${row[0].name}" />
	</display:column>
	<display:column title="Nº">
		<jstl:out value="${row[1]}" />
	</display:column>

</display:table>


<h3>
	<spring:message code="dashboard.questions.rendezvous" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />: ${dashboard9[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard9[1]}</li>
</ul>

<h3>
	<spring:message code="dashboard.answers.rendezvous" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />:
		${dashboard10[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard10[1]}</li>
</ul>

<h3>
	<spring:message code="dashboard.replies.comment" />
</h3>

<ul>
	<li><spring:message code="dashboard.average" />:
		${dashboard11[0]}</li>
	<li><spring:message code="dashboard.deviation" />:
		${dashboard11[1]}</li>
</ul>
<br />
<acme:backButton text="label.back" />
<br />

