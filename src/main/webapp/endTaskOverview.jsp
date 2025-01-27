<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <jsp:include page="template/head.jsp" >
    <jsp:param name="title" value="TaskOverview" />
  </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
  <div class="container-lg">
    <table class="table table-responsive-lg">
      <thead class="table-active">
      <tr>
        <th scope="col">Id</th>
        <th scope="col">description</th>
        <th scope="col">Project</th>
        <th scope="col">Status</th>
        <th scope="col">End</th>
      </tr>
      </thead>
      <tbody>
      <c:if test="${tasks.size() > 0}">
      <c:forEach var="task" items="${tasks}">
      <tr>
        <td>${task.getId()}</td>
        <td>${task.getDescription()}</td>
        <td>${task.getProject().getName()}</td>

        <td>
            ${task.getStatus()}</td>
        <td ><a href="/task/endTaskPage?taskid=${task.getId()}&projectid=${task.getProject().getId()}">click to end task </a></td>
      </tr>
      </c:forEach>
      </c:if>
      <tbody>
    </table>
  </div>
</main>
</body>
</html>
