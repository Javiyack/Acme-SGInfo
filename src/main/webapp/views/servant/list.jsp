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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<div class="seccion w3-light-gray">
    <display:table pagesize="${pageSize}"
                   class="flat-table0 flat-table-1 w3-light-grey" name="servants"
                   requestURI="${requestUri}" id="row">
            <jstl:set var="url" value="servant/display.do?id=${row.id}"/>
        <security:authorize access="hasRole('MANAGER')">
            <jstl:set var="url" value="servant/manager/edit.do?id=${row.id}"/>
        </security:authorize>
            <acme:imgColumn src="${row.picture}" href="${url}" title="label.picture" css="iButton tableImg" style="width:9em;"/>
            <acme:urlColumn value="${row.name}" href="${url}" title="label.name" css="iButton"/>
            <acme:urlColumn value="${row.description}" href="${url}" title="label.description" css="iButton" style="width:60%;" />
            <acme:urlColumn value="${row.price}" href="${url}" title="label.price" css="iButton" style="max-widht:4em;"/>
        </display:table>
        <br>
    <spring:message code="label.new" var="newTitle"/>
    <spring:message code="label.service" var="sernvantTitle"/>
    <security:authorize access="hasRole('MANAGER')">
        <div>
            <i class="fa fa-plus-square w3-xxlarge w3-text-dark-grey w3-hover-text-orange iButton w3-padding w3-margin-right"
               onclick="relativeRedir('servant/manager/create.do');" title="${newTitle} ${sernvantTitle}"></i>
            <jstl:if test="${requestableOnly==null}">
                <a href="servant/list.do" >
                    <i class="fa fa fa-filter w3-xxlarge css-unchecked iButton w3-padding w3-margin-right"
                       title="<spring:message code="label.available"/>">
                    </i></a>
            </jstl:if>
            <jstl:if test="${requestableOnly}">
                <a href="servant/manager/list.do" >
                    <i class="fa fa fa-filter w3-xxlarge css-checked iButton w3-padding w3-margin-right"
                       title="<spring:message code="label.show.all"/>"></i>
                </a>

            </jstl:if>
            </security:authorize>
        </div>

</div>