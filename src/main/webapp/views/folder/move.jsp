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




<display:table pagesize="5" class="flat-table0" keepStatus="true"
	name="folders" id="row" requestURI="folder/move.do">
	
	<acme:column property="name" title="folder.folder"/>

	<display:column>
		<jstl:choose>
			<jstl:when test="${folder.id eq row.id}">
				<spring:message code="folder.samefolder" />
			</jstl:when>
			<jstl:when test="${folder.parent eq row}">
				 <spring:message code="folder.alreadyIn" /> 
			</jstl:when>
			<jstl:otherwise>
				<a
					href="folder/saveMove.do?targetfolderId=${row.id}&folderId=${folder.getId()}"><spring:message
						code="folder.move" /></a>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
</display:table>


<acme:cancelButton url="folder/list.do?folderId=${folder.id}" code="folder.back" css ="formButton toLeft"/>


