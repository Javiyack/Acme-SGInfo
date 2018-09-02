<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form requestUri="configuration/administrator/edit.do" modelAttribute="configuration">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="configuration.companyName" path="companyName" />
	<br />
	
	<acme:textbox code="configuration.companyName" path="passKey" />
	<br />
	
	<acme:textbox code="configuration.banner" path="logo" />
	<br />

	<acme:textbox code="configuration.benefitsPercentage" path="iva" />
	<br />
	<acme:textbox code="configuration.benefitsPercentage" path="hourPrice" />
	<br />
	<acme:textbox code="configuration.benefitsPercentage" path="iva" />
	<br />
	<acme:textbox code="configuration.benefitsPercentage" path="iva" />
	<br />
	
	<acme:textarea code="configuration.welcomeMessageEs" path="welcomeMessageEs" />
	<acme:textarea code="configuration.welcomeMessageEn" path="welcomeMessageEn" />
	<br />  
	
	<acme:submit code="configuration.save" name="save" css="formButton toLeft" />
                
</form:form>