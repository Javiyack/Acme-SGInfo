<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<div class="formPanel">
	<form:form action="${requestUri}" modelAttribute="bill"
		class="formForm">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="floating-box">

<div clasS="w3-row">
			<div class="w3-col m2 w3-left" >
				<ul style="list-style-type:none;padding-left:1em;margin-left:0em;" >
					<li><h2>
					<spring:message code="label.customer" />
				</h2>
					
					<li ><input value="${customer.name}" readonly="${readonly}"
					placeholder="${placeholder}" class="billHead" title="${value}" />
				
					
					<li><input value="${customer.address}" readonly="${readonly}"
					placeholder="${placeholder}" class="billHead" title="${value}" />
				
					
					<li><input value="${customer.nif}" readonly="${readonly}"
					placeholder="${placeholder}" class="billHead" title="${value}" />
				
					
					<li><input value="${customer.webSite}" readonly="${readonly}"
					placeholder="${placeholder}" class="billHead" title="${value}" />
				
					
					<li><input value="${customer.email}" readonly="${readonly}"
					placeholder="${placeholder}" class="billHead" title="${value}" />
				
				</ul>	
			</div>
			<div class="w3-col m8 w3-center">&nbsp;</div>
			<div class="w3-col m2 w3-right " style="padding-right:1em;">
				<ul  style="list-style-type:none">
				 	<li><h2>
						<br>
					</h2>				
					<li><input value="${biller.name}" readonly="${readonly}"
					style="text-align: right;" placeholder="${placeholder}"
					class="billHead  toRight" /> 
					 <li><input value="${biller.address}"
					readonly="${readonly}" style="text-align: right;"
					placeholder="${placeholder}" class="billHead  toRight" /> 
					<li><input value="${biller.webSite}" readonly="${readonly}"
					style="text-align: right;" placeholder="${placeholder}"
					class="billHead  toRight" />
					 <li><input value="${biller.email}" readonly="${readonly}" style="text-align: right;"
					placeholder="${placeholder}" class="billHead  toRight" />
					 <li> <input value="${biller.nif}" readonly="${readonly}"
					style="text-align: right;" placeholder="${placeholder}"
					class="billHead  toRight" />
				 
				 </ul>
			</div>
</div>
			<br>
			<div>
				
				<spring:message code="label.amount" var="labelAmount"/>
				<display:table class="w3-table w3-striped w3-bordered" name="labors"
					id="row">

					<spring:message code="label.incidence" var="title" />
					<display:column title="${title }" value="${row.incidence.title}"/>
     		   		<spring:message code="label.labor" var="title" />
					<display:column property="title" title="${title}" sortable="true"
						class="${classTd}" />
					<acme:column property="description" title="label.description" />
					<acme:column property="time" title="label.time" format="time.format"/>
					<spring:message code="label.amount" var="labelAmount"/>
					<display:column title="${labelAmount }">
					<fmt:formatNumber  value = "${row.time.hours * precioHora +  row.time.minutes * precioHora / 60 }" 
         				type = "number" maxFractionDigits = "2" minFractionDigits = "2" />
     		   		</display:column>
					<input value="${row.time.hours * precioHora +  row.time.minutes * precioHora / 60 }" />
				</display:table>
			</div>
			<hr>	
			<div class="w3-row">
			<div class="w3-third w3-container w3-left">				
				
					<spring:message code="label.tax.base" />
				
				<br />
			</div>
			<div class="w3-third w3-container w3-right">
				<div class="billHead w3-right">
				<fmt:formatNumber  value = "${bill.amount.amount}" 
         				type = "number" maxFractionDigits = "2" minFractionDigits = "2" />
         				<jstl:out value="${bill.amount.currency}"/>     		   							
				</div>
				<br />
			</div>
			</div>
			<div class="w3-row">
			<div class="w3-third w3-container w3-left">				
				
					<spring:message code="label.iva" />
				
				<br />
			</div>
			<div class="w3-third w3-container w3-right">
				<div class="billHead w3-right">
				21%					
				</div>
				<br />
			</div>
			</div>
			<div class="w3-row">
			
			<div class="w3-third w3-container w3-left">				
				<h2>
					<spring:message code="label.total" />
				</h2>
				<br />
			</div>
			<div class="w3-third w3-container w3-right">
				<h2 class="billHead w3-right">
				<fmt:formatNumber  value = "${bill.amount.amount * 1.21}" 
         				type = "number" maxFractionDigits = "2" minFractionDigits = "2" />
         				<jstl:out value="${bill.amount.currency}"/>     		   							
				</h2>
				<br />
			</div>
			
			
		</div>
		</div>
		<br>
		<div class="w3-panel">
			<acme:cancelButton url="${backUrl}" code="label.back"
				css="formButton w3-left" />
			<br /> <br />
		</div>
	</form:form>
</div>
<jstl:out value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"></jstl:out>