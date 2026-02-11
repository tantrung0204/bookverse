<%-- 
    Document   : genre-view
    Created on : Feb 11, 2026, 8:48:53 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View genre</title>
    </head>
    <body>
        <c:if test="${empty requestScope.genre_list}">
            <p class="empty-msg"> Empty List </p>
        </c:if>
        <c:if test="${not empty requestScope.genre_list}">
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
                    <c:forEach var="g" items="${requestScope.genre_list}">
                        <tr>
                            <td>${g.genreId}</td>
                            <td>
                                <form action="genre" method="Post">
                                    <input type="hidden" name="id" value="${g.genreId}">
                                    <button type="submit" style="border:none; background:none; color:blue; cursor:pointer;"> ${g.genreName}</button>
                                </form>                              
                            </td>
                            <td>${g.descriptionText}</td>
                             <c:if test="${g.status==1}"><td>ok</td></c:if>
                             <c:if test="${g.status==0}"><td>ko</td></c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </body>
</html>
