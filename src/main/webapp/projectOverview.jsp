<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <jsp:include page="template/head.jsp" >
    <jsp:param name="title" value="ProjectOverview" />
  </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
  <div class="container-lg">
    <button class="mt-2 mb-2 btn btn-secondary" type="button">
      <a class="nav-link" href="/project/projectForm">Create a new Project</a>
    </button>
    <table class="table table-responsive-lg">
      <thead class="table-active">
      <tr>
        <th scope="col">Id</th>
        <th scope="col">Project Name</th>
        <th scope="col">Status</th>
        <th scope="col">Info</th>
      </tr>
      </thead>
      <tbody>

      <c:if test="${projects != null}">
      <c:forEach var="project" items="${projects}">
        <tr>
          <td>${project.getId()}</td>
          <td>${project.getName()}</td>
          <td>${project.getStatus()}</td>
          <td ><a href="/project/showProject?projectid=${project.getId()}">click to show info </a></td>
        </tr>
      </c:forEach>
      </c:if>

      <tbody>
    </table>
  </div>
</main>
</body>
</html>