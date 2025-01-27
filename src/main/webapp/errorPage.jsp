
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="template/head.jsp" >
        <jsp:param name="title" value="Error" />
    </jsp:include>
</head>
<body>
<%@ include file="template/header.jsp" %>

<main>
    <div class="container">
        <h1><%= request.getAttribute("errormessage") %></h1>
    </div>
</main>

</body>
</html>