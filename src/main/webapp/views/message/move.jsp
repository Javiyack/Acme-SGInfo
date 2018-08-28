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




<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="folders"  id="row" requestURI="message/move.do">
	
	<acme:column property="name" title="ms.folderName"/>

	<display:column>
	<jstl:choose>
	<jstl:when test="${folder.id eq row.id}">
	<spring:message code="ms.alreadyIn" />
	</jstl:when>
	<jstl:otherwise>
	<a href="message/saveMove.do?folderId=${row.id}&messageId=${m.getId()}"><spring:message
						code="ms.move" /></a>
	</jstl:otherwise>
	</jstl:choose>
	</display:column>
</display:table>

<acme:cancelButton url="message/display.do?messageId=${m.id}" code="ms.back" css ="formButton toLeft"/>
