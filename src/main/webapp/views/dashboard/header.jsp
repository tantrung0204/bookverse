<%-- Document : header.jsp Created on : Feb 19, 2026, 9:07:38 PM Author : TrungNT - CE200064 --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">

<header class="dashboard-header d-flex justify-content-between align-items-center">

    <div class="header-left">
        <p class="header-title mb-1">Dashboard</p>
    </div>

    <div class="header-right dropdown">
        <div class="user-profile-btn d-flex align-items-center gap-2 dropdown-toggle user-dropdown-link" id="profileDropdown"
             data-bs-toggle="dropdown" aria-expanded="false" style="border: none; background: transparent;">
            <c:choose>
                <c:when test="${not empty sessionScope.user.profileImageUrl}">
                    <img src="${sessionScope.user.profileImageUrl}" alt="${sessionScope.user.username}" width="35" height="35" 
                         class="rounded-circle header-avatar"
                         onerror="this.src='${pageContext.request.contextPath}/assets/images/default-avt.jpg';">
                </c:when>
                <c:otherwise>
                    <i class="fas fa-user-circle fa-2x brand-color"></i>
                </c:otherwise>
            </c:choose>
            <span class="fw-bold d-none d-sm-inline-block">
                <c:out value="${sessionScope.user.username}" default="Staff" />
            </span>
        </div>

        <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2 brand-dropdown" aria-labelledby="profileDropdown">
            <li>
                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/profile">
                    <i class="far fa-user me-2"></i> Profile
                </a>
            </li>
            <li>
                <hr class="dropdown-divider">
            </li>
            <li>
                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/signout">
                    <i class="fas fa-sign-out-alt me-2"></i> Sign Out
                </a>
            </li>
        </ul>
    </div>

</header>