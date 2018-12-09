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

<security:authorize access="isAuthenticated()">
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
	<jstl:if test="${rol == 'user' or rol == 'responsible'}">
		<jstl:set value="external" var="accesscontrol" />
	</jstl:if>
	<jstl:if
		test="${rol == 'technician' or rol == 'manager' or rol == 'administrator'}">
		<jstl:set value="internal" var="accesscontrol" />
	</jstl:if>
	<div class="seccion w3-light-grey">
		<div class="w3-row-padding w3-margin-top">

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


			<display:table pagesize="${pageSize}" class="flat-table0 flat-table-1 w3-light-grey"
						   name="incidences" requestURI="${requestUri}" id="row">
				<jstl:set var="classTd" value="w3-flat-pomegranate" />				
				<jstl:if test="${row.startingDate < date}">
					<jstl:set var="classTd" value="w3-flat-sun-flower" />
				</jstl:if>
				<jstl:if test="${row.endingDate < date}">
					<jstl:set var="classTd" value="w3-green" />
				</jstl:if>
				<jstl:if test="${row.cancelled == true}">
					<jstl:set var="classTd" value="w3-flat-asbestos w3-text-grey" />
				</jstl:if>
				<spring:message code="incidencia.ticker" var="title" />
				<display:column sortable="true" property="ticker" title="${title}"
					class="${classTd}" />
				<acme:column title="label.customer" property="user.customer.name"
					sortable="true" css="${classTd}"/>
				<spring:message code="incidencia.user" var="userTitle" />
				<display:column title="${userTitle}" class="${classTd}">
					<div>
						<a href="actor/display.do?actorId=${row.user.id}"> <jstl:out
								value="${row.user.userAccount.username}" />
						</a>
					</div>
				</display:column>
				<spring:message code="incidencia.technician" var="title" />
				<display:column title="${title}" class="${classTd}" sortable="true">
					<div>
						<a href="actor/display.do?actorId=${row.technician.id}"> <jstl:out
								value="${row.technician.userAccount.username}" />
						</a>
					</div>
				</display:column>
				
				<spring:message code="incidencia.name" var="title" />
				<display:column property="title" title="${title}" class="${classTd}" />


				



				<spring:message code="moment.format" var="momentFormat" />
				<spring:message code="incidencia.publication.moment"
					var="momentTilte" />
				<display:column property="publicationDate" title="${momentTilte}"
					format="${momentFormat}" class="${classTd}" sortable="true"/>


				<spring:message code="moment.format" var="momentFormat" />
				<spring:message code="incidencia.ending.moment"
					var="momentTilte" />
								
				<display:column property="endingDate" title="${momentTilte}"
					format="${momentFormat}" class="${classTd}" sortable="true"/>


				
				

				<security:authorize access="isAnonymous()">

				</security:authorize>

				<security:authorize access="hasRole('USER')">

				</security:authorize>

				<security:authorize access="hasRole('ADMINISTRATOR')">

				</security:authorize>


				<display:column class="${classTd}" sortable="true">
					<jstl:set var="owns"
						value="${logedActor.id == row.user.userAccount.id or logedActor.id == row.technician.userAccount.id}" />

					<jstl:if test="${owns}">
						<div>
							<a href="incidence/${accesscontrol}/edit.do?id=${row.id}"> <i
								class="fa fa-edit w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
					<jstl:if test="${!owns}">
						<div>
							<a href="incidence/${accesscontrol}/display.do?id=${row.id}"> <i
								class="fa fa-eye w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
				</display:column>

			</display:table>
			<hr>
			<acme:backButton text="label.back" css="formButton toLeft" />
			<acme:button url="/incidence/${accesscontrol}/create.do" text="incidencia.new" />
			<br />
		</div>
	</div>
</security:authorize>