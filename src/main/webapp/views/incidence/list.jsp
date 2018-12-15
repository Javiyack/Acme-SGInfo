<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.apache.commons.lang.time.DateUtils" %>
<%@page import="org.hibernate.engine.spi.RowSelection" %>
<%@ page import="java.util.Date" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<security:authorize access="isAuthenticated()">
    <jsp:useBean id="date" class="java.util.Date"/>

    <security:authorize access="isAuthenticated()">
        <jstl:set var="colom" value=", "/>
        <security:authentication property="principal.username" var="username"/>
        <security:authentication property="principal" var="logedActor"/>
        <security:authentication property="principal.authorities[0]"
                                 var="permiso"/>
        <jstl:set var="rol" value="${fn:toLowerCase(permiso)}/"/>
    </security:authorize>
    <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
    <jstl:if test="${rol == 'user' or rol == 'responsible'}">
        <jstl:set value="external" var="accesscontrol"/>
    </jstl:if>
    <jstl:if
            test="${rol == 'technician' or rol == 'manager' or rol == 'administrator'}">
        <jstl:set value="internal" var="accesscontrol"/>
    </jstl:if>
    <div class="seccion w3-light-grey">
        <div class="w3-row-padding w3-margin-top">

            <jstl:if test="${pageSize == null}">
                <jstl:set value="20" var="pageSize"/>
            </jstl:if>
            <form:form action="${requestUri}" method="GET">
                <spring:message code="pagination.size"/>
                <input hidden="true" name="word" value="${word}">
                <input type="number" name="pageSize" min="1" max="100"
                       value="${pageSize}">
                <input type="submit" value=">">
            </form:form>


            <div style="overflow-x:auto;">
                <display:table pagesize="${pageSize}" class="flat-table0 flat-table-1 w3-light-grey"
                               name="incidences" requestURI="${requestUri}" id="row">
                    <jstl:set var="owns"
                              value="${logedActor.id == row.user.userAccount.id or logedActor.id == row.technician.userAccount.id}"/>

                    <jstl:if test="${owns}">
                        <jstl:set var="url" value="incidence/${accesscontrol}/edit.do?id=${row.id}"/>
                    </jstl:if>
                    <jstl:if test="${!owns}">
                        <jstl:set var="url" value="incidence/${accesscontrol}/display.do?id=${row.id}"/>
                    </jstl:if>
                    
                    <jstl:set var="resultIncidence" value="${row.endingDate<date}"/>
                    <jstl:if test="${noVoted.contains(row) and logedActor.id == row.user.userAccount.id}">
                    	<jstl:set var="rate" value="fa fa-star w3-xlarge"/>
                    	<jstl:set var="urlRate" value="valuation/create.do?id=${row.id}"/>
                    </jstl:if>
                    <jstl:if test="${noVoted.contains(row) and logedActor.id != row.user.userAccount.id}">
                    	<jstl:set var="rate" value=""/>
                    	<jstl:if test="${owns}">
	                        <jstl:set var="urlRate" value="incidence/${accesscontrol}/edit.do?id=${row.id}"/>
	                    </jstl:if>
	                    <jstl:if test="${!owns}">
	                        <jstl:set var="urlRate" value="incidence/${accesscontrol}/display.do?id=${row.id}"/>
	                    </jstl:if>
                    </jstl:if>
                    <jstl:if test="${!noVoted.contains(row)}">
                    	<jstl:set var="rate" value="fa fa-eye w3-xlarge"/>
                    	<jstl:set var="urlRate" value="valuation/display.do?id=${row.id}"/>
                    </jstl:if>
                    
                    <jstl:set var="rowcolor" value="background-color: rgba(255,0,0,0.6); color: white;"/>
                    <jstl:if test="${row.startingDate < date}">
                        <jstl:set var="rowcolor" value="background-color: rgba(255,180,0,0.5); color: red;"/>
                    </jstl:if>
                    <jstl:if test="${row.endingDate < date}">
                        <jstl:set var="rowcolor" value="background-color: rgba(0,255,0,0.2); color: green;"/>
                    </jstl:if>
                    <jstl:if test="${row.cancelled == true}">
                        <jstl:set var="rowcolor" value="background-color: rgba(0,0,0,0.1); color: grey;"/>
                    </jstl:if>
                    <acme:urlColumn value="${row.ticker}" title="incidencia.ticker" href="${url}"
                                    css="iButton" style="${rowcolor}"/>

                    <acme:urlColumn value="${row.user.customer.name}" title="label.customer" href="${url}"

                                    css="iButton" style="${rowcolor}"/>
                    <acme:urlColumn value="${row.user.userAccount.username}" title="incidencia.user"
                                    href="actor/display.do?actorId=${row.user.id}"
                                    css="" style="${rowcolor}"/>
                    <acme:urlColumn value="${row.technician.userAccount.username}" title="incidencia.technician"
                                    href="actor/display.do?actorId=${row.technician.id}"
                                    style="${rowcolor}" css="iLink" />
                    <acme:urlColumn value="${row.title}" title="incidencia.name"
                                    href="actor/display.do?actorId=${row.technician.id}"
                                    style="${rowcolor}" css="iLink"/>
                    <spring:message code="moment.pattern" var="momentPattern"/>
                    <fmt:formatDate value="${row.publicationDate}" pattern="${momentPattern}" var="creationMoment"/>
                    <acme:urlColumn value="${creationMoment}." title="label.moment" href="${url}" css="iButton"
                                    style="${rowcolor}"/>
                    <fmt:formatDate value="${row.endingDate}" pattern="${momentPattern}" var="endingDate"/>
                    <acme:urlColumn value="${endingDate}." title="label.ending.on" href="${url}" css="iButton" icon=""
                                    style="${rowcolor}" />

                    <jstl:set var="owns" value="${logedActor.id == row.user.userAccount.id or logedActor.id == row.technician.userAccount.id}"/>
                    <jstl:if test="${owns}">
                        <jstl:set var="icono" value="fa fa-edit w3-xlarge"/>
                    </jstl:if>
                    <jstl:if test="${!owns}">
                        <jstl:set var="icono" value="fa fa-eye w3-xlarge"/>
                    </jstl:if>
                    <acme:urlColumn value="" title="label.sort" href="${url}"
                                    css="iButton" style="${rowcolor}" icon="${icono}"/>
                                    
                    
                    
                    <jstl:if test="${resultIncidence}">                    	
                    	<acme:urlColumn value="" title="label.rate" href="${urlRate}"
                                    css="iButton" style="${rowcolor}" icon="${rate}"/>
                    </jstl:if>
                    
                </display:table>
            </div>
            <hr>
            <acme:backButton text="label.back" css="formButton toLeft"/>
            <acme:button url="/incidence/${accesscontrol}/create.do" text="incidencia.new"/>
            <br/>
        </div>
    </div>
</security:authorize>