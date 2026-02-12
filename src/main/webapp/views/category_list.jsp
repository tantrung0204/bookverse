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
        <meta charset="UTF-8">
        <title>Category List</title>
    </head>

    <body>

        <h2>Category List</h2>
        <a class="text-decoration-none btn btn-primary" href="${pageContext.request.contextPath}/category?view=create">Thêm thể loại
        </a>
        <form method="get" action="category">
            <input type="text"
                   name="keyword"
                   placeholder="Search category..."
                   value="${keyword}">
            <button type="submit">Tìm kiếm</button>
        </form>

        <c:choose>        
            <c:when test="${not empty message}">
                <p style="color:red; font-weight:bold;">
                    ${message}
                </p>
            </c:when>
            <c:when test="${not empty category_list}">
                <table >
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Category Name</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${category_list}">
                            <tr>
                                <td>${c.categoryId}</td>
                                <td>${c.categoryName}</td>
                                <td>${c.descriptionText}</td>
                                <td class="actions" > 
                                    <div style="display: flex; justify-content: center">
                                        <a href="${pageContext.request.contextPath}/category?view=view_detail&id=${c.categoryId}" > Detail
                                            <i class="fa-solid fa-eye"></i>
                                        </a>
                                    </div>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/category?view=add&id=${c.categoryId}" >  
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p style="color:gray;">Empty List</p>
            </c:otherwise>

        </c:choose>

    </body>
</html>
