<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="Action Overview" />
    </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
    <div class="container-lg">
        <table class="table table-responsive-lg">
            <thead class="table-active">
            <tr>
                <th scope="col">userId</th>
                <th scope="col">description</th>

            </tr>
            </thead>
            <tbody>
            <c:if test="${undoActions.size() > 0}">
            <c:forEach var="action" items="${undoActions}">
            <tr>
                <td>${action.getUserId()}</td>
                <td>${action.getDescription()}</td>
            </tr>
            </c:forEach>
            </c:if>
            <c:if test="${redoActions.size() > 0}">
            <c:forEach var="action" items="${redoActions}">
            <tr bgcolor="#d3d3d3">
                <td>${action.getUserId()}</td>
                <td>${action.getDescription()}</td>
            </tr>
            </c:forEach>
            </c:if>
            <tbody>
        </table>
        <a href="/action/undo">
            <button type="button" class="btn btn-primary">
                Undo
            </button>
        </a>
        <a href="/action/redo">
            <button type="button" class="btn btn-primary">
                Redo
            </button>
        </a>
    </div>
</main>
</body>
</html>
