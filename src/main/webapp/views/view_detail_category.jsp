<%-- 
    Document   : view_detail_category
    Created on : 11 Feb 2026, 19:42:21
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Category Detail</title>
    </head>
    <body>
        <c:if test ="${view_detail != null}">
            <input type="hidden" name="id" value="${view_detail.categoryId}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Category Name</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${view_detail.categoryId}</td>
                        <td>${view_detail.categoryName}</td>
                        <td>${view_detail.descriptionText}</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
        </c:if>
    </body>
</html>
