<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- <%@page import="com.sprhib.controller.OrganizationController"%>
<%@page import="com.sprhib.utilities.Constants"%> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%-- <title><spring:message code="${Constants.MESSAGE_KEY_ORGANIZATION_LIST_TITLE}" /></title> --%>
<title><spring:message code="org.list.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="../common/header.jsp"%>
    <h1>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_ORGANIZATION_LIST_HEADER}" /> --%>
      <spring:message code="org.list.header" />
    </h1>
    <p>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_ORGANIZATION_LIST_INTRO}" /> --%>
      <spring:message code="org.list.intro" />
    </p>
    <table border="1px" cellpadding="0" cellspacing="0">
      <thead>
        <tr>
          <%-- <th width="10%"><spring:message code="${Constants.MESSAGE_KEY_FIELD_ID}" /></th>
          <th width="15%"><spring:message code="${Constants.MESSAGE_KEY_FIELD_NAME}" /></th>
          <th width="10%"><spring:message code="${Constants.MESSAGE_KEY_ACTIONS}" /></th> --%>
          <th width="10%"><spring:message code="field.id" /></th>
          <th width="15%"><spring:message code="field.name" /></th>
          <th width="10%"><spring:message code="actions" /></th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="organization" items="${organizations}">
          <tr>
            <td>${organization.id}</td>
            <td>${organization.name}</td>
            <%-- <td><a href="${pageContext.request.contextPath}${OrganizationController.PATH}${OrganizationController.PATH_EDIT}/${organization.id}"><spring:message code="${Constants.MESSAGE_KEY_ACTION_EDIT}" /></a><br /> <a
              href="${pageContext.request.contextPath}${OrganizationController.PATH}${OrganizationController.PATH_DELETE}/${organization.id}"><spring:message code="${Constants.MESSAGE_KEY_ACTION_DELETE}" /></a><br /></td> --%>
              <td><a href="${pageContext.request.contextPath}/org/edit/${organization.id}"><spring:message code="action.edit" /></a><br /> <a
              href="${pageContext.request.contextPath}/org/delete/${organization.id}"><spring:message code="action.delete" /></a><br /></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <%@ include file="../common/footer.jsp"%>
  </div>
</body>
</html>