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
            <a href="#" class="sidebar-link active">
                <i class="bi bi-graph-up"></i> Dashboard
            </a>
        </li>

        <div class="menu-section">Management</div>

        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-people"></i> Customer
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-person-badge"></i> Staff
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-book"></i> Books
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-collection"></i> Series
            </a>
        </li>
        <li class="sidebar-item">
            <a href="category" class="sidebar-link active">
                <i class="bi bi-tags"></i> Categories
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-cart2"></i> Orders
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-star"></i> Reviews
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-percent"></i> Promotions
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-bell"></i> Notifications
            </a>
        </li>
        <li class="sidebar-item">
            <a href="#" class="sidebar-link">
                <i class="bi bi-bar-chart-steps"></i> Statistics
            </a>
        </li>
    </ul>
</aside>
