<%-- 
    Document   : cus-notification-detail.jsp
    Created on : 11 Mar 2026, 09:29:29
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/notification-list.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <h2>${notification.title}</h2>

        <p class="time">
            ${notification.createdAt}
        </p>
        
         <img src="${pageContext.request.contextPath}/${notification.imageUrl}"
                                             alt="Notification Image"
                                             class="notification-img">
                            

        <p>
            ${notification.contentText}
        </p>

        <a href="${pageContext.request.contextPath}/notification/customer">
            ← Back to notifications
        </a>
    
    </body>
</html>
