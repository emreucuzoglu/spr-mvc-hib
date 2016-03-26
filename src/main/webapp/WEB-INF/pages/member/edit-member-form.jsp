<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <%@page import="com.sprhib.controller.TeamController"%>
<%@page import="com.sprhib.controller.MemberController"%>
<%@page import="com.sprhib.utilities.Constants"%> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%-- <title><spring:message code="${Constants.MESSAGE_KEY_MEMBER_EDIT_TITLE}" /></title> --%>
<title><spring:message code="member.edit.title" /></title>
</head>
<%@ include file="../common/header.jsp"%>
<body>
  <div class="container">
    <h1>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_MEMBER_EDIT_HEADER}" /> --%>
      <spring:message code="member.edit.header" />
    </h1>
    <p>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_MEMBER_EDIT_INTRO}" /> --%>
      <spring:message code="member.edit.intro" />
    </p>
    <%-- <form:form method="POST" modelAttribute="${MemberController.VIEW_MODEL_MEMBER}" action="${pageContext.request.contextPath}${MemberController.PATH}${MemberController.PATH_EDIT}/${member.id}"> --%>
    <form:form method="POST" modelAttribute="member" action="${pageContext.request.contextPath}/member/edit/${member.id}">
      <table>
        <tbody>
          <tr>
            <%-- <td><spring:message code="${Constants.MESSAGE_KEY_FIELD_TEAMS}" />:</td> --%>
            <td><spring:message code="field.teams" />:</td>
            <%-- <td><form:select path="${MemberController.MODEL_ATTRIBUTE_TEAMS}"> --%>
            <td><form:select path="teams">
                <c:forEach items="${teams}" var="tmpTeam">
                  <option value="${tmpTeam.id}"
                    <c:forEach items="${member.teams}" var="tmpMemberTeam">  
                   <c:if test="${tmpTeam.id == tmpMemberTeam.id }">selected</c:if>
                </c:forEach>>
                    ${tmpTeam.name}</option>
                </c:forEach>
              </form:select></td>
            <%-- <td style="color: red; font-style: italic;"><form:errors path="${MemberController.MODEL_ATTRIBUTE_TEAMS}" /></td> --%>
            <td style="color: red; font-style: italic;"><form:errors path="teams" /></td>
          </tr>
          <tr>
            <%-- <td><spring:message code="${Constants.MESSAGE_KEY_FIELD_NAME}" />:</td> --%>
            <td><spring:message code="field.name" />:</td>
            <%-- <td><form:input path="${MemberController.MODEL_ATTRIBUTE_NAME}" required="required" /></td> --%>
            <td><form:input path="name" required="required" /></td>
            <%-- <td style="color: red; font-style: italic;"><form:errors path="${MemberController.MODEL_ATTRIBUTE_NAME}" /></td> --%>
            <td style="color: red; font-style: italic;"><form:errors path="name" /></td>
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