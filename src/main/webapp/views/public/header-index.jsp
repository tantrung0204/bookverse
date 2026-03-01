<%-- 
    Document   : header-index.jsp
    Created on : Feb 28, 2026, 2:59:57 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="bookverse-header">
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <a class="navbar-brand me-5" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Bookverse Logo" height="75" class="d-inline-block align-text-top">
            </a>

            <div class="collapse navbar-collapse justify-content-between" id="navbarSupportedContent">

                <form class="d-flex mx-lg-auto flex-grow-1 header-search-form my-3 my-lg-0" action="${pageContext.request.contextPath}/search" method="get">
                    <div class="header-search-group position-relative w-100">
                        <input class="form-control" type="search" name="query" placeholder="Search books, authors..." aria-label="Search">
                        <button class="btn-search-icon" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </form>

                <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                    <c:choose>
                        <%-- CASE 1: NOT LOGGED IN --%>
                        <c:when test="${empty sessionScope.user}">
                            <li class="nav-item me-3 d-flex align-items-center">
                                <a class="btn-brand-rounded px-4 py-2" href="${pageContext.request.contextPath}/login">Sign In</a>
                            </li>
                            <li class="nav-item d-flex align-items-center">
                                <a class="btn-outline-brand-rounded px-4 py-2" href="${pageContext.request.contextPath}/register">Sign Up</a>
                            </li>
                        </c:when>

                        <%-- CASE 2: USER LOGGED IN --%>
                        <c:otherwise>
                            <li class="nav-item me-3">
                                <a class="nav-link icon-link position-relative" href="#" title="Notifications">
                                    <i class="far fa-bell fa-lg"></i>
                                    <span class="position-absolute top-10 start-100 translate-middle p-1 bg-danger border border-light rounded-circle badge-dot"></span>
                                </a>
                            </li>

                            <li class="nav-item me-4">
                                <a class="nav-link icon-link position-relative" href="#" title="Shopping Cart">
                                    <i class="fas fa-shopping-cart fa-lg"></i>
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-brand-accent" style="font-size: 0.7rem;">
                                        3
                                    </span>
                                </a>
                            </li>

                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle d-flex align-items-center user-dropdown-link" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.user.avatarUrl}">
                                            <img src="${sessionScope.user.avatarUrl}" alt="${sessionScope.user.username}" width="35" height="35" class="rounded-circle me-2 user-avatar border border-brand">
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-user-circle fa-2x me-2 brand-color"></i>
                                        </c:otherwise>
                                    </c:choose>

                                    <span class="fw-bold brand-color d-none d-sm-inline-block">
                                        <c:out value="${sessionScope.user.username}" default="User" />
                                    </span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end shadow brand-dropdown" aria-labelledby="navbarDropdown">
                                    <li><a class="dropdown-item" href="#"><i class="far fa-user me-2"></i> Profile</a></li>
                                    <li><a class="dropdown-item" href="#"><i class="fas fa-receipt me-2"></i> My Orders</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt me-2"></i> Sign Out</a></li>
                                </ul>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
</header>
