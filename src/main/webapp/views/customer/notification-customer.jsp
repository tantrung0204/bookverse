<%-- 
    Document   : notification-customer
    Created on : 10 Mar 2026, 18:41:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:choose>

            <c:when test="${empty notifications}">
                <div class="empty-notification">
                    <p>No notifications available</p>
                </div>
            </c:when>

            <c:otherwise>

                <c:forEach var="n" items="${notifications}">

                    <div class="notification-card">

                        <div class="notification-icon">
                            🔔
                        </div>

                        <div class="notification-content">
                            <h4>${n.notificationId.title}</h4>
                            <p>${n.notificationId.contentText}</p>
                            <span class="time">
                                ${n.notificationId.createdAt}
                            </span>
                        </div>

                    </div>

                </c:forEach>

            </c:otherwise>

        </c:choose>
    
        </body>
</html>
