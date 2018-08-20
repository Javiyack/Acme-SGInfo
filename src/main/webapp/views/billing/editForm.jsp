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



	<form:form action="rendezvous/user/editForm.do"
		modelAttribute="rendezvousEditForm">

		<form:hidden path="id" />

		<div class="floating-box">
			<IMG src="${rendezvousEditForm.picture}" class="rendezvousImg" />


		</div>
		<div class="floating-box">
			<div>
				<iframe class="mapBox"
					src="https://www.google.com/maps/embed/v1/search?q=${rendezvousEditForm.location.latitude},${rendezvousEditForm.location.longitude}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c">
				</iframe>
			</div>

		</div>

		<div class="after-box">
			<table class="formTable">
				<tbody>
					<acme:textboxOnTable code="rendezvous.name" path="name" />
					<acme:textareaOnTable code="rendezvous.description"
						path="description" />
					<acme:textboxOnTable code="rendezvous.moment" path="moment"
						placeholder="dd/MM/yyyy HH:mm" />

					<spring:message code="location.latitude.placeholder"
						var="latitudePlaceholder" />
					<acme:textboxOnTable code="rendezvous.location.latitude"
						path="location.latitude" placeholder="${latitudePlaceholder}"
						pattern="^(\+|-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\.[0-9]{1,6})?))$" />
					<spring:message code="location.longitude.placeholder"
						var="logitudePlaceholder" />
					<acme:textboxOnTable code="rendezvous.location.longitude"
						path="location.longitude" placeholder="${logitudePlaceholder}"
						pattern="^(\+|-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\.[0-9]{1,6})?))$" />

					<acme:textboxOnTable code="rendezvous.picture" path="picture"
						placeholder="Url..." />
					<tr>
						<td><acme:checkBox code="rendezvous.draft" path="draft" /> <acme:checkBox
								code="rendezvous.adult" path="adult" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		<br />
		<div class="after-box">
			<acme:cancel url="/" code="rendezvous.cancel" />
			<acme:submit name="save" code="rendezvous.save" />

			<jstl:if test="${rendezvousEditForm.id!=0 }">
				<acme:submit name="delete" code="rendezvous.delete" />
			</jstl:if>

		</div>
	</form:form>
