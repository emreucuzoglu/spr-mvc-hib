<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <%@page import="com.sprhib.controller.OrganizationController"%>
<%@page import="com.sprhib.utilities.Constants"%> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%-- <title><spring:message code="${Constants.MESSAGE_KEY_ORGANIZATION_ADD_TITLE}" /></title> --%>
<title><spring:message code="org.add.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="../common/header.jsp"%>
    <h1>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_ORGANIZATION_ADD_HEADER}" /> --%>
      <spring:message code="org.add.header" />
    </h1>
    <p>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_ORGANIZATION_ADD_INTRO}" /> --%>
      <spring:message code="org.add.intro" />
    </p>
    <%-- <form:form method="POST" modelAttribute="${OrganizationController.VIEW_MODEL_ORGANIZATION}" action="${pageContext.request.contextPath}${OrganizationController.PATH}${OrganizationController.PATH_ADD}"> --%>
    <form:form method="POST" modelAttribute="organization" action="${pageContext.request.contextPath}/org/add">
      <table>
        <tbody>
          <tr>
            <%-- <td id="org_add_name_txt"><spring:message code="${Constants.MESSAGE_KEY_FIELD_NAME}" />:</td>
            <td id="org_add_name_val"><form:input path="${OrganizationController.MODEL_ATTRIBUTE_NAME}" required="required" /></td>
            <td style="color: red; font-style: italic;"><form:errors path="${OrganizationController.MODEL_ATTRIBUTE_NAME}" /></td> --%>
            <td id="org_add_name_txt"><spring:message code="field.name" />:</td>
            <td id="org_add_name_val"><form:input path="name" required="required" /></td>
            <td style="color: red; font-style: italic;"><form:errors path="name" /></td>
          </tr>
          <tr>
            <%-- <td><input type="submit" value="<spring:message code="${Constants.MESSAGE_KEY_ACTION_ADD}"/>" /></td> --%>
            <td><input type="submit" value="<spring:message code="action.add"/>" /></td>
            <td></td>
          </tr>
        </tbody>
      </table>
    </form:form>
    <%@ include file="../common/footer.jsp"%>
  </div>
</body>
</html>