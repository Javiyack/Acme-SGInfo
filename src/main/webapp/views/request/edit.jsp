
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
          value="${(display || !owns) && request.id != 0}"/>

<jstl:set var="creditCardNeeded"
          value="${request.servant.price>0}"/>
<jstl:set var="creator"
          value="${request.responsible.userAccount eq logedAccount}"/>
<jstl:set var="reciber"
          value="${rol eq 'manager'}"/>


<div class="seccion w3-light-grey">
    <form:form action="${requestUri}" modelAttribute="request">

        <form:hidden path="id"/>
        <form:hidden path="version"/>
        <form:hidden path="servant.id"/>
        <form:hidden path="responsible.id"/>
        <div class="row">
            <div class="col-100" style="padding-bottom: 0px!important;">
                <a href="" class="iButton" style="padding-bottom: 0px!important;">Acme-Showroom/</a>
                <a href="servant/display.do?itemId=${request.servant.id}" class="iButton" style="padding-bottom: 0px!important;">
                    <jstl:out value="${request.servant.name}"/>/</a>
                <a href="actor/display.do?actorId=${request.responsible.id}" class="fa fa-user font-awesome w3-margin-right toRight">
                    <jstl:out value="${request.responsible.userAccount.username}"/>
                </a>
                <hr style="margin-top: 0.2em;">
            </div>
        </div>
        <div class="row">
            <div class="col-50">
                <acme:textbox code="label.item" path="servant.name"
                              readonly="true"/>
            </div>
            <div class="col-25">
                <acme:textbox code="label.price" path="servant.price"
                              readonly="true"/>
            </div>
            <div class="col-25">
                <acme:moment code="label.moment" path="creationMoment"
                             readonly="true" css="flat"/>
                <acme:moment code="label.moment" path="startingDay"
                             readonly="true" css="flat"/>
                <acme:moment code="label.moment" path="endingDay"
                             readonly="true" css="flat"/>
            </div>
        </div>
        <div class="row">
            <div class="col-50">
                <acme:textbox code="label.description" path="servant.description"
                              readonly="true"/>
            </div>
            <div class="col-50">
                <form:label path="status">
                    <spring:message code="label.status"/>
                </form:label>
                <jstl:if test="${request.status != 'PENDING'}">
                    <form:hidden path="status"/>
                </jstl:if>
                <select id="status" name="status" >
                    <jstl:forEach items="${estados}" var="state">
                        <option value="${state}" id="${state}" <jstl:if test="${state eq request.status}">
                            selected
                        </jstl:if> <jstl:if test="${request.status != 'PENDING' or (creator and state != 'PENDING') or (reciber and state eq 'PENDING')}">
                            disabled
                        </jstl:if> >
                            <spring:message code="request.status.${state}"/>
                        </option>
                    </jstl:forEach>
                </select>
            </div>
        </div>
        <hr>

        <div class="row">
            <div class="col-100">
                <jstl:if test="${request.id==0 or reciber and request.status eq 'PENDING'}">
                    <hr>
                    <acme:submit name="save" code="label.save"
                                 css="btn formButton"/>
                </jstl:if>
            </div>

        </div>

    </form:form>

</div>