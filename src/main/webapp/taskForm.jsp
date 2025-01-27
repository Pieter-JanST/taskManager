<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="CreateTask" />
    </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
    <div class="container-lg">
        <h1>Create a new Task</h1>
        <form action="/task/createTask" method="post">
            <!--input type="number" value="${param.projectid}" id="projectid" name="projectid" required hidden="hidden"-->
            <div class="form-group">
                <label for="project">link to Project::</label>
                <select id="project" name="project" multiple="multiple">
                    <c:if test="${projects.size() > 0}">
                        <c:forEach var="project" items="${projects}">
                            <option value="${project.getId()}">${project.getId()}: ${project.getName()}</option>
                        </c:forEach>
                    </c:if>
                </select>
            </div>

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
                <p><label for="rolesNecessary">Roles necessary for the task:</label></p>
                <small>Separate each role with a comma and use upper camel case(e.g. PythonProgrammer,Developer).</small>
                <input class="form-control" type="text" id="rolesNecessary" name="rolesNecessary" value="PythonProgrammer" required>
            </div>
            <p>Other tasks:</p>
            <c:if test="${projects.size() > 0}">
                <c:forEach var="project" items="${projects}">
                    <c:forEach var="task" items="${project.getTasks()}">
                        <p>${task.getId()}: ${task.getDescription()}</p>
                    </c:forEach>
                </c:forEach>
                <div class="form-group">
                    <p><label for="dependsOn">Id's of dependant tasks:</label></p>
                    <small>Separate each id with a comma (e.g. 3,5).</small>
                    <input class="form-control" type="text" id="dependsOn" name="dependsOn" value="">
                </div>
            </c:if>

            <!--div class="form-group">
                <label for="dependsOn">depends on::</label>
                <select id="dependsOn" name="dependsOn" multiple="multiple">
                    <option value="-1">Independent</option>
                    <c:if test="${tasks.size() > 0}">
                        <c:forEach var="task" items="${tasks}">
                            <option value="${task.getId()}">${task.getId()}: ${task.getDescription()}</option>
                        </c:forEach>
                    </c:if>
                </select>
            </div-->

            <input class="btn btn-primary mt-2" type="submit" value="Submit" />
            <a href="javascript:history.back()">Cancel</a>
        </form>
    </div>
</main>
</body>
</html>