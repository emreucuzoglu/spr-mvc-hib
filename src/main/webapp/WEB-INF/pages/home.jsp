<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- <%@page import="com.sprhib.utilities.Constants"%> --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%-- <title><spring:message code="${Constants.MESSAGE_KEY_HOME_TITLE}" /></title> --%>
<title><spring:message code="home.title" /></title>
</head>
<body>
  <%@ include file="./common/header.jsp"%>
  <div class="container">
    <h1>
      <%-- <spring:message code="${Constants.MESSAGE_KEY_HOME_HEADER}" /> --%>
      <spring:message code="home.header" />
    </h1>
    <p id="home_message">${message}<br />
    </p>
    <%@ include file="./common/footer.jsp"%>
  </div>
</body>
</html>