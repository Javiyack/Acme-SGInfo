<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<form:form method="POST" action="file/edit.do" modelAttribute="fileForm" enctype="multipart/form-data">
	<form:input type="file" path="file" id="file" class="formInput"/>
          
	<form:hidden path="id" />
	<form:hidden path="fk" />
	<form:hidden path="type" />
	
	<spring:message code="file.name.placeholder" var="placeholder" />	
	<acme:textbox code="file.name" path="name" placeholder="${placeholder }"/>
	<br />
	<acme:textbox code="subSection.comments" path="comment" />
	<br />
	 
    <form:input type="file" path="file" id="file" class="formInput"/>
                                
	<jstl:if test="${file == null}" >
		<acme:submit name="save" code="file.save" css="formButton toLeft" />&nbsp;
	</jstl:if>
	
	<jstl:if test="${file != null}" >
		<acme:submit name="delete" code="file.delete" css="formButton toLeft" />
	</jstl:if>

	<acme:button url="${fileForm.type}/display.do?${fileForm.type}Id=${fileForm.fk}" text="file.back" css="formButton toLeft" />
	
	<br />

</form:form>