<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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
<div class="formPanel">
<form:form action="${requestUri}" modelAttribute="incidencia" class = "formForm">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="user" />
	<form:hidden path="technician" />
	
	<div class="floating-box">
		<div class="formElement">
		<acme:textbox code="incidencia.ticker" path="ticker" css="formInput" /><br />
		</div>
		<acme:textbox code="incidencia.name" path="title"  css="formInput"/><br />
		<acme:textbox code="incidencia.description" path="description" css="formInput" /><br />
		<acme:textbox code="incidencia.service" path="servant"  css="formInput"/><br />
		<acme:textbox code="incidencia.user" path="user.name" css="formInput" /><br />
		<acme:textbox code="incidencia.technician" path="technician.name" css="formInput" /><br />
		<acme:checkBox code="incidencia.cancelled" path="cancelled"  css="formInput"/><br />
		<acme:textbox code="incidencia.cancellation.reason" path="cancelationReason" css="formInput" /><br />
		<acme:textbox code="incidencia.publication.moment" path="publicationDate"
			placeholder="dd/MM/yyyy HH:mm"  css="formInput"/><br />
		<acme:textbox code="incidencia.starting.moment" path="startingDate"
			placeholder="dd/MM/yyyy HH:mm"  css="formInput"/><br />
		<acme:textbox code="incidencia.ending.moment" path="endingDate"
			placeholder="dd/MM/yyyy HH:mm"  css="formInput"/><br />

	</div>

	<br />
	<div class="after-box">
		<acme:cancelButton url="${backUrl}" code="rendezvous.cancel" />
		<acme:submit name="save" code="rendezvous.save" />
		<jstl:if test="${rendezvous.id!=0 }">
			<acme:submit name="delete" code="rendezvous.delete" />
		</jstl:if>
	</div>
</form:form>
</div>