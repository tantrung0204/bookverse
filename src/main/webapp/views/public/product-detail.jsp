<%-- 
    Document   : product-detail
    Created on : Feb 10, 2026, 8:42:15 PM
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
        <title>${product.name} - Bookverse</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" type="text/css"/>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <style>
            body {
                background-color: #f6f4e6;
                color: #333;
                font-family: 'Segoe UI', sans-serif;
            }
            .product-detail-wrapper {
                padding: 40px 0;
            }

            /* Cột Ảnh */
            .main-img-container {
                border: 1px solid #eee;
                border-radius: 8px;
                padding: 20px;
                text-align: center;
                background: #fff;
                position: relative;
            }
            .main-img {
                max-width: 100%;
                height: auto;
                max-height: 500px;
                object-fit: contain;
            }
            .badge-type {
                position: absolute;
                top: 15px;
                left: 15px;
                padding: 5px 12px;
                border-radius: 4px;
                color: #fff;
                font-weight: 600;
                font-size: 0.85rem;
            }
            .badge-book {
                background-color: #3498db;
            }
            .badge-stationery {
                background-color: #e67e22;
            }

            /* Cột Thông tin */
            .product-info h1 {
                font-size: 1.8rem;
                font-weight: 700;
                color: #2c3e50;
                margin-bottom: 10px;
            }
            .product-price {
                font-size: 1.6rem;
                color: #d35400;
                font-weight: bold;
                margin: 15px 0;
            }

            .product-meta {
                color: #555;
                font-size: 0.95rem;
                margin-bottom: 8px;
                border-bottom: 1px dashed #eee;
                padding-bottom: 8px;
            }
            .product-meta:last-child {
                border-bottom: none;
            }
            .meta-label {
                font-weight: 600;
                color: #333;
                min-width: 120px;
                display: inline-block;
            }

            .stock-status {
                font-weight: 600;
            }
            .in-stock {
                color: #27ae60;
            }
            .out-of-stock {
                color: #c0392b;
            }

            /* Form Cart */
            .qty-input-group {
                display: flex;
                align-items: center;
                gap: 0;
                border: 1px solid #ced4da;
                border-radius: 5px;
                overflow: hidden;
                width: 120px;
            }
            .qty-btn {
                border: none;
                background: #f8f9fa;
                padding: 8px 12px;
                cursor: pointer;
                transition: 0.2s;
            }
            .qty-btn:hover {
                background: #e9ecef;
            }
            .qty-input {
                border: none;
                text-align: center;
                width: 40px;
                -moz-appearance: textfield;
            }

            .btn-cart {
                background-color: #d35400;
                color: #fff;
                padding: 10px 25px;
                border-radius: 5px;
                border: none;
                font-weight: 600;
                text-transform: uppercase;
                transition: 0.3s;
                flex-grow: 1;
            }
            .btn-cart:hover {
                background-color: #a04000;
                color: #fff;
            }
            .btn-buy {
                background-color: #2c3e50;
                color: #fff;
                padding: 10px 25px;
                border-radius: 5px;
                border: none;
                font-weight: 600;
                text-transform: uppercase;
                transition: 0.3s;
            }
            .btn-buy:hover {
                background-color: #1a252f;
                color: #fff;
            }

            /* Description Box */
            .description-box {
                margin-top: 30px;
                background: #fff;
                padding: 25px;
                border-radius: 8px;
                border: 1px solid #eee;
            }
            .section-heading {
                font-size: 1.3rem;
                border-bottom: 2px solid #eee;
                padding-bottom: 10px;
                margin-bottom: 15px;
                color: #2c3e50;
            }

            /* TOAST MESSAGE CSS (Style lại cho đẹp hơn) */
            #toast {
                visibility: hidden;
                min-width: 300px;
                background-color: #333;
                color: #fff;
                text-align: center;
                border-radius: 4px;
                padding: 16px;
                position: fixed;
                z-index: 1000;
                right: 30px;
                top: 100px; /* Xuất hiện ở góc phải trên */
                box-shadow: 0 4px 8px rgba(0,0,0,0.2);
                font-size: 1rem;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 10px;
            }
            #toast.show {
                visibility: visible;
                -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
                animation: fadein 0.5s, fadeout 0.5s 2.5s;
            }
            #toast.toast-success {
                background-color: #27ae60;
            }
            #toast.toast-error {
                background-color: #c0392b;
            }

            @-webkit-keyframes fadein {
                from {
                    right: 0;
                    opacity: 0;
                }
                to {
                    right: 30px;
                    opacity: 1;
                }
            }
            @keyframes fadein {
                from {
                    right: 0;
                    opacity: 0;
                }
                to {
                    right: 30px;
                    opacity: 1;
                }
            }
            @-webkit-keyframes fadeout {
                from {
                    right: 30px;
                    opacity: 1;
                }
                to {
                    right: 0;
                    opacity: 0;
                }
            }
            @keyframes fadeout {
                from {
                    right: 30px;
                    opacity: 1;
                }
                to {
                    right: 0;
                    opacity: 0;
                }
            }

            /* Related Products */
            .custom-card {
                border: 1px solid #eee;
                border-radius: 8px;
                text-decoration: none;
                color: inherit;
                display: block;
                background: #fff;
                transition: 0.3s;
            }
            .custom-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            .card-img-wrapper {
                height: 200px;
                overflow: hidden;
                border-radius: 8px 8px 0 0;
            }
            .card-img-wrapper img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
            .card-body-custom {
                padding: 10px;
            }
            .product-title-sm {
                font-weight: 600;
                font-size: 0.95rem;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        </style>
    </head>
    <body>

        <jsp:include page="header-index.jsp" />
        <jsp:include page="navbar.jsp">
            <jsp:param name="activePage" value=""/>
        </jsp:include>
        <div class="container product-detail-wrapper">
            <div class="row">
                <div class="col-md-5 mb-4">
                    <div class="main-img-container shadow-sm">
                        <c:if test="${productType == 'book'}"><span class="badge-type badge-book">Book</span></c:if>
                        <c:if test="${productType == 'stationery'}"><span class="badge-type badge-stationery">Stationery</span></c:if>

                            <img src="${product.imageUrl}" alt="${product.name}" class="main-img"
                             onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';"/>
                    </div>
                </div>

                <div class="col-md-7 product-info">
                    <h1>${product.name}</h1>

                    <div class="d-flex align-items-center mb-3">
                        <div class="text-warning me-2">
                            <c:forEach begin="1" end="5" var="i">
                                <i class="${i <= product.averageRating ? 'fas' : 'far'} fa-star"></i>
                            </c:forEach>
                        </div>
                        <span class="text-muted small">(${product.reviewCount} reviews) | Sold: ${product.soldQuantity}</span>
                    </div>

                    <div class="product-price">
                        <fmt:formatNumber value="${product.price}" pattern="#,###"/> đ
                    </div>

                    <div class="mb-3">
                        <span class="meta-label">Availability:</span>
                        <c:choose>
                            <c:when test="${product.stockQuantity > 0}">
                                <span class="stock-status in-stock"><i class="fas fa-check-circle"></i> In Stock (${product.stockQuantity})</span>
                            </c:when>
                            <c:otherwise>
                                <span class="stock-status out-of-stock"><i class="fas fa-times-circle"></i> Out of Stock</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="specs-container mb-4">
                        <div class="product-meta"><span class="meta-label">Category:</span> ${product.categoryId.categoryName}</div>

                        <c:choose>
                            <c:when test="${productType == 'book'}">
                                <div class="product-meta">
                                    <span class="meta-label">Author:</span> 
                                    <c:forEach items="${product.authorCollection}" var="a" varStatus="loop">
                                        ${a.authorName}${!loop.last ? ', ' : ''}
                                    </c:forEach>
                                </div>
                                <div class="product-meta"><span class="meta-label">Publisher:</span> ${product.publisher}</div>
                                <div class="product-meta"><span class="meta-label">Published Year:</span> ${product.publishedYear}</div>
                                <c:if test="${not empty product.isbn}">
                                    <div class="product-meta"><span class="meta-label">ISBN:</span> ${product.isbn}</div>
                                </c:if>
                                <c:if test="${not empty product.translator}">
                                    <div class="product-meta"><span class="meta-label">Translator:</span> ${product.translator}</div>
                                </c:if>
                            </c:when>

                            <c:when test="${productType == 'stationery'}">
                                <c:if test="${not empty product.color}">
                                    <div class="product-meta"><span class="meta-label">Color:</span> ${product.color}</div>
                                </c:if>
                                <c:if test="${not empty product.material}">
                                    <div class="product-meta"><span class="meta-label">Material:</span> ${product.material}</div>
                                </c:if>
                            </c:when>
                        </c:choose>
                    </div>

                    <div class="action-area">
                        <form action="cart" method="post" class="d-flex flex-wrap gap-3 align-items-end">
                            <input type="hidden" name="productId" value="${product.productId}"/>

                            <div>
                                <label for="quantity" class="fw-bold mb-1 d-block">Quantity:</label>
                                <div class="qty-input-group">
                                    <button type="button" class="qty-btn" onclick="document.getElementById('quantity').stepDown()">-</button>
                                    <input type="number" id="quantity" name="quantity" class="qty-input" 
                                           value="1" min="1" max="${product.stockQuantity}" required 
                                           ${product.stockQuantity <= 0 ? 'disabled' : ''} />
                                    <button type="button" class="qty-btn" onclick="document.getElementById('quantity').stepUp()">+</button>
                                </div>
                            </div>

                            <div class="d-flex gap-2 flex-grow-1">
                                <button type="submit" name="action" value="add" class="btn btn-cart" 
                                        ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                                    <i class="fa fa-shopping-cart me-2"></i> Add to Cart
                                </button>

                                <button type="submit" name="action" value="buy" class="btn btn-buy" 
                                        ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                                    Buy Now
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="description-box shadow-sm">
                <h3 class="section-heading">Description</h3>
                <div style="line-height: 1.8; color: #555;">
                    <c:choose>
                        <c:when test="${not empty product.descriptionText}">
                            ${product.descriptionText}
                        </c:when>
                        <c:otherwise>
                            <p>No description available for this product.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <c:if test="${not empty relatedProducts}">
                <div class="mt-5">
                    <h3 class="section-heading">Related Products</h3>
                    <div class="row">
                        <c:forEach items="${relatedProducts}" var="rp">
                            <div class="col-6 col-md-3 mb-4">
                                <a href="product-detail?id=${rp.productId}" class="custom-card shadow-sm h-100">
                                    <div class="card-img-wrapper">
                                        <img src="${rp.imageUrl}" alt="${rp.name}"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';"/>
                                    </div>
                                    <div class="card-body-custom">
                                        <div class="product-title-sm" title="${rp.name}">${rp.name}</div>
                                        <div class="text-danger fw-bold">
                                            <fmt:formatNumber value="${rp.price}" pattern="#,###"/> đ
                                        </div>
                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

        </div>

        <c:if test="${not empty sessionScope.cartMessage}">
            <div id="toast" class="${sessionScope.messageType == 'error' ? 'toast-error' : 'toast-success'}">
                <c:if test="${sessionScope.messageType != 'error'}">
                    <i class="fa fa-check-circle fa-lg"></i>
                </c:if>
                <c:if test="${sessionScope.messageType == 'error'}">
                    <i class="fa fa-exclamation-circle fa-lg"></i>
                </c:if>
                <span>${sessionScope.cartMessage}</span>
            </div>

            <script>
                window.onload = function () {
                    var x = document.getElementById("toast");
                    x.className += " show";
                    setTimeout(function () {
                        x.className = x.className.replace(" show", "");
                    }, 3000);
                };
            </script>

            <c:remove var="cartMessage" scope="session"/>
            <c:remove var="messageType" scope="session"/>
        </c:if>


        <jsp:include page="footer-index.jsp" />
        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js"></script>
    </body>
</html>