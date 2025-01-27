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
                <th scope="col">Start</th>
                <th scope="col">Info</th>
                <th scope="col">Del</th>
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
                                <form onChange="updateStatus(this)" method="post" action="/task/updateTaskStatus?taskid=${task.getId()}&projectid=${project.getId()}">
                                </form>
                                    ${task.getStatus()}</td>
                            <td ><a href="/task/startTaskPage?taskid=${task.getId()}&projectid=${task.getProject().getId()}">click to start task </a></td>

                            <td ><a href="/task/showTask?taskid=${task.getId()}&projectid=${task.getProject().getId()}">click to show info </a></td>
                            <td >
                                <c:if test="${task.getStatus() == 'PENDING' || task.getStatus() == 'EXECUTING'}">
                                    <a href="/task/deleteTask?taskid=${task.getId()}" onclick="return confirm('Are you sure you want to delete this task?');">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="Red" class="bi bi-trash" viewBox="0 0 16 16">
                                            <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z"/>
                                            <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z"/>
                                        </svg>
                                    </a>
                                </c:if>

                                <c:if test="${task.getStatus() != 'PENDING' && task.getStatus() != 'EXECUTING' }">
                                    <a href="/task/deleteTask?taskid=${task.getId()}" >
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="Red" class="bi bi-trash" viewBox="0 0 16 16">
                                            <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z"/>
                                            <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z"/>
                                        </svg>
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            <tbody>
        </table>
    </div>
</main>
</body>
</html>
