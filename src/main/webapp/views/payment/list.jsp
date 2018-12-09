<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.hibernate.engine.spi.RowSelection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jsp:useBean id="date" class="java.util.Date" />
<div class="formForm">
<display:table pagesize="5" class="flat-table0 flat-table-1 w3-light-grey" name="incidencias" requestURI="${requestUri}" id="row">

	<jstl:if test="${row.publicationDate < date}">
		<jstl:set var="classTd" value="passed" />
	</jstl:if>
	<jstl:if test="${row.publicationDate > date}">
		<jstl:set var="classTd" value="" />
	</jstl:if>
	<jstl:if test="${row.cancelled == true}">
		<jstl:set var="classTd" value="${classTd} adult" />
	</jstl:if>
	<spring:message code="incidencia.ticker" var="rendezvousName" />
	<display:column property="ticker" title="${rendezvousName}" class="${classTd}"/>
	<spring:message code="incidencia.name" var="rendezvousName" />
	<display:column property="title" title="${rendezvousName}" class="${classTd}"/>
	
	
	<spring:message code="incidencia.description" var="rendezvousDescription" />
	<display:column property="description" title="${rendezvousDescription}" class="${classTd}"/>



	<spring:message code="moment.format" var="momentFormat" />
	<spring:message code="incidencia.publication.moment" var="momentTilte" />
	<display:column property="publicationDate" title="${momentTilte}" format="${momentFormat}" class="${classTd}"/>	
	
	
	<spring:message code="incidencia.user" var="userTitle" />
	<display:column title="${userTitle}" class="${classTd}">
		<div>
			<a href="user/display.do?userId=${row.user.id}"> 
				<jstl:out value="${row.user.name}"/>
			</a>
		</div>
	</display:column>	
	
	
	<spring:message code="incidencia.user" var="rendezvousUser" />
	<display:column title="${rendezvousUser}" class="${classTd}">
		<div>
			<a href="technician/display.do?userId=${row.user.id}"> 
				<jstl:out value="${row.technician.name}"/>
			</a>
		</div>
	</display:column>	
	
	
	<security:authorize access="isAnonymous()">	

	</security:authorize>	
	
	<security:authorize access="hasRole('USER')">
	
	</security:authorize>	

	<security:authorize access="hasRole('ADMINISTRATOR')">
		
	</security:authorize>

	
	<display:column class="${classTd}">
			<div>
				<a href="incidencia/user/edit.do?id=${row.id}">
					<spring:message code="label.view" />
				</a>
			</div>
		</display:column>

</display:table>

<acme:backButton text="label.back" />
<acme:button url="/incidencia/user/create.do" text="label.new" />
<br />
</div>	
