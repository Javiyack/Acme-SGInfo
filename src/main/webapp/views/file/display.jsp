<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


	
<acme:labelvalue code="file.name" value="${name}" /> 
<acme:labelvalue code="subSection.comments" value="${fileForm.comment}"/> 
<acme:labelvalue code="file.uploadDate" value="${uploadDate}" isDatetime="true"/> 

<br/>

	<acme:button url="file/edit.do?fileId=${fileForm.id}" text="file.edit" css="formButton toLeft" />

<acme:button url="${fileForm.type}/display.do?${fileForm.type}Id=${fileForm.fk}" text="file.back" css="formButton toLeft" />
	

&nbsp;


	
		

