<%-- 
    Document   : shop
    Created on : Mar 1, 2026, 4:05:16 PM
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

        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/shop-style.css"> 
    </head>

    <body>

        <jsp:include page="header-index.jsp" />
        <jsp:include page="navbar.jsp">
            <jsp:param name="activePage" value="${selectedType}"/>
        </jsp:include>

        <div class="shop-wrapper container">

            <aside class="filter-column">
                <form id="filterForm" action="shop" method="GET">
                    <input type="hidden" name="type" value="${selectedType}" />
                    <input type="hidden" id="pageInput" name="page" value="1" />

                    <div class="filter-group">
                        <h4>Sort By Price</h4>
                        <select name="sort" class="form-select" style="font-family: 'Playfair Display', serif;">
                            <option value="">Default</option>
                            <option value="asc" ${selectedSort == 'asc' ? 'selected' : ''}>Low to High</option>
                            <option value="desc" ${selectedSort == 'desc' ? 'selected' : ''}>High to Low</option>
                        </select>
                    </div>

                    <c:if test="${selectedType == 'book'}">
                        <div class="filter-group">
                            <h4>Genres</h4>
                            <div style="max-height: 300px; overflow-y: auto;"> <c:forEach items="${allGenres}" var="g">
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

                    <button type="submit" class="btn btn-filter-apply w-100 text-white mt-3">Apply Filter</button>
                </form>
            </aside>

            <section class="product-column">
                <h2 class="shop-title">
                    ${selectedType == 'book' ? 'All Books' : 'All Stationery'}
                    <span style="font-size: 0.6em; color: #777; font-weight: 400;">(${totalProducts} items found)</span>
                </h2>

                <div class="grid-container">
                    <c:forEach items="${productList}" var="p">
                        <a href="product-detail?id=${p.productId}" class="custom-card">
                            <div class="card-img-wrapper">
                                <img src="${p.imageUrl}" alt="${p.name}" />
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
                            </div>
                        </a>
                    </c:forEach>
                </div>
                <c:if test="${empty productList}">
                    <div class="col-12 text-center py-5">
                        <p class="text-muted">No products found matching your criteria.</p>
                    </div>
                </c:if>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
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
                                       // Set giá trị cho input hidden 'page'
                                       document.getElementById('pageInput').value = pageNum;
                                       // Submit form bộ lọc để giữ lại các filter đang chọn
                                       document.getElementById('filterForm').submit();
                                   }
        </script>
    </body>
</html>