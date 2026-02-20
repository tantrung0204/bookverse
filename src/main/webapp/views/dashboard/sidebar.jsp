<%-- 
    Document   : side
    Created on : Feb 19, 2026, 9:34:01 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/sidebar.css">

<aside class="sidebar">
    <div class="sidebar-brand">
        <div class="brand-icon">
            <i class="bi bi-journal-text"></i>
        </div>
        <div class="brand-text">
            <h5>Bookverse</h5>
            <span>Admin Panel</span>
        </div>
    </div>

    <ul class="sidebar-menu">
        <li class="sidebar-item">
            <a href="#" class="sidebar-link ${activeMenu == 'dashboard'? 'active':''}">
                <i class="bi bi-graph-up"></i> Dashboard
            </a>
        </li>

        <div class="menu-section">Management</div>

        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/customer" 
               class="sidebar-link ${activeMenu == 'customer'? 'active':''}">
                <i class="bi bi-people"></i> Customers
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'staff'? 'active':''}">
                <i class="bi bi-person-badge"></i> Staff
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'product'? 'active':''}">
                <i class="bi bi-book"></i> Products
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/category" 
               class="sidebar-link ${activeMenu == 'category'? 'active':''}">
                <i class="bi bi-tags"></i> Categories
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'order'? 'active':''}">
                <i class="bi bi-cart2"></i> Orders
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'feedback'? 'active':''}">
                <i class="bi bi-star"></i> Feedback
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'voucher'? 'active':''}">
                <i class="bi bi-percent"></i> Vouchers
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'notification'? 'active':''}">
                <i class="bi bi-bell"></i> Notifications
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" 
               class="sidebar-link ${activeMenu == 'statistic'? 'active':''}">
                <i class="bi bi-bar-chart-steps"></i> Statistics
            </a>
        </li>
    </ul>
</aside>
