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
        <div class="card mt-3 mb-3">
            <div class="card-body">
                <h2>${project.getName()}</h2>
                <ul>
                    <li>id: ${project.getId()}</li>
                    <li>description: ${project.getDescription()}</li>
                    <li>status: ${project.getStatus()}</li>
                    <li>creationTime: ${project.getCreationTime().toString().split("\\.")[0]} </li>
                    <li>dueTime: ${project.getDueTime().toString().split("\\.")[0]}</li>
                    <li>total excecution time in minutes: ${project.getTotalExecutionTime()}</li>
                </ul>
            </div>

        </div>

        <table class="table table-responsive-lg">
            <thead class="table-active">
            <tr>
                <th scope="col">Id</th>
                <th scope="col">description</th>
                <th scope="col">Status</th>
                <th scope="col">Info</th>
            </tr>
            </thead>
            <tbody>

            <c:if test="${project.getTasks().size() > 0}">
            <c:forEach var="task" items="${project.getTasks()}">
            <tr>
                <td>${task.getId()}</td>
                <td>${task.getDescription()}</td>

                <td>${task.getStatus()}</td>
                <td ><a href="/task/showTask?taskid=${task.getId()}&projectid=${project.getId()}">click to show info</a></td>
            </tr>
            </c:forEach>
            </c:if>
            <tbody>
        </table>
    </div>
</main>
</body>
</html>

<script language="javascript">
    //submit the status update when clicking on the buttons
    function updateStatus(form) {
        form.submit();
    }

</script>