<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <jsp:include page="template/head.jsp" >
    <jsp:param name="title" value="endTaskPage" />
  </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
  <div class="container-lg">
    <div class="card mt-3 mb-3">
      <div class="card-body">
        <h2>Task</h2>
        <ul>
          <li>id: ${task.getId()} </li>
          <li>belongs to project: ${task.getProject().getName()} </li>
          <li>description: ${task.getDescription()} </li>
          <li>status: ${task.getStatus()} </li>
          <li>estimatedDuration: ${task.getEstimatedDuration()} </li>
          <li>acceptableDeviation: ${task.getAcceptableDeviation()}</li>
          <div class="card">
            <div class="card-body">
              <p>roles needed</p>
              <ul>
                <c:if test="${task.getNecessaryRoles().size() > 0}">
                  <c:forEach var="role" items="${task.getNecessaryRoles().keySet()}">
                    <li>Task: ${role} : ${(task.getNecessaryRoles().get(role)==null)? "not yet assigned" :task.getNecessaryRoles().get(role).getUsername()}</li>
                  </c:forEach>
                </c:if>
              </ul>
            </div>
          </div>
          <li>startTime: ${task.getTimeSpan().getStartTime() }</li>
          <li>endTime: ${task.getTimeSpan().getEndTime() }</li>
          <li>
            <div class="card">
              <div class="card-body">
                <p>Depends on</p>
                <ul>
                  <c:if test="${task.getDependsOn().size() > 0}">
                    <c:forEach var="dep" items="${task.getDependsOn()}">
                      <li>Task: ${dep.getId()}</li>
                    </c:forEach>
                  </c:if>
                </ul>
              </div>
            </div>
          </li>
          <li><a>
            Current time is: ${time}
          </a></li>
          <form method="post" action="/task/endTask?taskid=${task.getId()}&projectid=${project.getId()}">
            <select  id="failed" name="failed">
              <option value="True">Failed</option>
              <option value="False">Finished</option>
            </select>
            <input class="btn btn-primary mt-2" type="submit" value="Submit" />
            <a href="/task/endTaskOverview">Cancel</a>
          </form>
        </ul>
      </div>
    </div>
  </div>
</main>
</body>
</html>