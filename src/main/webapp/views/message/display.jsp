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

<spring:message code="moment.pattern" var="momentFormat" />
<fmt:formatDate value="${m.moment}" pattern="${momentFormat}"
	var="momentVar" />

<div class="seccion">
<ul style="list-style-type: disc">

	<li><b><spring:message code="ms.moment"></spring:message>:</b> <jstl:out
			value="${momentVar}" /></li>

	<li><b><spring:message code="ms.priority"></spring:message>:</b> <jstl:out
			value="${m.priority}" /></li>

	<li><b><spring:message code="ms.sender"></spring:message>:</b> <jstl:out
			value="${m.sender.userAccount.username}" /></li>

	<jstl:choose>
		<jstl:when test="${m.broadcast eq false }">
			<li><b><spring:message code="ms.recipient"></spring:message>:</b>
				<jstl:out value="${m.recipient.userAccount.username}" /></li>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="ms.all" var="allVar" />
			<li><b><spring:message code="ms.recipient"></spring:message>:</b>
				<jstl:out value="${allVar }" /></li>
		</jstl:otherwise>
	</jstl:choose>


	<li><b><spring:message code="ms.subject"></spring:message>:</b> <jstl:out
			value="${m.subject}" /></li>
</ul>
<spring:message code="ms.body"></spring:message>
:
<br>
<div class="dashboard">
	<jstl:out value="${m.body}" />
</div>
<br>
<input type="button" name="back"
	value="<spring:message code="ms.back" />"
	onclick="javascript: relativeRedir('folder/list.do?folderId=${folder.id}')" class ="formButton toLeft" />
<input type="button" name="move"
	value="<spring:message code="ms.move" />"
	onclick="javascript: relativeRedir('message/move.do?messageId=<jstl:out value="${m.getId()}"/>');" 
	class ="formButton toLeft"/>
<input type="button" name="delete"
	value="<spring:message code="ms.delete" />"
	onclick="javascript: relativeRedir('message/delete.do?messageId=${m.getId()}&folderId=${folder.id}');" 
	class ="formButton toLeft"/>
<br>
<br>
<br>

</div>