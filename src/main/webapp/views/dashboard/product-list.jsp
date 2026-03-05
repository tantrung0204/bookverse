<%-- 
    Document   : product-list.jsp
    Created on : 24 Feb 2026, 16:55:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý Sản phẩm</title>
    </head>
    <body>
        <h1>Quản lý Sản phẩm</h1>

        <div style="margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/product?tab=book">
                <c:if test="${currentTab == 'book'}"><b></c:if>Quản lý Sách<c:if test="${currentTab == 'book'}"></b></c:if>
                </a> | 
                    <a href="${pageContext.request.contextPath}/product?tab=stationery">
                <c:if test="${currentTab == 'stationery'}"><b></c:if>Quản lý Văn phòng phẩm<c:if test="${currentTab == 'stationery'}"></b></c:if>
                </a>
            </div>

                <form action="${pageContext.request.contextPath}/product" method="GET" style="margin-bottom: 20px;">
            <input type="hidden" name="tab" value="${currentTab}">
            <input type="text" name="keyword" value="${currentKeyword}" placeholder="Nhập tên sản phẩm để tìm kiếm...">
            <button type="submit">Tìm kiếm</button>
        </form>

        <table border="1" cellpadding="8" cellspacing="0" style="width: 100%; text-align: left;">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty productList}">
                        <c:forEach var="product" items="${productList}">
                            <tr>
                                <td>${product.productId}</td>
                                <td>${product.name}</td>
                                <td>${product.price}</td>
                                <td>${product.stockQuantity}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.status == 1}">Đang hoạt động</c:when>
                                        <c:otherwise>Ngừng bán</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${product.type}</td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" style="text-align: center;">Không có sản phẩm nào khớp với tìm kiếm!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <br>

        <c:if test="${totalPages > 1}">
            <div>
                Trang: 
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <strong style="margin: 0 5px;">[${i}]</strong>
                        </c:when>
                        <c:otherwise>
                            <a style="margin: 0 5px;" href="${pageContext.request.contextPath}/product?tab=${currentTab}&keyword=${currentKeyword}&page=${i}">[${i}]</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>

    </body>
</html>