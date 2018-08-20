
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form action="servant/manager/edit.do" modelAttribute="servant">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="cancelled" />

	<acme:textbox code="servant.name" path="name" />
	<br />

	<acme:textbox code="servant.description" path="description" />
	<br />

	<acme:textbox code="servant.picture" path="picture" />
	<br />

	<acme:select items="${categories}" itemLabel="name"
		code="servant.category" path="category" />
	<br />

	<acme:submit name="save" code="servant.save" />

	<acme:cancel url="/" code="servant.cancel" />

	<jstl:if test="${servant.id!=0 }">

		<acme:submit name="delete" code="servant.delete" />

	</jstl:if>




</form:form>

