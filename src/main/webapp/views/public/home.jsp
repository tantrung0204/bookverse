<%-- 
    Document   : home
    Created on : Feb 28, 2026, 3:13:06 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home - Bookverse</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css"  type="text/css"/>
    </head>
    <body>
        <jsp:include page="header-index.jsp" />
        <jsp:include page="navbar.jsp">
            <jsp:param name="activePage" value="home"/>
        </jsp:include>

        <div class="main-content">

            <h2 class="section-title">Best Selling Books</h2>

            <div class="product-slider-wrapper">
                <button class="nav-btn prev-btn" onclick="scrollList('book-list', -300)">
                    <i class="fa-solid fa-chevron-left"></i>
                </button>

                <div class="product-scroll-container" id="book-list">
                    <c:forEach items="${topBooks}" var="b">
                        <a href="product-detail?id=${b.productId}" class="custom-card">
                            <div class="card-img-wrapper">
                                <img src="${b.imageUrl}" alt="${b.name}"
                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';"/>
                            </div>
                            <div class="card-body-custom">
                                <div class="product-title" title="${b.name}">${b.name}</div>
                                <div class="product-price">
                                    <fmt:formatNumber value="${b.price}" pattern="#,###"/> đ
                                </div>
                                <div class="product-meta">
                                    <span>
                                        <span class="rating-star">★</span> 
                                        <fmt:formatNumber value="${b.averageRating}" maxFractionDigits="1"/>
                                    </span>
                                    <span>Sold: ${b.soldQuantity}</span>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>

                <button class="nav-btn next-btn" onclick="scrollList('book-list', 300)">
                    <i class="fa-solid fa-chevron-right"></i>
                </button>
            </div>

            <div class="view-more-container">
                <a href="shop?type=book" class="btn-view-more">View All Books</a>
            </div>


            <hr class="section-divider"/>


            <h2 class="section-title">Best Selling Stationery</h2>

            <div class="product-slider-wrapper">
                <button class="nav-btn prev-btn" onclick="scrollList('stationery-list', -300)">
                    <i class="fa-solid fa-chevron-left"></i>
                </button>

                <div class="product-scroll-container" id="stationery-list">
                    <c:forEach items="${topStationery}" var="s">
                        <a href="product-detail?id=${s.productId}" class="custom-card">
                            <div class="card-img-wrapper">
                                <img src="${s.imageUrl}" alt="${s.name}" 
                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';"/>
                            </div>
                            <div class="card-body-custom">
                                <div class="product-title" title="${s.name}">${s.name}</div>
                                <div class="product-price">
                                    <fmt:formatNumber value="${s.price}" pattern="#,###"/> đ
                                </div>
                                <div class="product-meta">
                                    <span>
                                        <span class="rating-star">★</span> 
                                        <fmt:formatNumber value="${s.averageRating}" maxFractionDigits="1"/>
                                    </span>
                                    <span>Sold: ${s.soldQuantity}</span>
                                </div>
                                <c:if test="${not empty s.color}">
                                    <div class="product-meta" style="margin-top: 5px; font-size: 0.8rem;">
                                        Color: ${s.color}
                                    </div>
                                </c:if>
                            </div>
                        </a>
                    </c:forEach>
                </div>

                <button class="nav-btn next-btn" onclick="scrollList('stationery-list', 300)">
                    <i class="fa-solid fa-chevron-right"></i>
                </button>
            </div>

            <div class="view-more-container">
                <a href="shop?type=stationery" class="btn-view-more">View All Stationery</a>
            </div>

        </div>

        <jsp:include page="footer-index.jsp" />

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>

        <script>
                    function scrollList(elementId, amount) {
                        const container = document.getElementById(elementId);
                        if (container) {
                            container.scrollBy({
                                left: amount,
                                behavior: 'smooth'
                            });
                        }
                    }
        </script>
    </body>
</html>