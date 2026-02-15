<%-- 
    Document   : createGenre-view
    Created on : Feb 12, 2026, 8:06:28 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Genre</title>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/genre" method="post">
            <input type="text" name="genreName" placeholder="Genre name">
            <c:if test="${not empty errorName}">
                <p style="color:red">${errorName}</p>
            </c:if>
            <input type="text" name="description" placeholder="description...">            
            <c:if test="${not empty errorDes}">
                <p style="color:red">${errorDes}</p>
            </c:if>
            <button type="submit">Submit</button>
        </form>
        <c:if test="${not empty result}">
            <p>${result}</p>
        </c:if>

    </body>
</html>
