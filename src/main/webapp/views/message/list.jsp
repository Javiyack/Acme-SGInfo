<%@page import="org.springframework.context.annotation.Import"%>
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

<legend>
	<spring:message code="folder.messages" />
</legend>
<display:table pagesize="10" class="displaytag" name="postBoxes"
	requestURI="folder/list.do" id="row2">
	<spring:message code="ms.subject" var="title"></spring:message>
	<display:column title="${title}">
		<a href="message/display.do?messageId=${row2.message.id}"> <jstl:out value="${row2.message.subject}"/>
		</a>
	</display:column>
	<acme:column property="message.sender.userAccount.username" title="ms.sender" sortable="true" />
	<acme:column property="message.moment" title="ms.moment" sortable="true" />
	<jstl:if test="${folder==null}">
		<acme:column property="folder.name" title="folder.folder" sortable="true" />	
	</jstl:if>
	<display:column>

		<i class="w3-bar-item fa fa fa-remove w3-xlarge" 
			onclick="relativeRedir('message/delete.do?messageId=${row2.message.id}&folderId=${folder.id}');"
			onmouseenter="overEffect(this);" onmouseleave="overEffect(this);"></i>		
	</display:column>
</display:table>
<p>
	<i class="w3-bar-item fa fa fa-edit w3-xlarge toRight" 
	onclick="relativeRedir('message/create.do');"
	onmouseenter="overEffect(this);" onmouseleave="overEffect(this);"></i>
</p>