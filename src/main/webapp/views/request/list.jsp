<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 --%>
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
    <jstl:set var="colom" value=", "/>
    <security:authentication property="principal.username" var="username"/>
    <security:authentication property="principal" var="logedActor"/>
    <security:authentication property="principal.authorities[0]"
                             var="permiso"/>
    <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
</security:authorize>
<div class="seccion w3-light-grey">
    <legend>
        <spring:message code="label.requests"/>
    </legend>

    <jstl:if test="${pageSize == null}">
        <jstl:set value="20" var="pageSize"/>
    </jstl:if>
    <jstl:if test="${!chirps.isEmpty()}">

        <form:form action="${requestUri}" method="GET">
            <spring:message code="pagination.size"/>
            <input hidden="true" name="word" value="${word}">
            <input hidden="true" name="topic" value="${topic}">
            <input type="number" name="pageSize" min="1" max="100"
                   value="${pageSize}">
            <input type="submit" value=">">
        </form:form>
    </jstl:if>
    <div style="overflow-x:auto;">

        <display:table pagesize="${pageSize}"
                       class="flat-table0 flat-table-1 w3-light-grey" name="requests"
                       requestURI="${requestUri}" id="row">

            <jstl:set var="url" value="servant/display.do?id=${row.servant.id}"/>
            <jstl:set var="icono" value="fa fa-eye w3-xlarge"/>
            <jstl:if test="${logedActor eq row.responsible.userAccount}">
                <jstl:set var="url" value="request/responsible/edit.do?requestId=${row.id}"/>
            </jstl:if>
            <security:authorize access="hasRole('MANAGER')">
                <jstl:set var="url" value="request/manager/edit.do?requestId=${row.id}"/>
                <jstl:set var="icono" value="fa fa-edit w3-xlarge"/>

            </security:authorize>

            <spring:message code="moment.pattern" var="intercionalizedPattern"/>
            <fmt:formatDate value="${row.creationMoment}" pattern="${intercionalizedPattern}" var="creationMoment"/>
            <spring:message code="date.pattern" var="intercionalizedDatePattern"/>
            <fmt:formatDate value="${row.startingDay}" pattern="${intercionalizedDatePattern}" var="startingDay"/>
            <fmt:formatDate value="${row.endingDay}" pattern="${intercionalizedDatePattern}" var="endingDay"/>
            <acme:urlColumn value="${row.responsible.name}" title="actor.authority.RESPONSIBLE" href="${url}" css="iButton"/>
            <acme:urlColumn value="${creationMoment}" title="label.moment" href="${url}" css="iButton"/>
            <acme:urlColumn value="${startingDay}" title="label.starting.on" href="${url}" css="iButton"/>
            <acme:urlColumn value="${endingDay}" title="label.ending.on" href="${url}" css="iButton"/>
            <acme:urlColumn value="${row.servant.name}" title="label.service" href="${url}" css="iButton" sortable="true"/>
            <acme:urlColumn value="${row.status}" title="label.status" sortable="true" href="${url}" css="iButton"/>
            <acme:urlColumn value="" title="label.none" href="${url}" css="iButton" icon="${icono}"/>


        </display:table>
    </div>


    <br/>
</div>

