<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="Home" />
    </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
    <div class="container">
        <h1>Hello</h1>
        <%
            String username = (String) session.getAttribute("username");
            List<String> roles = (List<String>) session.getAttribute("roles");
            if (username != null) {
        %>
        <p>Welcome <%= username %></p>
        <p>Your roles: <%= roles %></p>
        <% }
            if (roles != null && roles.contains("ProjectManager")){%>
                <button class="mt-2 mb-2 btn btn-secondary" type="button">
                <a class="nav-link" href="/task/taskForm">Create a new Task</a>
                </button>
            <%}
            else { %>
        <p>Please log in to continue</p>
        <% } %>
    </div>
</main>

</body>
</html>