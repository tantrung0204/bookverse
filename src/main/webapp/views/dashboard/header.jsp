<%-- 
    Document   : header.jsp
    Created on : Feb 19, 2026, 9:07:38 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">

<header class="dashboard-header d-flex justify-content-between align-items-center">
    
    <div class="header-left">
        <p class="header-title mb-1">Dashboard</p>
        <!--<p class="header-subtitle">Create and manage categories for your library</p>-->
    </div>

    <div class="header-right dropdown">
        <div class="user-profile-btn d-flex align-items-center gap-2 dropdown-toggle" 
             id="profileDropdown" 
             data-bs-toggle="dropdown" 
             aria-expanded="false">
            <img src="" 
                 alt="Avatar" 
                 class="header-avatar rounded-circle" 
            <span class="fw-medium text-dark">
                abc
            </span>
        </div>
        
        <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2" aria-labelledby="profileDropdown">
            <li>
                <a class="dropdown-item py-2" href="#">
                    <i class="bi bi-person me-2"></i> View Profile
                </a>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li>
                <a class="dropdown-item py-2 text-danger" href="#">
                    <i class="bi bi-box-arrow-right me-2"></i> Logout
                </a>
            </li>
        </ul>
    </div>
    
</header>
