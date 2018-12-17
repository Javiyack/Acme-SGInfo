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
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<security:authorize access="hasRole('ADMINISTRATOR')">
	<spring:message code="msg.delete.confirmation" var="deleteConfirmation" />
	<div class="seccion w3-light-grey">
		<legend>
			<spring:message code="label.tabooWords"/>
		</legend><ul class="w3-ul">

		<jstl:forEach items="${tabooWords}" var="row">
			<jstl:set var="deleteUrl" value="tabooWord/administrator/delete.do?tabooWordId=${row.id}" />
			<li class="w3-display-container iButton w3-hover-white">
				<div onclick="relativeRedir('tabooWord/administrator/edit.do?tabooWordId=${row.id}');">
					<jstl:out value="${row.text}" />
				</div>
				<span class="fas fa-times fw w3-large w3-transparent w3-display-right w3-padding iButton w3-hover-text-gray"
					  onclick="showConfirmationAlert('${deleteConfirmation}', '${row.text}', '${deleteUrl}');"/>
			</li>
		</jstl:forEach>
	</ul>
		<hr>
		<acme:button url="tabooWord/administrator/create.do"
					 text="tabooWord.create" css="formButton toLeft" />
<spring:message code="tabooWord.seek.tooltip" var="tooltip"/>
		<acme:button url="tabooWord/administrator/seek.do"
					 text="tabooWord.seek" css="formButton toLeft" title="${tooltip}" />

		<br />
	</div>
</security:authorize>