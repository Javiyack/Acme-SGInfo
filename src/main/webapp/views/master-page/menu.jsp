<%--
 * header.jsp
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
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>

<security:authorize access="isAuthenticated()">
	<jstl:set var="colom" value=", " />
	<security:authentication property="principal.username" var="username" />
	<security:authentication property="principal.authorities[0]" var="permiso" />
	<jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
</security:authorize>


<jstl:if test="${rol == 'user' or rol == 'responsable'}" >
		<jstl:set value="external" var="rol"/>
</jstl:if>
<jstl:if test="${rol == 'technician' or rol == 'manager'}" >
		<jstl:set value="internal" var="rol"/>
</jstl:if>

<!-- Menu and banner usually + "$") -->
<!-- Sidebar/menu -->

  <nav class="w3-sidebar w3-collapse w3-red w3-animate-left" style="z-index:3;width:300px;" id="mySidebar">
  <div class="w3-bar-block">
    <a href="#" class="w3-bar-item w3-button w3-hide-large w3-dark-grey w3-hover-black" onclick="w3_close()" title="close menu">
    <i class="fa fa-arrow-left fa-fw"></i></a>
  </div>
  <br>
  
  <div class="w3-container w3-row">
    <div class="w3-col s4">
      <security:authorize access="isAuthenticated()">
      	<img src="https://www.w3schools.com/w3images/avatar2.png" class="w3-circle w3-margin-right" style="width:46px">
      </security:authorize>	
      <security:authorize access="isAnonymous()">
      	<a href="security/login.do" class=""><i class="fa fa-sign-in w3-margin w3-xxlarge"></i></a>
      </security:authorize>
    </div>
    <div class="w3-col s8 w3-bar">
      <span><spring:message code="welcome.greeting.msg"/>${colom}<strong>${username}</strong></span><br>
       <security:authorize access="isAnonymous()">
      	<a href="actor/create.do" class=""><i class="fa fa-user-plus w3-bar-item w3-large"></i></a>
      </security:authorize><a href="#"><i class="fa fa-envelope w3-bar-item w3-large"></i></a>
      <security:authorize access="isAuthenticated()">
      <a href="actor/edit.do"><i class="fa fa-user w3-bar-item w3-large"></i></a>
      </security:authorize>
      <a href="#"><i class="fa fa-cog w3-bar-item w3-large"></i></a>
    </div>
  </div>  
  <hr>
  <div class="w3-bar-block" style="padding-bottom:60px">
    
    <security:authorize access="isAuthenticated()">
    <a href="incidence/${rol}/list.do" class="w3-bar-item w3-button w3-padding w3-xlarge">
   		<i class="fa fa-ambulance fa-fw"></i>  <spring:message code="label.incidences" /> </a>
   </security:authorize>
    <a href="customer/list.do" class="w3-bar-item w3-button w3-padding w3-xlarge">
    	<i class="fa fa-diamond fa-fw"></i>  <spring:message code="label.customers" /> </a>
   <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-eye fa-fw"></i>  Servicios</a>
    <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-users fa-fw"></i>  Facturas</a>
    <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-bullseye fa-fw"></i>  Equipos</a>
    <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-bell fa-fw"></i>  News</a>
    <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-bank fa-fw"></i>  General</a>
    <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-history fa-fw"></i>  History</a>
    <a href="#" class="w3-bar-item w3-button w3-padding w3-xlarge">
    <i class="fa fa-cog fa-fw"></i>  Settings</a><br><br>
  </div>
  
</nav>


<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<script>
var btnContainer = document.getElementById("myDIV");

//Get all buttons with class="btn" inside the container
var btns = btnContainer.getElementsByClassName("btn");

//Loop through the buttons and add the active class to the current/clicked button
for (var i = 0; i < btns.length; i++) {
btns[i].addEventListener("click", function() {
 var current = document.getElementsByClassName("active");
 current[0].className = current[0].className.replace(" active", "");
 this.className += " active";
});
}
</script>