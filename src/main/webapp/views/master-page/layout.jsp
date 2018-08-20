<%--
 * layout.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="shortcut icon" href="favicon.ico" />
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="scripts/cookiePopups.js"></script>
<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf8" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
<script type="text/javascript" src="scripts/w3.js"></script>
<script type="text/javascript" src="scripts/acme.js"></script>
  

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-black.css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="styles/common.css" type="text/css">
<link rel="stylesheet" href="styles/displaytag.css" type="text/css">
<link rel="stylesheet" href="styles/cookie.css" type="text/css">
<link rel="stylesheet" href="styles/topnav.css" type="text/css">
<link rel="stylesheet" href="styles/acme.css" type="text/css">

<title><tiles:insertAttribute name="title" ignore="true" /></title>

<body class="w3-white">
	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="menu" />

	<!-- !PAGE CONTENT! -->
	<div class="w3-main" style="margin-left: 300px; margin-top: 46px; padding: 0em 10% 0em 10%;">
		<!-- Mensajes de error -->
		<jstl:if test="${message != null}">
				<br />
				<span class="message"><spring:message code="${message}" /></span>
			</jstl:if>
		
		<!-- Titulo -->
		<div class="titulo">		
			<strong><tiles:insertAttribute name="title" />	</strong>	
		</div>
	<div class="w3-container w3-padding-bottom w3-margin-bottom">
		<tiles:insertAttribute name="body" />
  <br>
	</div>
	</div>
	<div class="footer"
		style="background: black; color: white; opacity: .9;">
		<tiles:insertAttribute name="footer" />
	</div>
</body>



</html>