<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <%@page import="com.sprhib.controller.TeamController"%>
<%@page import="com.sprhib.utilities.Constants"%> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%-- <title><spring:message code="${Constants.MESSAGE_KEY_TEAM_EDIT_TITLE}" /></title> --%>
<title><spring:message code="team.edit.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="../common/header.jsp"%>
    <h1>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_TEAM_EDIT_HEADER}" /> --%>
      <spring:message code="team.edit.header" />
    </h1>
    <p>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_TEAM_EDIT_INTRO}" /> --%>
      <spring:message code="team.edit.intro" />
    </p>
    <%-- <form:form method="POST" modelAttribute="${TeamController.VIEW_MODEL_TEAM}" action="${pageContext.request.contextPath}${TeamController.PATH}${TeamController.PATH_EDIT}/${team.id}"> --%>
    <form:form method="POST" modelAttribute="team" action="${pageContext.request.contextPath}/team/edit/${team.id}">
      <table>
        <tbody>
          <tr>
            <%-- <td><spring:message code="${Constants.MESSAGE_KEY_FIELD_ORGANIZATION}" />:</td> --%>
            <td><spring:message code="field.organization" />:</td>
            <%-- <td><form:select path="${TeamController.MODEL_ATTRIBUTE_ORGANIZATION}"> --%>
            <td><form:select path="organization">
                <c:forEach items="${organizations}" var="tmpOrganization">
                  <option value="${tmpOrganization.id}" <c:if test="${tmpOrganization.id == team.organization.id }">selected</c:if>>
                    ${tmpOrganization.name}</option>
                </c:forEach>
              </form:select></td>
            <%-- <td style="color: red; font-style: italic;"><form:errors path="${TeamController.MODEL_ATTRIBUTE_ORGANIZATION}" /></td> --%>
            <td style="color: red; font-style: italic;"><form:errors path="organization" /></td>
          </tr>
          <tr>
            <%-- <td><spring:message code="${Constants.MESSAGE_KEY_FIELD_NAME}" />:</td>
            <td><form:input path="${TeamController.MODEL_ATTRIBUTE_NAME}" required="required" /></td>
            <td style="color: red; font-style: italic;"><form:errors path="${TeamController.MODEL_ATTRIBUTE_NAME}" /></td> --%>
            <td><spring:message code="field.name" />:</td>
            <td><form:input path="name" required="required" /></td>
            <td style="color: red; font-style: italic;"><form:errors path="name" /></td>
          </tr>
          <tr>
            <%-- <td><spring:message code="${Constants.MESSAGE_KEY_FIELD_RATING}" />:</td>
            <td><form:input path="${TeamController.MODEL_ATTRIBUTE_RATING}" /></td>
            <td style="color: red; font-style: italic;"><form:errors path="${TeamController.MODEL_ATTRIBUTE_RATING}" /></td> --%>
            <td><spring:message code="field.rating" />:</td>
            <td><form:input path="rating" /></td>
            <td style="color: red; font-style: italic;"><form:errors path="rating" /></td>
          </tr>
          <tr>
            <%-- <td><input type="submit" value="<spring:message code="${Constants.MESSAGE_KEY_ACTION_EDIT}"/>" /></td> --%>
            <td><input type="submit" value="<spring:message code="action.edit"/>" /></td>
            <td></td>
          </tr>
        </tbody>
      </table>
    </form:form>
    <%@ include file="../common/footer.jsp"%>
  </div>
</body>
</html>