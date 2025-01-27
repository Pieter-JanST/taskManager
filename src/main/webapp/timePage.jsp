<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <jsp:include page="template/head.jsp" >
    <jsp:param name="title" value="Clock" />
  </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
  <div class="container">
    <h1>CurrentTime</h1>
    <p><%= request.getAttribute("clock") %></p>

    <form action="/clock/advanceTime" method="post">
      <div class="form-group">
        <label for="systemTime">Time:</label>
        <input class="form-control" type="datetime-local" id="systemTime" name="systemTime" value="<%=request.getAttribute("clockValue")%>" required>
      </div>
      <input class="btn btn-primary mt-2" type="submit" value="Submit" />
    </form>

    <form action="/clock/advanceTime" method="post">
      <div class="form-group">
        <label for="minutesTime">Minutes:</label>
        <input class="form-control" type="number" id="minutesTime" name="minutesTime" value="50" required>
      </div>
      <input class="btn btn-primary mt-2" type="submit" value="Submit" />
      <a href="/">Cancel</a>
    </form>

  </div>

</main>

</body>
</html>