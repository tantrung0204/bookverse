<%-- 
    Document   : category_list
    Created on : 10 Feb 2026, 18:33:04
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Category list</title>
    </head>
    <body>
        <c:if test="${empty requestScope.category_list}">
            <p class="empty-msg">Empty List</p>
        </c:if>
        <c:if test="${not empty requestScope.category_list}">
            <table>
                <thead>
                     <a class="text-decoration-none btn btn-primary" href="${pageContext.request.contextPath}/category/account?view=create">Add category
                    <tr>
                        <th>ID</th>
                        <th>Category Name</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${requestScope.category_list}">
                        <tr>
                            <td>${c.categoryId}</td>
                            <td>${c.categoryName}</td>
                            <td>${c.descriptionText}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </body>
</html>
