<%-- 
    Document   : navbar
    Created on : Feb 28, 2026, 4:24:06 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="bookverse-navbar navbar navbar-expand-lg">
    <div class="container">

        <button class="navbar-toggler text-white" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <i class="fas fa-bars me-2"></i> Menu
        </button>

        <div class="collapse navbar-collapse justify-content-center" id="mainNavbar">
            <ul class="navbar-nav bookverse-nav-list">

                <li class="nav-item">
                    <a class="nav-link ${param.activePage == 'home' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/home">Home</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link ${param.activePage == 'book' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/shop?type=book">Books</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link ${param.activePage == 'stationery' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/shop?type=stationery">Stationery</a>
                </li>

                <li class="nav-item dropdown position-static">
                    <a class="nav-link dropdown-toggle" href="" id="categoriesDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Categories
                    </a>

                    <ul class="dropdown-menu brand-dropdown-menu" aria-labelledby="categoriesDropdown">
                        <c:forEach items="${categories}" var="cat">
                            <li>
                                <a class="dropdown-item" 
                                   href="${pageContext.request.contextPath}/shop?type=all&categoryId=${cat.categoryId}">
                                    ${cat.categoryName}
                                </a>
                            </li>
                            </c:forEach>
                    </ul>
                </li>

            </ul>
        </div>

    </div>
</nav>
