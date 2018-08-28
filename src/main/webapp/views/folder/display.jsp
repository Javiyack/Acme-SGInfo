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

<div class="seccion w3-light-green">
		
<h2>
	<jstl:out value="${folder.getName()}" />
</h2>

<fieldset>
	<legend><spring:message code="folder.folders" /></legend>
	<display:table pagesize="10" class="displaytag" name="folders" requestURI="folder/list.do" id="row">
	
		<acme:column property="name" title="folder.name" sortable="true"/>
	
		<display:column>
			<a href="folder/list.do?folderId=<jstl:out value="${row.id}"/>">
				<spring:message code="label.view" />
			</a>
		</display:column>
	
	</display:table>
</fieldset>


<fieldset>
	<legend><spring:message code="folder.messages" /></legend>
	<display:table pagesize="10" class="displaytag" name="messages" requestURI="folder/list.do" id="row2">
	
		<acme:column property="subject" title="ms.subject" sortable="true"/>
		
		<display:column>
			<a href="message/display.do?messageId=${row2.id}">
				<spring:message code="label.view" />
			</a>
		</display:column>
	
	
		<display:column>
			<a href="message/delete.do?messageId=${row2.id}">
				<spring:message code="label.delete" />
			</a>
		</display:column>
	
	</display:table>
</fieldset>



<acme:button text="folder.newfolder" url="folder/create.do?folderId=${folder.id}" />

<jstl:if test="${empty folder.getParentFolder()}" >
	<acme:button text="folder.back" url="folder/list.do" />
</jstl:if>
<jstl:if test="${!empty folder.getParentFolder()}" >
	<acme:button text="folder.back" url="folder/list.do?folderId=${folder.parent.id}" />
</jstl:if>

		
<jstl:if test="${folder.getSystemFolder() eq false }">
	<acme:button text="folder.move" url="folder/move.do?folderId=${folder.getId()}" />
	<acme:button text="folder.delete" url="folder/delete.do?folderId=${folder.getId()}" />

	<jstl:if test="${empty folder.getParentFolder()}">
		<acme:button text="folder.changeName" url="folder/editFirst.do?folderId=${folder.getId()}" />
	</jstl:if>
	<jstl:if test="${!empty folder.getParentFolder()}">
		<acme:button text="folder.changeName" url="folder/edit.do?folderId=${folder.getId()}" />		
	</jstl:if>
</jstl:if>

</div>
