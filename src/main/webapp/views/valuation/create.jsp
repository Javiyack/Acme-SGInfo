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

<spring:message code="valuation" var="valuationText" />

<security:authorize access="isAuthenticated()">
	<security:authorize access="isAuthenticated()">
		<jstl:set var="colom" value=", " />
		<security:authentication property="principal.username" var="username" />
		<security:authentication property="principal" var="logedActor" />
		<security:authentication property="principal.authorities[0]"
			var="permiso" />
		<jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
        <jstl:if test="${rol == 'user' || rol == 'responsible'}">
            <jstl:set var="accesscontrol" value="external"/>
        </jstl:if>
        <jstl:if test="${rol == 'technician' || rol == 'manager'}">
            <jstl:set var="accesscontrol" value="internal"/>
        </jstl:if>
	</security:authorize>

	<jstl:set var="readonly"
		value="${(display || !owns || closed) && valuationForm.id != 0}" />

	<form:form action="${requestUri}" modelAttribute="valuationForm">


		<div class="seccion w3-light-grey">

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="incidence" />
			
			<div class="row">
	            <div class="col-100" style="padding-bottom: 0px!important;">
	                <a href="" class="iButton" style="padding-bottom: 0px!important;"><i class="fa fa-home font-awesome"></i></a> >
	                <a href="incidence/${accesscontrol }/list.do" class="iButton" style="padding-bottom: 0px!important;">
	                    <i class="fas fa-shield-alt fa-fw"></i></a> >
	                <a href="valuation/display.do?id=${valuationForm.incidence.id}" class="iButton"
	                   style="padding-bottom: 0px!important;">
	                    <jstl:out value="${valuationText}"/></a> >
	                <hr style="margin-top: 0.2em;">
	            </div>
	        </div>

			<div class="w3-row-padding w3-margin-top">
				<div class="w3-quarter"></div>
				<div class="w3-half">
					<acme:textbox code="valuation.value" path="value"
						readonly="${readonly}" />
					<acme:textarea code="valuation.comments" path="comments"
						css="formTextArea" readonly="${readonly}" />
				</div>
				<div class="w3-quarter"></div>

			</div>
			
			<div class="w3-row-padding">
	            
				<jstl:if test="${!readonly}">
					<acme:submit name="save" code="label.save" css="formButton toLeft" />
				</jstl:if>
            </div>			

		</div>           



	</form:form>
</security:authorize>