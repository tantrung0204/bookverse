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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-detail.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        
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
                                <span class="stock-status in-stock"><i class="fas fa-check-circle"></i> In Stock (${product.stockQuantity} available)</span>
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
                        <div>
                            <label for="quantity" class="fw-bold mb-1 d-block">Quantity:</label>
                            <div class="qty-input-group">
                                <button type="button" class="qty-btn" onclick="document.getElementById('quantity').stepDown(); document.getElementById('quantity').dispatchEvent(new Event('input'));">-</button>
                                <input type="number" id="quantity" name="quantity" class="qty-input" 
                                       value="1" min="1" max="${product.stockQuantity}" required 
                                       ${product.stockQuantity <= 0 ? 'disabled' : ''} />
                                <button type="button" class="qty-btn" onclick="document.getElementById('quantity').stepUp(); document.getElementById('quantity').dispatchEvent(new Event('input'));">+</button>
                            </div>
                        </div>

                        <div class="d-flex gap-2 flex-grow-1 mt-3">
                            <%-- Add to Cart form --%>
                            <form action="cart" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="add" />
                                <input type="hidden" name="productId" value="${product.productId}"/>
                                <input type="hidden" name="quantity" id="cartQuantity" value="1"/>
                                <button type="submit" class="btn btn-cart" id="add-to-cart"
                                        ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                                    <i class="fa fa-shopping-cart me-2"></i> Add to Cart
                                </button>
                            </form>

                            <%-- Buy Now form - goes to checkout --%>
                            <form action="${pageContext.request.contextPath}/checkout" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="buyNow" />
                                <input type="hidden" name="productId" value="${product.productId}"/>
                                <input type="hidden" name="quantity" id="buyNowQuantity" value="1"/>
                                <button type="submit" class="btn btn-buy" 
                                        ${product.stockQuantity <= 0 ? 'disabled' : ''}>
                                    Buy Now
                                </button>
                            </form>
                        </div>

                        <%-- Sync quantity input with hidden fields --%>
                        <script>
                            document.getElementById('quantity').addEventListener('change', function() {
                                document.getElementById('cartQuantity').value = this.value;
                                document.getElementById('buyNowQuantity').value = this.value;
                            });
                            // Also sync on stepUp/stepDown
                            document.getElementById('quantity').addEventListener('input', function() {
                                document.getElementById('cartQuantity').value = this.value;
                                document.getElementById('buyNowQuantity').value = this.value;
                            });
                        </script>
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

            <div id="reviews-section" class="feedback-section shadow-sm">
                <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                    <h3 class="m-0" style="font-size: 1.4rem; color: #2c3e50;">Customer Reviews</h3>

                    <div class="d-flex align-items-center gap-2">
                        <span class="fs-4 fw-bold text-dark"><fmt:formatNumber value="${product.averageRating}" maxFractionDigits="1"/></span>
                        <span class="text-warning fs-5">★</span>
                        <span class="text-muted">(${product.reviewCount} Reviews)</span>
                    </div>
                </div>
                <div class="review-list">
                    <c:choose>
                        <%-- MODIFIED: Use feedbackList instead of product.feedbackCollection --%>
                        <c:when test="${empty feedbackList}">
                            <div class="text-center py-4 text-muted">
                                <i class="far fa-comment-dots fa-3x mb-3 text-light"></i>
                                <p>There are no reviews for this product yet. Be the first to review!</p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <%-- MODIFIED: Loop through feedbackList --%>
                            <c:forEach items="${feedbackList}" var="fb">
                                <div class="review-item">
                                    <div>
                                        <c:choose>
                                            <c:when test="${not empty fb.customerId.profileImageUrl}">
                                                <img src="${fb.customerId.profileImageUrl}" alt="${fb.customerId.fullName}" class="reviewer-avatar"
                                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/default-avt.jpg';">
                                                <div class="reviewer-avatar-placeholder" style="display: none;">
                                                    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                                                    ${fn:substring(fb.customerId.fullName, 0, 1)}
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                                                <div class="reviewer-avatar-placeholder">
                                                    ${fn:substring(fb.customerId.fullName, 0, 1)}
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="review-content">
                                        <div class="reviewer-name">${fb.customerId.fullName}</div>

                                        <div class="review-date">
                                            <fmt:formatDate value="${fb.createdAt}" pattern="dd MMM, yyyy 'at' HH:mm"/>
                                        </div>

                                        <div class="review-rating">
                                            <c:forEach begin="1" end="5" var="star">
                                                <i class="${star <= fb.rating ? 'fas' : 'far'} fa-star"></i>
                                            </c:forEach>
                                        </div>

                                        <p class="review-text">${fb.contentText}</p>
                                    </div>
                                </div>
                            </c:forEach>

                            <%-- ADDED: Pagination Controls for Feedback --%>
                            <c:if test="${totalFbPages > 1}">
                                <div class="pagination mt-4 d-flex justify-content-center gap-1">
                                    <c:forEach begin="1" end="${totalFbPages}" var="i">
                                        <%-- Note the #reviews-section to scroll back down after click --%>
                                        <a href="product-detail?id=${product.productId}&page=${i}#reviews-section" 
                                           class="page-link-custom ${currentFbPage == i ? 'active' : ''}">
                                            ${i}
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:if>

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