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

<div class="seccion w3-light-green">
	<form:form action="${requestUri}" modelAttribute="modelMessage">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="moment" />
		<form:hidden path="sender" />


		<spring:message code="ms.priority.low" var="low" />
		<spring:message code="ms.priority.neutral" var="neutral" />
		<spring:message code="ms.priority.high" var="high" />


		<div class="row">
			<div class="col-50">

				<jstl:if test="${requestUri eq 'message/edit.do'}">
					<acme:select items="${actors}" itemLabel="userAccount.username"
						code="ms.recipient" path="recipient" id="id" />
				</jstl:if>
			</div>
			<div class="col-50">
				<form:label path="priority">
					<spring:message code="ms.priority" />
				</form:label>
				<form:select path="priority" class="formInput">
					<form:option label="${low}" value="LOW" />
					<form:option label="${neutral}" value="NEUTRAL" />
					<form:option label="${high}" value="HIGH" />
				</form:select>
				<form:errors cssClass="error" path="priority" />


			</div>
		</div>
		<div class="row">
			<div class="col-100">
			<acme:textbox code="ms.subject" path="subject" />
			</div>
		</div>
		<div class="row">
			<div class="col-100">
			<acme:textarea code="ms.body" path="body" css="formTextArea" />
			</div>
		</div>
		<div class="row">
			<div class="col-25">
					<acme:submit name="save" code="ms.send" css="formButton toLeft" />&nbsp;
					<acme:cancel url="/" code="ms.cancel" css="formButton toLeft" />
				</div>
			</div>
	</form:form>
</div>