<%-- 
    Document   : home
    Created on : Feb 10, 2026
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Home Page - BookVerse</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="styles/home.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>

        <div class="container py-5">
            <div class="row mb-4 align-items-center">
                <div class="col-md-6">
                    <h1 class="display-6 fw-bold text-primary logo">
                        <i class="fa-solid fa-book-open"></i> BookVerse
                    </h1>
                </div>
                <div class="col-md-6">
                    <form action="home" method="get" class="d-flex">
                        <input type="hidden" name="action" value="search"/>
                        <div class="input-group">
                            <input type="text" class="form-control" name="keyword" 
                                   value="${searchKeyword}" placeholder="Search products...">
                            <button class="btn btn-primary" type="submit">
                                <i class="fa-solid fa-search"></i> Search
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <c:if test="${empty requestScope.PRODUCT_LIST}">
                <div class="alert alert-warning text-center" role="alert">
                    <i class="fa-solid fa-box-open fa-2x mb-3"></i><br>
                    No products found! Please try a different keyword.
                    <br>
                    <a href="home" class="btn btn-outline-dark mt-2">View all</a>
                </div>
            </c:if>

            <c:if test="${not empty requestScope.PRODUCT_LIST}">
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                    <c:forEach var="p" items="${requestScope.PRODUCT_LIST}">
                        <div class="col">
                            <div class="card product-card h-100">

                                <div class="product-img-wrapper">
                                    <a href="home?action=detail&id=${p.productId}">
                                        <img src="${p.imageUrl}" class="card-img-top" alt="${p.name}">
                                    </a>
                                </div>

                                <div class="card-body">
                                    <a href="home?action=detail&id=${p.productId}" class="product-title" title="${p.name}">
                                        ${p.name}
                                    </a>

                                    <div class="product-meta">
                                        <div class="rating">
                                            <span>
                                                <fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1"/>
                                            </span>
                                            <i class="fa-solid fa-star"></i>
                                        </div>
                                        <div class="sold">
                                            Sold: <span>${p.soldQuantity}</span>
                                        </div>
                                    </div>

                                    <div class="product-price">
                                        <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                    </div>

                                    <div class="mt-auto">
                                        <form action="cart" method="post">
                                            <input type="hidden" name="action" value="add"/>
                                            <input type="hidden" name="productId" value="${p.productId}"/>
                                            <button type="submit" class="btn btn-outline-primary btn-add-cart">
                                                <i class="fa-solid fa-cart-plus"></i> Add to Cart
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div> 

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>