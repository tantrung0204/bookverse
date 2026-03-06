<%-- 
    Document   : product-list.jsp
    Created on : 24 Feb 2026, 16:55:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Products</p>
        <p class="subtitle">Create and manage books and stationery for your library</p>
    </div>

    <div class="content-card">

        <div class="tab-container">
            <a href="${pageContext.request.contextPath}/product?tab=book" 
               class="tab-item ${currentTab == 'book' ? 'active' : ''}">
                <i class="bi bi-book me-1"></i> Books
            </a>
            <a href="${pageContext.request.contextPath}/product?tab=stationery" 
               class="tab-item ${currentTab == 'stationery' ? 'active' : ''}">
                <i class="bi bi-pencil-square me-1"></i> Stationery
            </a>
        </div>

        <div class="toolbar">
            <button type="button" class="btn-add" onclick="openCreateProductPopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Product
            </button>

            <form action="${pageContext.request.contextPath}/product" method="GET">
                <input type="hidden" name="tab" value="${currentTab}">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search products..." value="${currentKeyword}">
                </div>
                <button type="submit" hidden></button>
            </form>
        </div>

        <table class="custom-table">
            <thead>
                <tr>
                    <th width="10%">ID</th>
                    <th width="25%">Name</th>
                    <th width="15%">Price</th>
                    <th width="10%">Quantity</th>
                    <th width="10%">Type</th>
                    <th width="15%">Status</th>
                    <th width="15%">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty productList}">
                        <c:forEach var="product" items="${productList}">
                            <tr>
                                <td><strong>${product.productId}</strong></td>
                                <td>${product.name}</td>
                                <td><fmt:formatNumber value="${product.price}" pattern="#,###"/> đ</td>
                                <td>${product.stockQuantity}</td>
                                <td>${product.type}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.status == 1}">
                                            <span class="badge-status badge-active">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-inactive">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail" title="View Detail" 
                                                onclick="openProductDetail('${product.productId}')">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit" title="Edit" 
                                                onclick="openProductEdit('${product.productId}')">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/product" method="post" style="display:inline;" 
                                              onsubmit="return confirm('Are you sure you want to delete product:\n${product.name} (ID: ${product.productId})')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${product.productId}">
                                            <input type="hidden" name="tab" value="${currentTab}">
                                            <button type="submit" class="btn-action btn-delete" title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7">
                                <div style="text-align:center; padding: 40px; color: #999;">
                                    <i class="bi bi-inbox" style="font-size: 40px;"></i>
                                    <p class="mt-2">No products found matching your search.</p>
                                </div>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <c:if test="${totalPages > 1}">
            <div class="pagination-container">
                <span style="color: #888; font-size: 14px;">Page:</span>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="page-link active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link" href="${pageContext.request.contextPath}/product?tab=${currentTab}&keyword=${currentKeyword}&page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>

    </div>
</div>

<script>
    // Placeholder functions for the action buttons. 
    // You can implement the popups similarly to how you did in category-list.jsp
    function openCreateProductPopup() {
        console.log("Open Create Product Popup");
        // Logic to open create modal
    }

    function openProductDetail(id) {
        console.log("View detail for product ID:", id);
        // Logic to fetch and show details
    }

    function openProductEdit(id) {
        console.log("Edit product ID:", id);
        // Logic to open edit modal and populate data
    }
</script>