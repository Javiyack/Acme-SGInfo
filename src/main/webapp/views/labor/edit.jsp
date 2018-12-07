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
	<jstl:set var="colom" value=", " />
	<security:authentication property="principal.username" var="username" />
	<security:authentication property="principal" var="logedAccount" />
	<security:authentication property="principal.authorities[0]"
		var="permiso" />
	<jstl:set var="rol" value="${fn:toLowerCase(permiso)}" />
</security:authorize>

<jstl:set var="readonly"
	value="${(display || !owns) && laborForm.id != 0}" />


<div class="seccion w3-light-grey">
	<form:form action="${requestUri}" modelAttribute="laborForm">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="incidence" />

		<div class="row">
			<div class="col-50">
				
				<acme:textbox code="label.incidence" path="incidence.title"
					readonly="${true}" />
				<acme:textbox code="label.labor" path="title"
					readonly="${readonly}" />
				<acme:time code="label.time" path="time"
					readonly="${readonly}" />
				<acme:moment code="label.moment" path="moment" readonly="${readonly}" 
					placeholder="dd-MM-yyyyTHH:mm" css="formInput" />
				<acme:textarea code="label.description" path="description"
					css="formTextArea" readonly="${readonly}" />
				<br>

			</div>

			
		</div>
		<div class="row">
			<div class="col-50"></div>

		</div>
		<div class="row">
			<div class="col-100">
				<jstl:if test="${!readonly}">
					<hr>
					<acme:submit name="save" code="rendezvous.save"
						css="formButton toLeft" />
				</jstl:if>
			</div>

		</div>
	</form:form>

</div>