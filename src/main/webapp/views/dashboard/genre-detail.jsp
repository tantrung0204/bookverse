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
        

        <c:if test="${not empty genre}">
            <table border="1">
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
                            ${genre.genreName}

                        </td>
                        <td>${genre.descriptionText}</td>

                        <c:if test="${genre.status==1}"><td>active</td></c:if>
                        <c:if test="${genre.status==0}"><td>inactive</td></c:if>
                            
                        </tr>
                    </tbody>
                </table>
        </c:if>
        <c:if test="${not empty message}">
            <div style="padding:10px;margin:10px 0;
                 background:#f8d7da;color:#721c24;
                 border:1px solid #f5c6cb;border-radius:5px;">
                ${message}
            </div>
        </c:if>
        <br>
        <a href="${pageContext.request.contextPath}/genre">
            ← Back to Genre list
        </a>       
    </body>
</html>
