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
<div class="seccion w3-light-green">
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


			<display:table pagesize="${pageSize}"
				class="displaytag w3-text-black" name="labors"
				requestURI="${requestUri}" id="row">


				<spring:message code="label.name" var="title" />
				<display:column property="title" title="${title}" sortable="true"
					class="${classTd}" />
				<acme:column property="description" title="label.description" />
				<acme:column property="moment" title="label.moment" />
				<acme:column property="time" title="label.time" />
				<display:column class="${classTd}">
					<jstl:set var="owns"
						value="${rol=='manager' or logedActor.id==row.incidence.technician.userAccount.id}" />

					<jstl:if test="${owns && !closed}">
						<div>
							<a href="labor/edit.do?id=${row.id}"> <i
								class="fa fa-edit w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
					<jstl:if test="${!owns || closed}">
						<div>
							<a href="labor/display.do?id=${row.id}"> <i
								class="fa fa-eye w3-xlarge"></i>
							</a>
						</div>
					</jstl:if>
				</display:column>
				
				<display:column class="${classTd}">
					<jstl:if test="${owns && !closed}">
						<div>
							<a href="labor/${rol}/delete.do?id=${row.id}&incidenceId=${row.incidence.id}"> <i
								class="fa fa-remove w3-xlarge"></i>
							</a>
						</div>

					</jstl:if>
					<jstl:if test="${!owns}">
						<div>
							<p>
							</p>
						</div>
					</jstl:if>
				</display:column>


			</display:table>
			<hr>
			<jstl:if test="${incidenceForm==null}">
				<acme:backButton text="label.back" css="formButton toLeft" />
			</jstl:if>
			<jstl:if test="${incidenceForm!=null && !closed}">
				<spring:message var="msg" code="msg.save.incidence.first" />
				<jstl:set var="url"
					value="/labor/create.do?incidenceId=${incidenceForm.id}"></jstl:set>
				<p>
					<i class="fa fa-plus-square w3-xlarge"
						onclick="javascript: showConditionalAlert('${msg}','${incidenceForm.id}','${url}');"></i>
				</p>

			</jstl:if>
			<br />
		</div>
	</div>
</div>