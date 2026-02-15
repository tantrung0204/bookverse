<%-- 
    Document   : customer-list
    Created on : Feb 9, 2026, 4:23:54 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer list</title>
    </head>
    <body>
        <c:if test="${empty requestScope.customer_list}">
            <p class="empty-msg">Empty List</p>
        </c:if>
        <c:if test="${not empty requestScope.customer_list}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Address</th>
                        <th>Phone number</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${requestScope.customer_list}">
                        <tr>
                            <td>${c.customerId}</td>
                            <td>${c.username}</td>
                            <td>${c.email}</td>
                            <td>${c.address}</td>
                            <td>${c.phoneNumber}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </body>
</html>
