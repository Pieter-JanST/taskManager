<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="switchTaskPage" />
    </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
    <div class="container-lg">
        <div class="card mt-3 mb-3">
            <div class="card-body">
                <h1>Warning: you where already working on a pending task, confirm to switch.</h1>
                <h2>Current Task</h2>
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
                                </c:if>
                            </ul>
                        </div>
                    </div>
                    <li>startTime: ${task.getTimeSpan().getStartTime() }</li>
                    <li>endTime: ${task.getTimeSpan().getEndTime() }</li>
                    <ul>
                        <c:if test="${task.getTimeSpan().getEndTime() != null}">
                            <li>${task.timeCheck()}</li>
                        </c:if>
                    </ul>
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


                    <h2>Other Task</h2>
                    <ul>
                        <li>id: ${oldTask.getId()} </li>
                        <li>belongs to project: ${oldTask.getProject().getName()} </li>
                        <li>description: ${oldTask.getDescription()} </li>
                        <li>status: ${oldTask.getStatus()} </li>
                        <li>estimatedDuration: ${oldTask.getEstimatedDuration()} </li>
                        <li>acceptableDeviation: ${oldTask.getAcceptableDeviation()}</li>
                        <div class="card">
                            <div class="card-body">
                                <p>roles needed</p>
                                <ul>
                                    <c:if test="${oldTask.getNecessaryRoles().size() > 0}">
                                        <c:forEach var="role" items="${oldTask.getNecessaryRoles().keySet()}">
                                            <li>Task: ${role} : ${(oldTask.getNecessaryRoles().get(role)==null)? "not yet assigned" : oldTask.getNecessaryRoles().get(role).getUsername()}</li>
                                        </c:forEach>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                        <li>startTime: ${oldTask.getTimeSpan().getStartTime() }</li>
                        <li>endTime: ${oldTask.getTimeSpan().getEndTime() }</li>
                        <ul>
                            <c:if test="${oldTask.getTimeSpan().getEndTime() != null}">
                                <li>${oldTask.timeCheck()}</li>
                            </c:if>
                        </ul>
                        <li>
                            <div class="card">
                                <div class="card-body">
                                    <p>Depends on</p>
                                    <ul>
                                        <c:if test="${oldTask.getDependsOn().size() > 0}">
                                            <c:forEach var="dep" items="${oldTask.getDependsOn()}">
                                                <li>Task: ${dep.getId()}</li>
                                            </c:forEach>
                                        </c:if>
                                    </ul>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <li><a href="task/switchTask?taskid=${task.getId()}&projectid=${task.getProject().getId()}">Click to confirm the switch.</a></li>
                    <li><a href="/task/taskOverview">Cancel</a></li>
                </ul>
            </div>
        </div>
    </div>
</main>
</body>
</html>