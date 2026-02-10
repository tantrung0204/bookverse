<%-- 
    Document   : home
    Created on : Feb 10, 2026, 8:17:28 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home page</title>
    </head>
    <body>
        <h1>Products</h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Image</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${requestScope.PRODUCT_LIST}">
                    <tr>
                        <td>${p.productId}</td>
                        <td>${p.name}</td>
                        <td><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                        <td><img src="${p.imageUrl}" alt="image" width="50" height="50"></td>
                        <td><a href="home?action=detail&id=${p.productId}">Detail</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
