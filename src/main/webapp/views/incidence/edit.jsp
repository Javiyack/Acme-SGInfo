<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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

<security:authorize access="isAuthenticated()">
    <security:authorize access="isAuthenticated()">
        <jstl:set var="colom" value=", "/>
        <security:authentication property="principal.username" var="username"/>
        <security:authentication property="principal" var="logedActor"/>
        <security:authentication property="principal.authorities[0]"
                                 var="permiso"/>
        <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
        <jstl:if test="${rol == 'user' || rol == 'responsible'}">
            <jstl:set var="accesscontrol" value="external"/>
        </jstl:if>
        <jstl:if test="${rol == 'technician' || rol == 'manager'}">
            <jstl:set var="accesscontrol" value="internal"/>
        </jstl:if>
    </security:authorize>
    <jstl:set var="owns"
              value="${logedActor.id == incidenceForm.user.userAccount.id or logedActor.id == incidenceForm.technician.userAccount.id}"/>

    <jstl:set var="readonly"
              value="${(display || !owns || closed) && incidenceForm.id != 0}"/>
    <jstl:set var="disabled"
              value="${(incidenceForm.cancelled) && incidenceForm.id != 0}"/>

    <form:form action="${requestUri}" modelAttribute="incidenceForm">


        <div class="seccion w3-light-grey">

            <form:hidden path="id"/>
            <form:hidden path="version"/>
            <form:hidden path="customerId"/>

            <div class="w3-row-padding w3-margin-top">
                <div class="w3-third">
                    <acme:textbox code="incidencia.ticker" path="ticker"
                                  readonly="true" disabled="${disabled}"/>
                    <acme:textbox code="incidencia.name" path="title"
                                  readonly="${readonly}" disabled="${disabled}"/>
                    <acme:textbox code="incidencia.service" path="servant"
                                  readonly="${ readonly}" disabled="${disabled}"/>
                    <jstl:if test="${rol == 'user' || readonly}">

                        <form:hidden path="user"/>
                    </jstl:if>
                    <jstl:if
                            test="${rol == 'user' || rol == 'responsible' || readonly}">
                        <form:hidden path="technician"/>
                    </jstl:if>

                    <acme:select items="${users}" itemLabel="name"
                                 code="incidencia.user" path="user"
                                 readonly="${rol == 'user' ||  readonly || disabled}" css="formSelect"/>
                </div>
                <div class="w3-third">
                    <acme:moment code="incidencia.publication.moment"
                                 path="publicationDate" placeholder="dd-MM-yyyyTHH:mm"
                                 readonly="true"/>
                    <div class="w3-row">
                        <div class="w3-threequarter">
                            <acme:moment code="incidencia.starting.moment" path="startingDate"
                                         placeholder="dd-MM-yyyyTHH:mm" css="formInput"
                                         readonly="true"/>
                        </div>
                        <div class="w3-quarter" style="padding-left: 10%;margin-top: 3em;">
                            <spring:message code="incidencia.start" var="title"/>
                            <jstl:if test="${owns and rol eq 'technician'}">
                                <jstl:if test="${!readonly || disabled}">
                                    <a href="incidence/technician/start.do?id=${incidenceForm.id}">
                                        <i class="fa fa-play fw w3-xlarge w3-text-green iButton zoom" title="${title}"></i>
                                    </a>
                                </jstl:if>
                                <jstl:if test="${readonly || disabled}">
                                    <i class="fa fa-play fw w3-xlarge w3-text-grey" title="${title}"></i>
                                </jstl:if></jstl:if>
                        </div>
                    </div>
                    <div class="w3-row">
                        <div class="w3-threequarter">
                            <acme:moment code="incidencia.ending.moment" path="endingDate"
                                         placeholder="dd-MM-yyyyTHH:mm" css="formInput"
                                         readonly="true"/>
                        </div>
                        <div class="w3-quarter" style="padding-left: 10%;margin-top: 3em;">
                            <spring:message code="label.cerrar" var="title"/>
                            <jstl:if test="${owns and rol eq 'technician'}">
                                <jstl:if test="${!readonly || disabled}">
                                    <a href="incidence/technician/close.do?id=${incidenceForm.id}">
                                        <i class="fa fa-stop fw w3-xlarge w3-text-green iButton zoom"
                                           title="${title}"></i>
                                    </a>
                                </jstl:if>
                                <jstl:if test="${readonly || disabled}">
                                    <i class="fa fa-stop fw w3-xlarge w3-text-grey" title="${title}"></i>
                                </jstl:if>
                            </jstl:if>
                        </div>
                    </div>

                    <acme:select items="${technicians}" itemLabel="name"
                                 code="incidencia.technician" path="technician"
                                 readonly="${rol == 'user' or rol == 'responsible'  or rol == 'technician' or  readonly}"
                                 css="formSelect"/>
                </div>
                <div class="w3-third">
                    <acme:textarea code="incidencia.description" path="description"
                                   css="formTextArea" readonly="${readonly}" disabled="${disabled}"/>
                    <br>

                    <div onclick="toggleDisabled('reason')">
                        <acme:checkBox code="incidencia.cancelled" path="cancelled" css="w3-check"
                                       readonly="${readonly or incidenceForm.id==0}"/>
                    </div>
                    <spring:message code="placeholder.cancel.reason" var="placeholder"/>
                    <jstl:set var="ifNoCancelled" value="true"/>
                    <jstl:if test="${incidenceForm.cancelled}">
                        <jstl:set var="ifNoCancelled" value="false"/>
                    </jstl:if>
                    <acme:textarea code="empty" path="cancellationReason"
                                   css="formTextArea" disabled="${ifNoCancelled or disabled}"
                                   placeholder="${placeholder}" readonly="${ readonly}"
                                   id="reason"/>
                </div>

            </div>

        </div>
        <div class="seccion w3-light-grey">
            <div class="w3-row-padding">
                <acme:cancelButton url="/incidence/${accesscontrol}/list.do" code="label.back"
                                   css="formButton toLeft"/>


                <jstl:if test="${readonly && owns && (rol == 'manager' || rol == 'technician')}">
                    <jstl:if test="${closed}">
                        <acme:submit name="reopen" code="label.abrir"
                                     css="formButton toLeft"/>
                    </jstl:if>
                    <jstl:if test="${!closed}">
                        <acme:submit name="close" code="label.cerrar"
                                     css="formButton toLeft"/>
                    </jstl:if>
                </jstl:if>
                <jstl:if test="${!readonly}">
                    <jstl:if test="${incidenceForm.id!=0}">
                        <acme:submit name="delete" code="label.delete"
                                     css="formButton toLeft"/>
                        <jstl:if test="${closed  && (rol == 'manager' || rol == 'technician')}">
                            <acme:submit name="reopen" code="label.abrir"
                                         css="formButton toLeft"/>
                        </jstl:if>
                        <jstl:if test="${!closed && (rol == 'manager' || rol == 'technician')}">
                            <acme:submit name="close" code="label.cerrar"
                                         css="formButton toLeft"/>
                        </jstl:if>
                    </jstl:if>
                    <acme:submit name="save" code="label.save" css="formButton toLeft"/>

                </jstl:if>
                <acme:button url="/incidence/${accesscontrol}/create.do?customerId=${incidenceForm.customerId}"
                             text="label.new"/>
            </div>
        </div>
        <jstl:if test="false">
            <div class="seccion w3-red"><jstl:out value="(readonly)${readonly}"/>
                <jstl:out value="(info)${info}"/>
                <jstl:out value="(incidenceForm)${incidenceForm}"/>
                <jstl:out value="(incidenceForm.id)${incidenceForm.id}"/>
                <jstl:out value="(closed)${closed}"/>
                <jstl:out value="(accesscontrol)${accesscontrol}"/>
                <jstl:out value="(rol)${rol}"/>
                <jstl:out value="(owns)${owns}"/>
            </div>
        </jstl:if>
        <div class="titulo" style="padding-left: 0.5em; padding-top: 0px;">
            <strong><spring:message code="label.labors"/></strong>
        </div>
        <%@ include file="/views/labor/list.jsp" %>


    </form:form>
</security:authorize>