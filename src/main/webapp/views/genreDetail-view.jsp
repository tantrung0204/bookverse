<%-- 
    Document   : genreDetail-view
    Created on : Feb 11, 2026, 9:57:55 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Genre Detail</title>
    </head>
    <body>
        <c:if test="${empty requestScope.genre_detail}">
            <p class="empty-msg"> Empty List </p>
        </c:if>
        <c:if test="${not empty requestScope.genre_detail}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Genre name</th>
                        <th>Description</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>

                    <tr>
                        <td>${genre_detail.genreId}</td>
                        <td>
                            ${genre_detail.genreName}

                        </td>
                        <td>${genre_detail.descriptionText}</td>
                        <c:if test="${genre_detail.status==1}"><td>ok</td></c:if>
                        <c:if test="${genre_detail.status==0}"><td>ko</td></c:if>
                        </tr>

                    </tbody>
                </table>
        </c:if>
    </body>
</html>
