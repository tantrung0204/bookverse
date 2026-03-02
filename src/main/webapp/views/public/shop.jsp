<%-- 
    Document   : shop
    Created on : Mar 1, 2026, 4:05:16 PM
    Author     : TrungNT - CE200064
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shop - Bookverse</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" type="text/css"/>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/shop.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
    </head>

    <body>

        <jsp:include page="header-index.jsp" />
        <jsp:include page="navbar.jsp">
            <jsp:param name="activePage" value="${selectedType}"/>
        </jsp:include>

        <div class="shop-wrapper container">

            <aside class="col-md-3 filter-column">
                <form id="filterForm" action="shop" method="GET">
                    <input type="hidden" name="type" value="${selectedType}" />
                    <input type="hidden" id="pageInput" name="page" value="1" />
                    <c:if test="${not empty searchKeyword}">
                        <input type="hidden" name="keyword" value="${searchKeyword}" />
                    </c:if>

                    <div class="filter-group mb-4">
                        <h5 class="filter-title mb-2">Sort By Price</h5>
                        <select name="sort" class="form-select filter-select">
                            <option value="">Default</option>
                            <option value="asc" ${selectedSort == 'asc' ? 'selected' : ''}>Low to High</option>
                            <option value="desc" ${selectedSort == 'desc' ? 'selected' : ''}>High to Low</option>
                        </select>
                    </div>

                    <c:if test="${selectedType == 'book'}">
                        <div class="filter-group mb-4">
                            <h5 class="filter-title mb-2">Genres</h5>
                            <div class="genre-scroll-box"> 
                                <c:forEach items="${allGenres}" var="g">
                                    <label class="checkbox-item">
                                        <input type="checkbox" name="genre" value="${g.genreId}" 
                                               <c:if test="${selectedGenres.contains(g.genreId)}">checked</c:if>
                                                   > 
                                        ${g.genreName}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                    <button type="button" onclick="submitFilter()" class="btn btn-filter-apply w-100 text-white mt-3">Apply Filter</button>
                </form>
            </aside>

            <section class="col-md-9 product-column">
                <div class="shop-header">
                    <h2 class="shop-title">
                        <c:choose>
                            <c:when test="${selectedType == 'book'}">All Books</c:when>
                            <c:when test="${selectedType == 'stationery'}">All Stationery</c:when>
                            <c:otherwise>All Products</c:otherwise>
                        </c:choose>
                    </h2>
                    <span class="shop-item-count">
                        (${totalProducts} items found)
                    </span>
                </div>

                <c:if test="${not empty searchKeyword}">
                    <div class="alert alert-light border mb-3 py-2 search-alert">
                        Search results for: "<strong>${searchKeyword}</strong>"
                    </div>
                </c:if>

                <div class="grid-container">
                    <c:forEach items="${productList}" var="p">
                        <a href="product-detail?id=${p.productId}" class="custom-card">
                            <div class="card-img-wrapper">
                                <img src="${p.imageUrl}" alt="${p.name}"
                                    onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';"/>
                            </div>
                            <div class="card-body-custom">
                                <div class="product-title" title="${p.name}">${p.name}</div>
                                <div class="product-price">
                                    <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                </div>
                                <div class="product-meta">
                                    <span>
                                        <span class="rating-star">★</span> 
                                        <fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1"/>
                                    </span>
                                    <span>Sold: ${p.soldQuantity}</span>
                                </div>
                                <c:if test="${p.type == 'Stationery' && not empty p.color}">
                                    <div class="product-meta mt-1 small text-muted border-0 pt-0">
                                        Color: ${p.color}
                                    </div>
                                </c:if>
                            </div>
                        </a>
                    </c:forEach>
                </div>

                <c:if test="${empty productList}">
                    <div class="text-center py-5">
                        <p class="no-products-text">
                            No products found matching your criteria.
                        </p>
                    </div>
                </c:if>

                <c:if test="${totalPages > 1}">
                    <div class="pagination mt-4 d-flex justify-content-center gap-1">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="javascript:void(0);" 
                               class="page-link-custom ${currentPage == i ? 'active' : ''}" 
                               onclick="goToPage(${i})">${i}</a>
                        </c:forEach>
                    </div>
                </c:if>
            </section>

        </div>

        <jsp:include page="footer-index.jsp" />

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
        <script>
                                   function goToPage(pageNum) {
                                       document.getElementById('pageInput').value = pageNum;
                                       document.getElementById('filterForm').submit();
                                   }

                                   function submitFilter() {
                                       document.getElementById('pageInput').value = 1;
                                       document.getElementById('filterForm').submit();
                                   }
        </script>
    </body>
</html>