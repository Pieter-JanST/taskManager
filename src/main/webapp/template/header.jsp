<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<header>
  <nav class="navbar navbar-expand-sm navbar-dark bg-primary">
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" href="/">home</a>
      </li>

      <c:if test="${sessionScope.username == null}">
        <li class="nav-item">
          <a class="nav-link" href="/user/loginPage">login</a>
        </li>
      </c:if>
      <c:if test="${sessionScope.username != null}">
        <li class="nav-item">
          <a class="nav-link" href="/user/logoutUser">Logout</a>
        </li>
      </c:if>
      <li class="nav-item">
        <a class="nav-link" href="/project/projectOverview">projects</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/task/taskOverview">tasks</a>
      </li>

      <li class="nav-item">
        <a class="nav-link" href="/task/endTaskOverview">End Task</a>
      </li>

      <li class="nav-item">
        <a class="nav-link" href="/clock/timePage">Advance Time</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/action/actionPage">actions</a>
      </li>


    </ul>
  </nav>
</header>
<%@ include file="messages.jsp" %>
