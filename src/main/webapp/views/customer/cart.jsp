<%-- 
    Document   : cart.jsp
    Created on : Feb 22, 2026, 8:18:07 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Shopping Cart - Bookverse</title>

        <link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cart.css">
    </head>
    <body>

        <jsp:include page="../public/header-index.jsp" />
        <jsp:include page="../public/navbar.jsp">
            <jsp:param name="activePage" value=""/>
        </jsp:include>

        <div class="container py-5" style="max-width: 1200px;">
            <h2 class="cart-page-title"><i class="fa-solid fa-cart-shopping me-2"></i> Your Shopping Cart</h2>

            <c:if test="${empty requestScope.cartList}">
                <div class="empty-cart-box shadow-sm">
                    <i class="fa-solid fa-basket-shopping fa-3x mb-3 empty-icon"></i>
                    <h4>Your cart is currently empty.</h4>
                    <p class="text-muted">Looks like you haven't added any items to the cart yet.</p>
                    <a href="home" class="btn-brand-solid mt-3">Start Shopping</a>
                </div>
            </c:if>

            <c:if test="${not empty requestScope.cartList}">
                <div class="cart-container shadow-sm">
                    <div class="table-responsive">
                        <table class="table table-hover cart-table align-middle">
                            <thead>
                                <tr>
                                    <th style="width: 100px;">Image</th>
                                    <th>Product Name</th>
                                    <th>Unit Price</th>
                                    <th class="text-center">Quantity</th>
                                    <th>Total</th>
                                    <th class="text-center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="grandTotal" value="0" />

                                <c:forEach items="${requestScope.cartList}" var="item">
                                    <c:set var="lineTotal" value="${item.productId.price * item.cartQuantity}"/>
                                    <c:set var="grandTotal" value="${grandTotal + lineTotal}" />

                                    <tr>
                                        <td>
                                            <a href="product-detail?id=${item.productId.productId}">
                                                <img src="${item.productId.imageUrl}" alt="${item.productId.name}" class="img-product"
                                                     onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';">
                                            </a>
                                        </td>

                                        <td>
                                            <a href="product-detail?id=${item.productId.productId}" class="product-name-link">
                                                ${item.productId.name}
                                            </a>
                                        </td>

                                        <td class="price-text">
                                            <fmt:formatNumber value="${item.productId.price}" pattern="#,###"/> đ
                                        </td>

                                        <td class="text-center">
                                            <div class="input-group qty-group d-flex justify-content-center">
                                                <button class="btn qty-btn-custom" type="button" 
                                                        onclick="updateQuantity(${item.cartId}, -1)">-</button>

                                                <input type="text" class="form-control text-center p-0 qty-input-custom" 
                                                       id="qty-${item.cartId}" 
                                                       value="${item.cartQuantity}" readonly>

                                                <button class="btn qty-btn-custom" type="button" 
                                                        onclick="updateQuantity(${item.cartId}, 1)">+</button>
                                            </div>
                                        </td>

                                        <td class="price-text">
                                            <span id="item-total-${item.cartId}">
                                                <fmt:formatNumber value="${lineTotal}" pattern="#,###"/>
                                            </span> đ
                                        </td>

                                        <td class="text-center">
                                            <form action="cart" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="cartId" value="${item.cartId}">

                                                <button type="submit" 
                                                        class="btn-remove-item" 
                                                        onclick="return confirm('Do you really want to remove this product?');"
                                                        title="Remove item">
                                                    <i class="fa-solid fa-trash-can"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="total-area">
                        <div class="row align-items-center">
                            <div class="col-md-6 mb-3 mb-md-0">
                                <a href="home" class="btn-brand-outline">
                                    <i class="fa-solid fa-arrow-left me-2"></i> Continue Shopping
                                </a>
                            </div>
                            <div class="col-md-6 text-end">
                                <span class="grand-total-label me-2">Grand Total:</span>
                                <span class="grand-total-amount">
                                    <span id="grand-total">
                                        <fmt:formatNumber value="${grandTotal}" pattern="#,###"/>
                                    </span> đ
                                </span>
                                <br><br>
                                <a href="checkout" class="btn-brand-solid btn-lg px-5">
                                    Place Order
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <c:if test="${not empty sessionScope.cartMessage}">
            <div id="toast" class="${sessionScope.messageType == 'error' ? 'toast-error' : 'toast-success'}">
                <c:if test="${sessionScope.messageType != 'error'}">
                    <i class="fa-solid fa-circle-check"></i>
                </c:if>
                <c:if test="${sessionScope.messageType == 'error'}">
                    <i class="fa-solid fa-circle-exclamation"></i>
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

        <jsp:include page="../public/footer-index.jsp" />

        <script>
            function formatCurrency(number) {
                return new Intl.NumberFormat('vi-VN').format(number);
            }

            function updateQuantity(cartId, change) {
                let qtyInput = document.getElementById("qty-" + cartId);
                let currentQty = parseInt(qtyInput.value);
                let newQty = currentQty + change;

                if (newQty < 1)
                    return;

                fetch('cart', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'action=update&cartId=' + cartId + '&quantity=' + newQty
                })
                        .then(response => {
                            if (response.ok) {
                                return response.json();
                            }
                            throw new Error('Network response was not ok');
                        })
                        .then(data => {
                            if (data.status === 'success') {
                                qtyInput.value = newQty;
                                document.getElementById("item-total-" + cartId).innerText = formatCurrency(data.itemTotal);
                                document.getElementById("grand-total").innerText = formatCurrency(data.grandTotal);
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert("Error!");
                        });
            }
        </script>

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
    </body>
</html>