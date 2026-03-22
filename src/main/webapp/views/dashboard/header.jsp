<%-- Document : header.jsp Created on : Feb 19, 2026, 9:07:38 PM Author : TrungNT - CE200064 --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">

<header class="dashboard-header d-flex justify-content-between align-items-center">

    <div class="header-left">
        <p class="header-title mb-1">Dashboard</p>
    </div>

    <div class="header-right dropdown">
        <a class="nav-link dropdown-toggle d-flex align-items-center user-dropdown-link" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <c:choose>
                <c:when test="${not empty sessionScope.user.profileImageUrl}">
                    <img src="${sessionScope.user.profileImageUrl}" alt="${sessionScope.user.username}" width="35" height="35" 
                         class="rounded-circle me-2 user-avatar border border-brand"
                         onerror="this.src='${pageContext.request.contextPath}/assets/images/default-avt.jpg';">
                </c:when>
                <c:otherwise>
                    <i class="fas fa-user-circle fa-2x me-2 brand-color"></i>
                </c:otherwise>
            </c:choose>

            <span class="fw-bold brand-color d-none d-sm-inline-block">
                <c:out value="${sessionScope.user.username}" default="User" />
            </span>
        </a>

        <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2" aria-labelledby="profileDropdown">
            <li>
                <a class="dropdown-item py-2" href="#">
                    <i class="bi bi-person me-2"></i> View Profile
                </a>
            </li>
            <li>
                <hr class="dropdown-divider">
            </li>
            <li>
                <a class="dropdown-item py-2 text-danger" href="${pageContext.request.contextPath}/signout">
                    <i class="bi bi-box-arrow-right me-2"></i> Logout
                </a>
            </li>
        </ul>
    </div>

</header>