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
<div class="seccion w3-light-green">

	<div class="w3-row">
		<div class="w3-col w3-quarter">

			<i class="w3-bar-item fa fa fa-folder-open-o w3-xxlarge"></i> <span
				class="w3-xlarge" style="margin-bottom: 1em; margin-left: 0.5em;"><jstl:out
					value="${folder.name}" /></span>
			<ul class="w3-ul w3-card-4 w3-padding">
				<jstl:forEach items="${folders}" var="subFolder">
					<li class="menuItem w3-bar"
						onclick="relativeRedir('folder/list.do?folderId=${subFolder.id}');">
						<div class="w3-quarter">
							<i
								class="fa fa-folder-o w3-bar-item w3-circle w3-hide-small w3-xlarge"></i>
						</div>
						<div class="w3-threequarter">
							<div class="w3-bar-item">
								<span class="w3-large"><jstl:out
										value=" ${subFolder.name}" /></span>
							</div>
						</div>
					</li>
				</jstl:forEach>
				<li class="w3-bar">
					 <jstl:if test="${folder!=null}">
						<div class="w3-quarter toRight">
							<i class="w3-bar-item fa fa-level-up w3-xlarge"
								onclick="relativeRedir('folder/list.do?folderId=${folder.parent.id}');"
								onmouseenter="overEffect(this);"
								onmouseleave="overEffect(this);"></i>
						</div>
					</jstl:if>
					 <jstl:if test="${folder!=null}">
						<div class="w3-quarter toRight">
							<spring:message var="msg" code="msg.delte.folder.confirmation" />
							<jstl:set var="url"
								value="/folder/delete.do?folderId=${folder.id}"></jstl:set>

							<i class="w3-bar-item fa fa-remove w3-xlarge"
								onclick="javascript: showConfirmationAlert(' ','${msg}','${url}');"
								onmouseenter="overEffect(this);"
								onmouseleave="overEffect(this);"></i>
						</div>
					</jstl:if> 
					<jstl:if test="${folder!=null}">
						<div class="w3-quarter toRight">
							<i class="w3-bar-item fa fa-edit w3-xlarge"
								onclick="relativeRedir('folder/edit.do?folderId=${folder.id}');"
								onmouseenter="overEffect(this);"
								onmouseleave="overEffect(this);"></i>
						</div>
					</jstl:if><div class="w3-quarter toRight">
						<i class="w3-bar-item fa fa-plus-square w3-xlarge"
							onclick="relativeRedir('folder/create.do?folderId=${folder.id}');"
							onmouseenter="overEffect(this);" onmouseleave="overEffect(this);"></i>
					</div>
				</li>
			</ul>
		</div>
		<div class="w3-col w3-panel w3-threequarter"><%@ include file="/views/message/list.jsp"%>
		</div>
	</div>
</div>