<%-- 
    Document   : product-list.jsp
    Created on : 24 Feb 2026, 16:55:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Products</p>
        <p class="subtitle">Create and manage product for your library</p>
    </div>


    <div class="content-card">

        <div class="toolbar">
            <div class="filter-buttons">
                <a href="${pageContext.request.contextPath}/product?action=list&type=book"
                   class="btn-filter ${type == null || type == 'book' ? 'active' : ''}">
                    📖 <span>Books</span>
                </a>

                <a href="${pageContext.request.contextPath}/product?action=list&type=stationery"
                   class="btn-filter ${type == 'stationery' ? 'active' : ''}">
                    ✏️ <span>Stationery</span>
                </a>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty products}">
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success">
                        ${successMsg}
                    </div>
                    <c:remove var="successMsg" scope="session" />
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-error">
                        ${errorMsg}
                    </div>
                    <c:remove var="errorMsg" scope="session" />
                </c:if>
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="25%">Name</th>
                            <th width="15%">Price</th>
                            <th width="10%">Stock</th>
                            <th width="15%">Type</th>
                            <th width="15%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td><strong>${p.productId}</strong></td>
                                <td>${p.name}</td>
                                <td>${p.price}</td>
                                <td>${p.stockQuantity}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.type == 'Book'}">
                                            <span class="badge-status badge-book">Book</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-stationery">Stationery</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail"
                                                title="View Detail"  onclick="openDetailPopup('${p.productId}')">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit" title="Edit"
                                                onclick="openEditPopup(
                                                        '${c.categoryId}',
                                                        '${c.categoryName}',
                                                        '${c.descriptionText}',
                                                        '${c.status}'
                                                        )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/category"
                                              method="post" style="display:inline;"
                                              onsubmit="return confirmDelete('${c.categoryId}', '${c.categoryName}')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${c.categoryId}">
                                            <button type="submit" class="btn-action btn-delete"
                                                    title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="text-align:center; padding: 40px; color: #999;">
                    <i class="bi bi-inbox" style="font-size: 40px;"></i>
                    <p class="mt-2">No categories found.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty deleteError}">
            <div class="alert alert-danger mt-3 ">${deleteError}</div>
            <c:remove var="deleteError" scope="session" />
        </c:if>
    </div>
</div>


