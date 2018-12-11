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
    <jstl:set var="colom" value=", "/>
    <security:authentication property="principal.username" var="username"/>
    <security:authentication property="principal" var="logedAccount"/>
    <security:authentication property="principal.authorities[0]"
                             var="permiso"/>
    <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
</security:authorize>

<jstl:set var="readonly"
          value="${(display || !owns) && customerForm.id != 0}"/>


<div class="seccion w3-light-grey">
    <form:form action="${requestUri}" modelAttribute="customerForm">

        <form:hidden path="id"/>
        <form:hidden path="version"/>

        <div class="row">
            <div class="col-50">
                <acme:textbox code="label.name" path="name" readonly="${readonly}" disabled="${!customerForm.active}"/>
                <acme:textbox code="customer.address" path="address"
                              readonly="${readonly}" disabled="${!customerForm.active}"/>
                <acme:textbox code="customer.website" path="webSite"
                              readonly="${readonly}" disabled="${!customerForm.active}"/>
                <acme:textbox code="actor.email" path="email" readonly="${readonly}"
                              disabled="${!customerForm.active}"/>
                <acme:moment code="customer.registration.date" path="fechaAlta"
                             placeholder="dd-MM-yyyyTHH:mm" css="formInput" readonly="true"
                             disabled="${!customerForm.active}"/>
                <acme:textarea code="label.description" path="description"
                               css="formTextArea" readonly="${readonly}" disabled="${!customerForm.active}"/>
                <br>
                <acme:checkBox code="label.active" path="active"
                               readonly="true" css="w3-check"/>

            </div>

            <div class="col-50">

                <jstl:if test="${!readonly}">
                    <acme:textbox code="label.passkey" path="passKey" readonly="true"
                                  disabled="${!customerForm.active}"/>
                    <acme:textbox code="label.nif" path="nif" readonly="${readonly}"
                                  disabled="${!customerForm.active}"/>

                    <acme:textbox code="customer.billing.address" path="billingAddress"
                                  readonly="${readonly}" disabled="${!customerForm.active}"/>

                </jstl:if>

                <spring:message var="alt" code="customer.company.logo"/>


                <div class="row w3-margin-top">
                    <div class="col-100 w3-padding">
                        <div class="w3-card imgCentered"
                             style="max-height: 22em; max-width: 20em;">
                            <img src="${customerForm.logo}"
                                 class="imgCentered" style="max-height: 20em; max-width: 20em;">
                            <div class="w3-container w3-white">
                                <acme:textbox code="label.none" path="logo" css="w3-border-0" readonly="${readonly}"
                                              placeholder="Logo" disabled="${!customerForm.active}"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-50"></div>

        </div>
        <div class="row">
            <div class="col-100">
                <jstl:if test="${!readonly}">
                    <hr>
                    <jstl:if test="${rol eq 'responsible'}">
                        <acme:submit name="save" code="rendezvous.save"
                                     css="formButton toLeft"/>
                    </jstl:if>
                    <jstl:if test="${rol eq 'manager'}">
                        <acme:button url="customer/manager/toggleActivation.do?customerId=${customerForm.id}"
                                     text="label.activation.${!customerForm.active}"
                                     css="formButton toLeft"/>
                    </jstl:if>
                </jstl:if>

            </div>

        </div>
    </form:form>

</div>