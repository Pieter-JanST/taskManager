<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="container">


    <c:if test="${param.succesmessage.length()>0}">
        <div class="alert alert-success mt-2" role="alert">
                ${ param.succesmessage}
        </div>
    </c:if>

    <c:if test="${param.errormessage.length()>0}">
        <div class="alert alert-danger mt-2" role="alert">
                ${ param.errormessage}
        </div>
    </c:if>

    <c:if test="${requestScope.containsKey('succesmessage')}">
        <div class="alert alert-success mt-2" role="alert">
            ${requestScope["succesmessage"]}
        </div>
    </c:if>

    <c:if test="${requestScope.containsKey('errormessage')}">
        <div class="alert alert-danger mt-2" role="alert">
                ${requestScope["errormessage"]}
        </div>
    </c:if>

</div>