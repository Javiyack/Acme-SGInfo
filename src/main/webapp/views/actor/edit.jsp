
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:out value="${message}"></jstl:out>
<div class="form">

	<form:form action="${requestUri}" modelAttribute="actorForm">
		<form:hidden path="id" />
		<br>

		<div class="seccion w3-light-grey">
			<legend>
				<spring:message code="actor.personal.data" />
			</legend>

			<div class="row">
				<div class="col-50">
					<acme:textbox code="actor.name" path="name" readonly="${!edition}" />
				</div>
				<div class="col-50">
					<acme:textbox code="actor.surname" path="surname"
						readonly="${!edition}" />
				</div>
			</div>

			<div class="row">
				<div class="col-50">
					<acme:textbox code="actor.email" path="email"
						readonly="${!edition}" />
				</div>
				<div class="col-50">
					<acme:textbox code="actor.phone" path="phone"
						readonly="${!edition}" />
				</div>
			</div>

			<div class="row">
				<div class="col-100">
					<acme:textbox code="actor.address" path="address"
						readonly="${!edition}" />
				</div>
			</div>

			<jstl:if test="${!edition}">
				<acme:backButton text="actor.back" css="formButton toLeft" />
			</jstl:if>
		</div>
		<jstl:if test="${edition}">
			<div class="seccion w3-light-grey">


				<jstl:if test="${creation}">
					<legend>
						<spring:message code="label.userAccount" />
					</legend>
					<div class="row">
						<div class="col-50">
							<acme:textbox code="actor.username" path="username" />
							<acme:password code="label.userAccount.password"
								path="password" id="password"
								onkeyup="javascript: checkPassword();" />
							<acme:password code="label.userAccount.repeatPassword"
								path="confirmPassword" id="confirm_password"
								onkeyup="javascript: checkPassword();" />
						</div>
						<div class="col-50">
							<acme:select items="${customers}" code="label.customer"
								itemLabel="name" path="customer" css="formSelect"
								addEmpty="true" />
							<acme:textbox code="label.passkey" path="passKey" />
							<form:label path="${path}">
								<spring:message code="actor.authority.selection" />
							</form:label>
							<select id="authority" name="authority">
								<jstl:forEach items="${permisos}" var="permiso">
									<option value="${permiso}">
										<spring:message code="actor.authority.${permiso}" />
									</option>

								</jstl:forEach>
							</select>
						</div>
					</div>
				</jstl:if>
				<jstl:if test="${!creation}">
					<legend>
						<spring:message code="label.userAccount" var="label" />
						<input type="button" value="${label}"
							onclick="javascript: showUserAccount();" class="acordeon">
					</legend>

					<div id="changePassword" style="display: none;">
						<br>
						<div class="row">
							<div class="col-50">								
								<acme:textbox code="label.customer" path="customer.name" css="formInput" readonly="true"/>
							</div>
							<div class="col-50">
								<acme:textbox code="actor.authority" path="authority" css="formInput" readonly="true"/>
							
							</div>
						</div>
						<div class="row">
							<div class="col-50">
								<acme:textbox code="actor.username" path="username"
									css="formInput" />
								<br />
								<acme:password code="label.userAccount.oldPassword"
									path="password" css="formInput" id="password"
									onkeyup="javascript: checkEdition();" />
								<br />
							</div>
							<div class="col-50">
								<acme:password code="label.userAccount.newPassword"
									path="newPassword" css="formInput" id="new_password"
									onkeyup="javascript: checkEdition();" />
								<br />
								<acme:password code="label.userAccount.repeatPassword"
									path="confirmPassword" id="confirm_password" css="formInput"
									onkeyup="javascript: checkEdition();" />
							</div>
						</div>
					</div>

				</jstl:if>
			</div>
			<div class="seccion w3-light-grey">
				<security:authorize access="isAnonymous()">
					<p class="terminos">
						<spring:message code="term.registration" />
					</p>
				</security:authorize>
				<div class="row">
					<div class="col-50">
						<input type="submit" name="save" id="save"
							value='<spring:message code="actor.save"/>'
							class="formButton toLeft" />&nbsp; <input type="button"
							name="cancel" value='<spring:message code="actor.cancel" />'
							onclick="javascript: relativeRedir('/');"
							class="formButton toLeft" />
					</div>
				</div>
			</div>
		</jstl:if>

	</form:form>

</div>