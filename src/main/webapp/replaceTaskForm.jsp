<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <jsp:include page="template/head.jsp" >
    <jsp:param name="title" value="replaceTask" />
  </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
  <div class="container-lg">
    <h1>ReplaceTask a Task</h1>
    <form action="/task/replaceTask" method="post">
      <input type="number" value="${param.projectid}" id="projectid" name="projectid" required hidden="hidden">
      <input type="number" value="${param.taskid}" id="taskid" name="taskid" required hidden="hidden">

      <div class="form-group">
        <label for="desc">Desc:</label>
        <input class="form-control" type="text" id="desc" name="desc" required>
      </div>
      <div class="form-group">
        <label for="estimated">Estimated Duration:</label>
        <input class="form-control" type="number" id="estimated" name="estimated" required>
      </div>
      <div class="form-group">
        <label for="deviation">Acceptable Deviation:</label>
        <input class="form-control" type="number" id="deviation" name="deviation" value="2" required>
      </div>
      <div class="form-group">
        <label for="rolesNecessary">Roles necessary for the task:</label>
        <input class="form-control" type="text" id="rolesNecessary" name="rolesNecessary" value="JavaProgrammer" required>
      </div>
      <div class="form-group">
        <label for="dependsOn">depends on::</label>
        <select id="dependsOn" name="dependsOn" multiple="multiple">
          <option value="-1">Independent</option>
          <c:if test="${tasks.size() > 0}">
            <c:forEach var="task" items="${tasks}">
              <option value="${task.getId()}">${task.getId()}: ${task.getDescription()}</option>
            </c:forEach>
          </c:if>
        </select>
      </div>

      <input class="btn btn-primary mt-2" type="submit" value="Submit" />
    </form>
  </div>
</main>
</body>
</html>