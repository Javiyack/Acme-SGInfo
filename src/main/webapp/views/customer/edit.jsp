
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
	value="${(display || !owns) && customerForm.id != 0}" />


<div class="seccion w3-light-green">
	<form:form action="${requestUri}" modelAttribute="customerForm">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="customerId" />

		<div class="row">
			<div class="col-50">
				<acme:textbox code="label.name" path="name" readonly="${readonly}" />
				<acme:textbox code="label.customer" path="customerId" readonly="${readonly}" />		
				<acme:textbox code="customer.address" path="address"
					readonly="${readonly}" />
				<acme:textbox code="customer.website" path="webSite"
					readonly="${readonly}" />
				<acme:textbox code="actor.email" path="email" readonly="${readonly}" />
				<acme:moment code="customer.registration.date" path="fechaAlta"
					placeholder="dd-MM-yyyyTHH:mm" css="formInput" readonly="true" />
				<acme:textarea code="label.description" path="description"
					css="formTextArea" readonly="${readonly}" />
				<br>
				<acme:checkBox code="label.active" path="active"
					readonly="${readonly}" />

			</div>

			<div class="col-50">

				<jstl:if test="${!readonly}">
					<acme:textbox code="label.passkey" path="passKey" readonly="true" />
					<acme:textbox code="label.nif" path="nif" readonly="${readonly}" />

					<acme:textbox code="customer.billing.address" path="billingAddress"
						readonly="${readonly}" />

				</jstl:if>
				<div class="row w3-margin-top">
					<div class="col-100 w3-padding">
						<div class="w3-card imgCentered"
							style="max-height: 22em; max-width: 20em;">
							<img src="https://www.w3schools.com/w3images/avatar2.png"
								class="imgCentered" style="max-height: 20em; max-width: 20em;">
							<div class="w3-container w3-white">
								<h4>
									<jstl:out value="${customerForm.name}"></jstl:out>
								</h4>
							</div>
						</div>
					</div>
				</div>
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