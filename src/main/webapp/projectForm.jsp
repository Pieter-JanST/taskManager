<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <jsp:include page="template/head.jsp" >
    <jsp:param name="title" value="CreateProject" />
  </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
  <div class="container-lg">
    <h1>Create a new Project</h1>
    <form action="/project/createProject" method="post">
      <div class="form-group">
        <label for="name">Name:</label>
        <input class="form-control" type="text" id="name" name="name" required>
      </div>
      <div class="form-group">
        <label for="description">Description:</label>
        <input class="form-control" type="text" id="description" name="description" required>
      </div>
      <div class="form-group">
          <label for="duetime">Time due::</label>
          <input class="form-control" type="datetime-local" id="duetime" name="duetime" value="2029-01-01T12:00:00" required>
      </div>
      <input class="btn btn-primary mt-2" type="submit" value="Submit" />
      <a href="javascript:history.back()">Cancel</a>
    </form>
  </div>
</main>
</body>
</html>