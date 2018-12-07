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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:useBean id="date" class="java.util.Date" />

<security:authorize access="isAuthenticated()">
	<jstl:set var="colom" value=", " />
	<security:authentication property="principal.username" var="username" />
	<security:authentication property="principal" var="logedActor" />
	<security:authentication property="principal.authorities[0]"
		var="permiso" />
	<jstl:set var="rol" value="${fn:toLowerCase(permiso)}/" />
</security:authorize>
<jstl:set var="rol" value="${fn:toLowerCase(permiso)}" />
<div class="seccion w3-light-grey">
	<div class="w3-row-padding w3-margin-top">
					<div class="w3-padding">

			<jstl:if test="${pageSize == null}">
				<jstl:set value="20" var="pageSize" />
			</jstl:if>
			<form:form action="${requestUri}" method="GET">
				<spring:message code="pagination.size" />
				<input hidden="true" name="word" value="${word}">
				<input type="number" name="pageSize" min="1" max="100"
					value="${pageSize}">
				<input type="submit" value=">">
			</form:form>


			<display:table pagesize="${pageSize}" class="displaytag w3-text-black"
				name="customers" requestURI="${requestUri}" id="row">


				<spring:message code="label.name" var="title" />
				<display:column property="name" title="${title}" sortable="true"
					class="${classTd}" />
				<acme:column property="description" title="label.description" />
				<acme:column property="webSite" title="customer.website" />
				<acme:column property="fechaAlta" title="customer.registration.date" format="moment.format" />
				 />
				<display:column class="${classTd}">
					<jstl:set var="owns"
						value="${rol=='manager' or logedCustomerId==row.id}" />

					<jstl:if test="${owns}">
						<div>
							<a href="customer/edit.do?id=${row.id}"> <i
								class="fa fa-edit w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
					<jstl:if test="${!owns}">
						<div>
							<a href="customer/display.do?id=${row.id}"> <i
								class="fa fa-eye w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
				</display:column>
				<display:column class="${classTd}">
					<jstl:if test="${rol=='manager' or rol=='technician'}">
						<div>
							<a href="incidence/internal/create.do?customerId=${row.id}"> <i
								class="	fa fa-ambulance w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
				</display:column>

			</display:table>

			<acme:backButton text="label.back" css="formButton toLeft" />
			<acme:button url="/customer/create.do" text="label.new" />
			<br />
		</div>
	</div>
</div>