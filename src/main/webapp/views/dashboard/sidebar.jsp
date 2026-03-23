<%-- Document : side Created on : Feb 19, 2026, 9:34:01 PM Author : TrungNT - CE200064 --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/sidebar.css">

<aside class="sidebar">
    <div class="sidebar-brand">
        <div class="brand-icon">
            <i class="bi bi-journal-text"></i>
        </div>
        <div class="brand-text">
            <h5>Bookverse</h5>
            <span>${fn:toUpperCase(fn:substring(sessionScope.role, 0,
                    1))}${fn:substring(sessionScope.role, 1, fn:length(sessionScope.role))} Panel</span>
        </div>
    </div>

    <ul class="sidebar-menu">
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/dashboard"
               class="sidebar-link ${activeMenu == 'dashboard'? 'active':''}">
                <i class="bi bi-graph-up"></i> Dashboard
            </a>
        </li>

        <div class="menu-section">Management</div>

        <%-- Customer: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/customer"
                   class="sidebar-link ${activeMenu == 'customer'? 'active':''}">
                    <i class="bi bi-people"></i> Customers
                </a>
            </li>
        </c:if>

        <%-- Staff: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/staff"
                   class="sidebar-link ${activeMenu == 'staff'? 'active':''}">
                    <i class="bi bi-person-badge"></i> Staff
                </a>
            </li>
        </c:if>

        <%-- Products: admin, seller, warehouse --%>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/dashboard/product"
               class="sidebar-link ${activeMenu == 'product'? 'active':''}">
                <i class="bi bi-book"></i> Products
            </a>
        </li>

        <%-- Categories: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/category"
                   class="sidebar-link ${activeMenu == 'category'? 'active':''}">
                    <i class="bi bi-tags"></i> Categories
                </a>
            </li>
        </c:if>

        <%-- Orders: admin, seller --%>
        <c:if
            test="${sessionScope.role == 'admin' || sessionScope.role == 'seller'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/order"
                   class="sidebar-link ${activeMenu == 'order'? 'active':''}">
                    <i class="bi bi-cart2"></i> Orders
                </a>
            </li>
        </c:if>

        <%-- Feedback: admin, seller --%>
        <c:if
            test="${sessionScope.role == 'admin' || sessionScope.role == 'seller'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/feedback"
                   class="sidebar-link ${activeMenu == 'feedback'? 'active':''}">
                    <i class="bi bi-star"></i> Feedback
                </a>
            </li>
        </c:if>

        <%-- Vouchers: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/voucher"
                   class="sidebar-link ${activeMenu == 'voucher'? 'active':''}">
                    <i class="bi bi-percent"></i> Vouchers
                </a>
            </li>
        </c:if>

        <%-- Notifications: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/notification"
                   class="sidebar-link ${activeMenu == 'notification'? 'active':''}">
                    <i class="bi bi-bell"></i> Notifications
                </a>
            </li>
        </c:if>

        <%-- Genres: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/genre"
                   class="sidebar-link ${activeMenu == 'genre'? 'active':''}">
                    <i class="bi bi-tags"></i> Genres
                </a>
            </li>
        </c:if>

        <%-- Authors: admin only --%>
        <c:if test="${sessionScope.role == 'admin'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/author"
                   class="sidebar-link ${activeMenu == 'author'? 'active':''}">
                    <i class="bi bi-person"></i> Authors
                </a>
            </li>
        </c:if>

        <%-- Inventories: admin, warehouse --%>
        <c:if
            test="${sessionScope.role == 'admin' || sessionScope.role == 'warehouse'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/inventory"
                   class="sidebar-link ${activeMenu == 'inventory'? 'active':''}">
                    <i class="bi bi-box-seam"></i>
                    Inventories
                </a>
            </li>
        </c:if>

        <%-- Suppliers: admin, warehouse --%>
        <c:if
            test="${sessionScope.role == 'admin' || sessionScope.role == 'warehouse'}">
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/dashboard/supplier"
                   class="sidebar-link ${activeMenu == 'supplier'? 'active':''}">
                    <i class="bi bi-box-fill"></i>
                    Suppliers
                </a>
            </li>
        </c:if>


        <%-- Statistics: admin, seller, warehouse --%>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/dashboard/statistics"
               class="sidebar-link ${activeMenu == 'statistics'? 'active':''}">
                <i class="bi bi-bar-chart-steps"></i> Statistics
            </a>
        </li>
    </ul>
</aside>