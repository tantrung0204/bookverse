<%-- 
    Document   : product-detail
    Created on : Feb 10, 2026, 8:42:15 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product detail</title>
    </head>
    <body>
        <div class="container">
            <div class="product-img">
                <img src="${p.imageUrl}" alt="${p.name}" />
            </div>
            <div class="product-info">
                <c:if test="${p.type == 'Book'}">
                    <span class="badge badge-book">Book</span>
                </c:if>
                <c:if test="${p.type == 'Stationery'}">
                    <span class="badge badge-stationery">Stationery</span>
                </c:if>

                <h1>${p.name}</h1>
                <p class="price">
                    <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                </p>
                <p>Status: 
                    <c:choose>
                        <c:when test="${p.stockQuantity > 0}">
                            <span style="color: green">Available (${p.stockQuantity})</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red">Unavailable</span>
                        </c:otherwise>
                    </c:choose>
                </p>

                <hr/>

                <h3>Description</h3>
                <table class="specs-table">

                    <c:if test="${p.type == 'Book'}">
                        <tr>
                            <th>Author:</th>
                            <td>
                                <c:forEach var="a" items="${p.authorCollection}">
                                    ${a.authorName} 
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <th>Publisher:</th>
                            <td>${p.publisher}</td>
                        </tr>
                        <tr>
                            <th>Published Year:</th>
                            <td>${p.publishedYear}</td>
                        </tr>
                        <tr>
                            <th>ISBN:</th>
                            <td>${p.isbn}</td>
                        </tr>
                        <tr>
                            <th>Translator:</th>
                            <td>${p.translator}</td>
                        </tr>
                        <tr>
                            <th colspan="2">Description:</th>
                        </tr>
                        <tr>
                            <td colspan="2">${p.descriptionText}</td>
                        </tr>
                    </c:if>

                    <c:if test="${p.type == 'Stationery'}">
                        <tr>
                            <th>Color:</th>
                            <td>${p.color}</td>
                        </tr>
                        <tr>
                            <th>Material:</th>
                            <td>${p.material}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </body>
</html>
