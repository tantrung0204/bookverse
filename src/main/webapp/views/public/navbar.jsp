<%-- 
    Document   : navbar
    Created on : Feb 28, 2026, 4:24:06 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="bookverse-navbar navbar navbar-expand-lg">
    <div class="container">
        
        <button class="navbar-toggler text-white" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <i class="fas fa-bars me-2"></i> Menu
        </button>

        <div class="collapse navbar-collapse justify-content-center" id="mainNavbar">
            <ul class="navbar-nav bookverse-nav-list">
                
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Home</a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/books">Book</a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/stationery">Stationery</a>
                </li>
                
                <li class="nav-item dropdown position-static">
                    <a class="nav-link dropdown-toggle" href="#" id="categoriesDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Categories
                    </a>
                    
                    <ul class="dropdown-menu brand-dropdown-menu" aria-labelledby="categoriesDropdown">
                        <li><a class="dropdown-item" href="#">Fiction & Literature</a></li>
                        <li><a class="dropdown-item" href="#">Non-Fiction</a></li>
                        <li><a class="dropdown-item" href="#">Mystery & Thriller</a></li>
                        <li><a class="dropdown-item" href="#">Science Fiction</a></li>
                        <li><a class="dropdown-item" href="#">Fantasy</a></li>
                        <li><a class="dropdown-item" href="#">Romance</a></li>
                        <li><a class="dropdown-item" href="#">Historical</a></li>
                        <li><a class="dropdown-item" href="#">Biography & Memoir</a></li>
                        <li><a class="dropdown-item" href="#">Children's Books</a></li>
                        <li><a class="dropdown-item" href="#">Art & Photography</a></li>
                        </ul>
                </li>
                
            </ul>
        </div>
        
    </div>
</nav>
