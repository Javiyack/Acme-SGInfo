<%@page import="org.springframework.context.annotation.Import" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMINISTRATOR')">
    <spring:message code="msg.delete.confirmation" var="deleteConfirmation"/>
    <div class="seccion w3-light-grey">
    <legend>
        <spring:message code="tabooWord.seek.tooltip"/>
    </legend>
    <ul class="w3-ul">

    <jstl:forEach items="${messages}" var="row">
        <jstl:set var="deleteUrl" value="message/administrator/delete.do?messageId=${row.id}"/>
        <li class="w3-display-container w3-hover-white">
            <div class="col">
                <div class="row">
                    <div class="col-25">
                    <jstl:out value="${row.sender.surname}, ${row.sender.name} "/>
                </div>
                    <div class="col-25">
                        <jstl:out value="${row.moment}"/>
                    </div>
                    <div class="col-25">
                        <jstl:out value="${row.subject}"/>
                    </div>

                </div>


            </div>
        </div>
    </jstl:forEach>
    </ul>
    <hr>
    <acme:button url="tabooWord/administrator/seek.do"
                 text="label.back" css="formButton toLeft"/>
    <spring:message code="tabooWord.seek.tooltip" var="tooltip"/>
    <acme:button url="tabooWord/administrator/seek.do"
                 text="tabooWord.seek" css="formButton toLeft" title="${tooltip}"/>

    <br/>
    </div>
</security:authorize>