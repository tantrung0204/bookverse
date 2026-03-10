<%-- 
    Document   : notification-customer
    Created on : 10 Mar 2026, 18:41:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

    <c:forEach var="n" items="${notifications}">

        <div class="notification-item">

            <h4>${n.notificationId.title}</h4>

            <p>${n.notificationId.contentText}</p>

            <small>${n.notificationId.createdAt}</small>

        </div>

    </c:forEach>
        </body>
</html>
