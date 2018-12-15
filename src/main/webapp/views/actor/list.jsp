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
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Date" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<security:authorize access="isAuthenticated()">
    <jstl:set var="colom" value=", "/>
    <security:authentication property="principal.username" var="username"/>
    <security:authentication property="principal" var="logedAccount"/>
    <security:authentication property="principal.authorities[0]"
                             var="permiso"/>
    <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
</security:authorize>

<div class="seccion w3-light-gray">
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
    <display:table pagesize="${pageSize}"
                   class="flat-table0 flat-table-1 w3-light-grey" name="actors"
                   requestURI="${requestUri}" id="row">
        <jstl:set var="url" value="actor/display.do?actorId=${row.id}"/>
        <jstl:set var="activeIcon" value="fa fa-square-o fw font-awesome w3-xlarge"/>
        <jstl:if test="${row.userAccount.active}">
            <jstl:set var="activeIcon" value="fa fa-check-square-o fw font-awesome w3-xlarge"/>
        </jstl:if>
        <acme:urlColumn value="${row.userAccount.username}" href="${url}" title="label.name" css="iButton"/>
        <acme:urlColumn value="${row.customer.name}" href="${url}" title="label.customer" css="iButton"/>
        <acme:urlColumn value="${row.userAccount.authorities[0]}" href="${url}" title="label.name" css="iButton"/>
        <acme:urlColumn value="${row.surname}, ${row.name}" href="${url}" title="label.surname" css="iButton"/>

        <security:authorize access="hasAnyRole('MANAGER', 'RESPONSIBLE', 'ADMINISTRATOR')">
            <acme:urlColumn value="" href="actor/${rol}/activation.do?id=${row.id}&pageSize=${pageSize}" title="label.active" css="iButton"
                        icon="${activeIcon}" style="max-widht:2em;"/>

        </security:authorize>
    </display:table>
    <hr>

    <div class="row">
        <div class="col-100">
            <security:authorize access="hasAnyRole('MANAGER', 'RESPONSIBLE', 'ADMINISTRATOR')">
                <acme:button url="actor/${rol}/deactivateAll.do?pageSize=${pageSize}"
                             text="label.activation.false.all"
                             css="formButton toLeft"/>
                <acme:button url="actor/${rol}/activateAll.do?pageSize=${pageSize}"
                             text="label.activation.true.all"
                             css="formButton toLeft"/>
            </security:authorize>
            <security:authorize access="hasAnyRole('ADMINISTRATOR')">
                <acme:button url="actor/${rol}/create.do"
                             text="label.new.administrator"
                             css="formButton toLeft"/>
            </security:authorize>

        </div>
    </div>

</div>