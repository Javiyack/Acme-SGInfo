<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.hibernate.engine.spi.RowSelection"%>
<%@page import="java.util.Date"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<jsp:useBean id="date" class="java.util.Date" />


<ul class="w3-ul ">
	<jstl:forEach items="${facturas}" var="facturas">
		<li value="${facturas.key.name}" class="w3-display-container">
			<div class="seccion w3-flat-green-sea">
				<div class="w3-row">
					<div class="w3-col m2 w3-center">
						<jstl:out value="${facturas.key.name}" />
					</div>
				</div>
				<div class="w3-row">
					<div class="w3-col m1 w3-center">&nbsp;</div>
					<div class="w3-col m3 w3-center"><strong><spring:message code="label.moment"/></strong></div>
					<div class="w3-col m2 w3-center"><strong><spring:message code="label.year"/></strong></div>
					<div class="w3-col m1 w3-center"><strong><spring:message code="label.month"/></strong></div>
					<div class="w3-col m2 w3-center"><strong><spring:message code="label.amount"/></strong></div>
					<div class="w3-col m1 w3-center"><strong><spring:message code="label.curency"/></strong></div>
					<div class="w3-col m2 w3-center"><strong><spring:message code="label.view"/></strong></div>
				</div>

				<ul class="w3-ul">
					<li><jstl:forEach items="${facturas.value}" var="row">

							<jstl:if test="${row.moment < date}">
								<jstl:set var="classTd" value="passed" />
							</jstl:if>
							<jstl:if test="${row.moment > date}">
								<jstl:set var="classTd" value="" />
							</jstl:if>
							<spring:message code="moment.format" var="momentFormat" />
							<spring:message code="date.pattern" var="datePattern" />
							<spring:message code="incidencia.publication.moment"
								var="momentTilte" />
							<li><div class="w3-row">
									<div class="w3-col m1 w3-center">&nbsp;</div>
									<div class="w3-col m3 w3-center">
										<fmt:formatDate pattern="${datePattern}" value="${row.moment}" />
									</div>
									<div class="w3-col m2 w3-center">
										<jstl:out value="${row.year}" />
									</div>
									<div class="w3-col m1 w3-center">
										<jstl:out value="${row.month}" />
									</div>
									<div class="w3-col m2 w3-center">
										<fmt:formatNumber type="number" maxFractionDigits="2"
											minFractionDigits="2" value="${row.amount.amount}" />
									</div>
									<div class="w3-col m1 w3-center">
										<jstl:out value="${row.amount.currency}" />
									</div>
									<div class="w3-col m2 w3-center">
										<a href="billing/manager/edit.do?id=${row.id}"> <i
								class="fa fa-eye w3-xlarge"></i>
										</a>
									</div>
								</div></li>

						</jstl:forEach>
				</ul>
			</div>
		</li>
	</jstl:forEach>
</ul>
<br />
<br />
<acme:backButton text="label.back" css="formButton toLeft" />
<acme:button url="/billing/manager/generate.do" text="label.new" />
<br />
<br />
