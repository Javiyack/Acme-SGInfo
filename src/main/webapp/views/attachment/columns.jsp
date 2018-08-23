<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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


<spring:message code="file.name" var="columTitle" />
<display:column title="${columTitle}">
	<a href="file/download.do?fileId=${row.id}"> <jstl:out
			value="${row.name}" />
	</a>
</display:column>

<spring:message code="file.size" var="columTitle" />
<display:column title="${columTitle}">
	<jstl:set var="sizeKB" value="${row.size / (1024)}"></jstl:set>
	<fmt:formatNumber type='number' maxFractionDigits='3' value='${sizeKB}' />
			KB
		</display:column>

<acme:column property="uploadDate" title="file.uploadDate"
	format="moment.format" />


<display:column>
	<a href="file/download.do?fileId=${row.id}">
		<spring:message code="msg.download" />
	</a>
</display:column>
<jstl:if test="${allowed}">
<display:column>
	<a href="file/edit.do?fileId=${row.id}">
		<spring:message code="msg.edit" />
	</a>
</display:column>
</jstl:if>
<jstl:if test="${allowed}">
<spring:message var = "deleteConfirmation" code="file.delete.confirmation" />
<jstl:set var="deleteUrl" value="file/delte.do?fileId=${row.id}"/>
<display:column>	
		<button style="border: none; background-color: white;" onclick="javascript: showConfirmationAlert('${deleteConfirmation}', '${row.name}', '${deleteUrl}');"><spring:message code="master.page.delete" /></Button>
</display:column>
</jstl:if>


