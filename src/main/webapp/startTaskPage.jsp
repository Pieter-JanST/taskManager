<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="startTaskPage" />
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
                                        <li>Task: ${role} : ${(task.getNecessaryRoles().get(role)==null)? "not yet assigned" : task.getNecessaryRoles().get(role).getUsername()}</li>
                                    </c:forEach>
                                    <td ><a href="/task/addUserToTask?taskId=${task.getId()}&projectId=${task.getProject().getId()}">click to confirm assign</a></td>
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
                    <li><a></a></li>
                </ul>
            </div>
        </div>
    </div>
</main>
</body>
</html>
