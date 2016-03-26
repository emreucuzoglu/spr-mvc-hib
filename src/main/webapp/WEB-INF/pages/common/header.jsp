<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- <%@page import="com.sprhib.utilities.Constants"%> --%>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="icon" href="../../favicon.ico" />
    <link href="${pageContext.request.contextPath}/static/assets/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/static/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/static/assets/css/navbar-fixed-top.css" rel="stylesheet"/>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
<body>
  <nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false"
          aria-controls="navbar">
          <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
        </button>
        <a id="linkLogo" class="navbar-brand" href="${pageContext.request.contextPath}/">SPR-MVC-HIB</a>
      </div>
      <div id="navbar" class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
          <%-- <li><a id="menu_home" href="${pageContext.request.contextPath}"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU}"/></a></li> --%>
          <li><a id="menu_home" href="${pageContext.request.contextPath}"><spring:message code="header.menu"/></a></li>
          <li class="dropdown"><a id="menu_org" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
            <%-- aria-expanded="false"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_ORGANIZATION}"/><span class="caret"></span></a> --%>
            aria-expanded="false"><spring:message code="header.menu.organization"/><span class="caret"></span></a>
            <ul class="dropdown-menu">
              <%-- <li><a id="menu_org_add" href="${pageContext.request.contextPath}/org/add"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_ADD}"/></a></li>
              <li><a id="menu_org_list" href="${pageContext.request.contextPath}/org/list"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_LIST}"/></a></li> --%>
              <li><a id="menu_org_add" href="${pageContext.request.contextPath}/org/add"><spring:message code="header.menu.add"/></a></li>
              <li><a id="menu_org_list" href="${pageContext.request.contextPath}/org/list"><spring:message code="header.menu.list"/></a></li>
            </ul></li>
          <li class="dropdown"><a id="menu_team" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
            <%-- aria-expanded="false"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_TEAM}"/><span class="caret"></span></a> --%>
            aria-expanded="false"><spring:message code="header.menu.team"/><span class="caret"></span></a>
            <ul class="dropdown-menu">
              <%-- <li><a id="menu_team_add" href="${pageContext.request.contextPath}/team/add"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_ADD}"/></a></li>
              <li><a id="menu_team_list" href="${pageContext.request.contextPath}/team/list"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_LIST}"/></a></li> --%>
              <li><a id="menu_team_add" href="${pageContext.request.contextPath}/team/add"><spring:message code="header.menu.add"/></a></li>
              <li><a id="menu_team_list" href="${pageContext.request.contextPath}/team/list"><spring:message code="header.menu.list"/></a></li>
            </ul></li>
          <li class="dropdown"><a id="menu_member" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
            <%-- aria-expanded="false"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_MEMBERS}"/><span class="caret"></span></a> --%>
            aria-expanded="false"><spring:message code="header.menu.member"/><span class="caret"></span></a>
            <ul class="dropdown-menu">
              <%-- <li><a id="menu_member_add" href="${pageContext.request.contextPath}/member/add"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_ADD}"/></a></li>
              <li><a id="menu_member_list" href="${pageContext.request.contextPath}/member/list"><spring:message code="${Constants.MESSAGE_KEY_HEADER_MENU_LIST}"/></a></li> --%>
              <li><a id="menu_member_add" href="${pageContext.request.contextPath}/member/add"><spring:message code="header.menu.add"/></a></li>
              <li><a id="menu_member_list" href="${pageContext.request.contextPath}/member/list"><spring:message code="header.menu.list"/></a></li>
            </ul></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li id="menu_lang_enUs"><a href="?locale=en_US">US</a></li>
          <li id="menu_lang_enGb"><a href="?locale=en_GB">GB</a></li>
        </ul>
      </div>
    </div>
  </nav>
  <!-- Bootstrap core JavaScript
    ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"><\/script>')</script>
  <script src="${pageContext.request.contextPath}/static/assets/js/bootstrap.min.js"></script>
  <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
  <script src="${pageContext.request.contextPath}/static/assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>

