<%@page import="org.springframework.test.web.ModelAndViewAssert" %>
<%@page import="org.springframework.web.servlet.ModelAndView" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${readonly==null}">
    <jstl:set var="readonly" value="false"/>
</jstl:if>
<jstl:if test="${servantForm.draft==false}">
    <jstl:out value="draft = false; "/>
    <jstl:set var="readonly" value="true"/>
</jstl:if>
re: <jstl:out value="${readonly}"/>
<div class="seccion w3-light-gray">
    <form:form action="${requestUri}" modelAttribute="servantForm">
        <form:hidden path="id"/>
        <form:hidden path="version"/>
        <div class="row">
            <div class="col-50">
                <acme:textbox code="servant.name" path="name" readonly="${readonly}"/>
                <acme:textbox code="servant.description" path="description" readonly="${readonly}"/>
                <div class="row">
                    <div class="col-12-5">
                        <spring:message code="placeholder.money" var="placeholder"/>
                        <acme:textbox code="label.price" path="price"
                                      pattern="\\d{1,7}.{0,1}\\d{0,2}"
                                      placeholder="0.00"
                                      title="${placeholder}" icon="fa fa-eur" readonly="${readonly}"/>
                    </div>
                    <security:authorize access="hasRole('MANAGER')">
                        <div class="col-25 w3-margin-top">
                            <br>
                            <acme:checkBox code="label.draft" path="draft" css="w3-check w3-text-black" readonly="${readonly}"/>
                        </div>
                        <div class="col-25 w3-margin-top">
                            <br>
                            <acme:checkBox code="label.cancel" path="cancelled" css="w3-check w3-text-black"/>
                        </div>

                    </security:authorize>
                </div>
            </div>
            <div class="col-50">
                <acme:textbox code="servant.picture" path="picture" readonly="${readonly}"/>

            </div>
        </div>
        <hr>
        <div class="row">
            <security:authorize access="hasRole('RESPONSIBLE')">
                <div class="col-25">
                        <spring:message code="label.request" var="title"/>
                        <acme:button text="label.request" url="request/responsible/create.do?serviceId=${servantForm.id}"
                                     icon="fa fa fa-shopping-cart font-awesome" title="${title}"/>
                </div>
            </security:authorize>
            <security:authorize access="hasRole('MANAGER')">
                <div class="col-25">
                    <acme:submit name="save" code="servant.save"/>
                </div>
            </security:authorize>
        </div>

    </form:form>

</div>

