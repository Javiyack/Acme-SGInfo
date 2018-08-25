<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="isAuthenticated()">
	<security:authorize access="isAuthenticated()">
		<jstl:set var="colom" value=", " />
		<security:authentication property="principal.username" var="username" />
		<security:authentication property="principal" var="logedActor" />
		<security:authentication property="principal.authorities[0]"
			var="permiso" />
		<jstl:set var="rol" value="${fn:toLowerCase(permiso)}" />
	</security:authorize>
	<jstl:set var="owns"
		value="${logedActor.id == incidenceForm.user.userAccount.id or logedActor.id == incidenceForm.technician.userAccount.id}" />

	<jstl:set var="readonly"
		value="${(display || !owns) && incidenceForm.id != 0}" />


	<form:form action="${requestUri}" modelAttribute="incidenceForm">


		<div class="seccion w3-light-green">

			<form:hidden path="id" />
			<form:hidden path="version" />

			<div class="w3-row-padding w3-margin-top">
				<div class="w3-third">
					<acme:textbox code="incidencia.ticker" path="ticker"
						readonly="true" />
					<acme:textbox code="incidencia.name" path="title"
						readonly="${readonly}" />
					<acme:textbox code="incidencia.service" path="servant"
						readonly="${readonly}" />
					<jstl:if test="${rol == 'user' || readonly}">

						<form:hidden path="user" />
					</jstl:if>
					<jstl:if
						test="${rol == 'user' || rol == 'responsable' || readonly}">
						<form:hidden path="technician" />
					</jstl:if>

					<acme:select items="${users}" itemLabel="name"
						code="incidencia.user" path="user"
						readonly="${rol == 'user' || readonly}" css="formSelect" />
				</div>
				<div class="w3-third">
					<acme:moment code="incidencia.publication.moment"
						path="publicationDate" placeholder="dd-MM-yyyyTHH:mm"
						readonly="true" />
					<acme:moment code="incidencia.starting.moment" path="startingDate"
						placeholder="dd-MM-yyyyTHH:mm" css="formInput"
						readonly="${rol == 'user' || readonly}" />
					<acme:moment code="incidencia.ending.moment" path="endingDate"
						placeholder="dd-MM-yyyyTHH:mm" css="formInput"
						readonly="${rol == 'user' || readonly}" />
					<acme:select items="${technicians}" itemLabel="name"
						code="incidencia.technician" path="technician"
						readonly="${rol == 'user' || rol == 'responsable' || readonly}"
						css="formSelect" />
				</div>
				<div class="w3-third">
					<acme:textarea code="incidencia.description" path="description"
						css="formTextArea" readonly="${readonly}" />
					<br>

					<acme:checkBox code="incidencia.cancelled" path="cancelled" css=""
						readonly="${readonly}" />
					<acme:textarea code="empty" path="cancelationReason"
						css="formTextArea" placeholder="Cancelation reason..."
						readonly="${readonly}" />
				</div>

			</div>

		</div>
		<div class="seccion w3-light-green">
			<div class="w3-row-padding w3-margin-top">
				<acme:cancelButton url="/incidence/${rol}/list.do"
					code="rendezvous.cancel" css="formButton toLeft" />
				<jstl:if test="${!readonly}">
					<jstl:if test="${incidence.id!=0 }">
						<acme:submit name="delete" code="label.delete"
							css="formButton toLeft" />
					</jstl:if>
					<acme:submit name="save" code="rendezvous.save"
						css="formButton toLeft" />
				</jstl:if>
			</div>
		</div>
		<div class="titulo" style="padding-left: 0.5em; padding-top: 0px;">
			<strong><spring:message code="label.labors" /></strong>
		</div>
		<%@ include file="/views/labor/list.jsp"%>
		<div class="titulo" style="padding-left: 0.5em; padding-top: 0px;">
			<strong><spring:message code="file.files" /></strong>
		</div>
		<%@ include file="/views/attachment/list.jsp"%>


	</form:form>
</security:authorize>