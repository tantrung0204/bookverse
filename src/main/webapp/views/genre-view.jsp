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
        <form action="genre" method="get">
            <input type="text" name="keyword" placeholder="Search genre name...">
            <button type="submit">Search</button>
        </form>

        <a href="${pageContext.request.contextPath}/views/createGenre-view.jsp">Create Genre</a>
        <c:if test="${empty requestScope.genre_list}">

            <c:if test="${empty requestScope.genre}">
                <p class="empty-msg"> No genre found </p>
            </c:if>
            <c:if test="${not empty requestScope.genre}">
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
                            <td>${genre.genreId}</td>
                            <td>
                                <form action="genre" method="Get">
                                    <input type="hidden" name="id" value="${genre.genreId}">
                                    <button type="submit" style="border:none; background:none; color:blue; cursor:pointer;"> ${genre.genreName}</button>
                                </form>                              
                            </td>
                            <td>${genre.descriptionText}</td>
                            <c:if test="${genre.status==1}"><td>active</td></c:if>
                            <c:if test="${genre.status==0}"><td>inactive</td></c:if>
                            </tr>

                        </tbody>
                    </table>
            </c:if>
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
                                <form action="genre" method="Get">
                                    <input type="hidden" name="id" value="${g.genreId}">
                                    <button type="submit" style="border:none; background:none; color:blue; cursor:pointer;"> ${g.genreName}</button>
                                </form>                              
                            </td>
                            <td>${g.descriptionText}</td>
                            <c:if test="${g.status==1}"><td>active</td></c:if>
                            <c:if test="${g.status==0}"><td>inactive</td></c:if>
                            </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </body>
</html>
