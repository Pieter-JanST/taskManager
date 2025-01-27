<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="Login" />
    </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>


<main>
    <div class="container">
        <form  method="post" action="/user/loginUser">
            <h2 >Please sign in</h2>
            <p>
                <label for="username">Username</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Username" value="user" required autofocus>
            </p>
            <p>
                <label for="password" >Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password" value="t" required>
            </p>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        </form>
    </div>
</main>

</body>
</html>

