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
<jstl:out value="${requestUri}"/>
<form:form action="${requestUri}" modelAttribute="folder">

	<form:hidden path="id" />
	<form:hidden path="version" />
	 <form:hidden path="systemFolder" />

<acme:select items="${otherFolders}" code="folder.folders"
								itemLabel="name" path="parent" css="formSelect w3-text-black"
								addEmpty="true" />
							
	<acme:textbox code="folder.name" path="name" />
	<br />

	<acme:submit name="save" code="folder.save" css ="formButton toLeft"/>&nbsp;
	
	<jstl:choose>
		<jstl:when test="${ editing eq false}">
			<input type="button" name="back"
				value="<spring:message code="folder.back"/>"
				onclick="javascript:relativeRedir('folder/list.do?folderId=${parent.id}')" class ="formButton toLeft" />
		</jstl:when>
		<jstl:otherwise>
			<input type="button" name="back"
				value="<spring:message code="folder.back"/>"
				onclick="javascript:relativeRedir('folder/list.do?folderId=<jstl:out value="${folder.getId()}"/>')" class ="formButton toLeft" />
		</jstl:otherwise>

	</jstl:choose>
</form:form>