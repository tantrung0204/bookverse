<%-- Document : cart.jsp Created on : Feb 22, 2026, 8:18:07 PM Author : TrungNT - CE200064 --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

                <fmt:setLocale value="vi_VN" />

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>My Shopping Cart</title>

                    <link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet"
                        type="text/css" />
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

                    <style>
                        body {
                            background-color: #f8f9fa;
                        }

                        .cart-container {
                            background-color: white;
                            padding: 30px;
                            border-radius: 10px;
                            box-shadow: 0 0 15px rgba(0, 0, 0, 0.05);
                        }

                        .img-product {
                            width: 80px;
                            height: 80px;
                            object-fit: cover;
                            border-radius: 5px;
                            border: 1px solid #dee2e6;
                        }

                        .table> :not(caption)>*>* {
                            vertical-align: middle;
                        }

                        .total-area {
                            background-color: #f1f3f5;
                            padding: 20px;
                            border-radius: 10px;
                            margin-top: 20px;
                        }

                        .total-price {
                            font-size: 1.5rem;
                            font-weight: bold;
                            color: #dc3545;
                        }

                        /* --- 1. CSS CHO TOAST NOTIFICATION --- */
                        #toast {
                            visibility: hidden;
                            min-width: 250px;
                            background-color: #333;
                            color: #fff;
                            text-align: center;
                            border-radius: 4px;
                            padding: 16px;
                            position: fixed;
                            z-index: 1000;
                            right: 30px;
                            top: 30px;
                            font-size: 17px;
                            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
                            display: flex;
                            align-items: center;
                            gap: 10px;
                            opacity: 0;
                            transition: opacity 0.5s ease-in-out;
                        }

                        #toast.show {
                            visibility: visible;
                            opacity: 1;
                            animation: slideIn 0.5s, fadeOut 0.5s 2.5s forwards;
                        }

                        .toast-success {
                            background-color: #28a745 !important;
                            border-left: 5px solid #1e7e34;
                        }

                        .toast-error {
                            background-color: #dc3545 !important;
                            border-left: 5px solid #bd2130;
                        }

                        @keyframes slideIn {
                            from {
                                right: -300px;
                                opacity: 0;
                            }

                            to {
                                right: 30px;
                                opacity: 1;
                            }
                        }

                        @keyframes fadeOut {
                            from {
                                opacity: 1;
                            }

                            to {
                                opacity: 0;
                                visibility: hidden;
                            }
                        }
                    </style>
                </head>

                <body>

                    <div class="container py-5">
                        <h2 class="mb-4"><i class="fa-solid fa-cart-shopping"></i> Your Shopping Cart</h2>

                        <c:if test="${empty requestScope.cartList}">
                            <div class="alert alert-info text-center py-5">
                                <i class="fa-solid fa-basket-shopping fa-3x mb-3"></i>
                                <h4>Your cart is currently empty.</h4>
                                <p>Looks like you haven't added any items to the cart yet.</p>
                                <a href="home" class="btn btn-primary mt-3">Start Shopping</a>
                            </div>
                        </c:if>

                        <c:if test="${not empty requestScope.cartList}">
                            <div class="cart-container">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
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
                                                <c:set var="lineTotal"
                                                    value="${item.productId.price * item.cartQuantity}" />
                                                <c:set var="grandTotal" value="${grandTotal + lineTotal}" />

                                                <tr>
                                                    <td>
                                                        <a href="home?action=detail&id=${item.productId.productId}">
                                                            <img src="${item.productId.imageUrl}"
                                                                alt="${item.productId.name}" class="img-product">
                                                        </a>
                                                    </td>

                                                    <td>
                                                        <a href="home?action=detail&id=${item.productId.productId}"
                                                            class="text-decoration-none text-dark fw-bold">
                                                            ${item.productId.name}
                                                        </a>
                                                    </td>

                                                    <td>
                                                        <fmt:formatNumber value="${item.productId.price}"
                                                            pattern="#,###" /> đ
                                                    </td>

                                                    <td class="text-center">
                                                        <div class="input-group d-flex justify-content-center"
                                                            style="width: 120px; margin: 0 auto;">
                                                            <button class="btn btn-outline-secondary btn-sm"
                                                                type="button"
                                                                onclick="updateQuantity(${item.cartId}, -1)">-</button>

                                                            <input type="text" class="form-control text-center p-0"
                                                                id="qty-${item.cartId}" value="${item.cartQuantity}"
                                                                readonly>

                                                            <button class="btn btn-outline-secondary btn-sm"
                                                                type="button"
                                                                onclick="updateQuantity(${item.cartId}, 1)">+</button>
                                                        </div>
                                                    </td>

                                                    <td class="fw-bold text-primary">
                                                        <span id="item-total-${item.cartId}">
                                                            <fmt:formatNumber value="${lineTotal}" pattern="#,###" />
                                                        </span> đ
                                                    </td>

                                                    <td class="text-center">
                                                        <form action="cart" method="post" style="display:inline;">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="cartId" value="${item.cartId}">

                                                            <button type="submit" class="btn btn-outline-danger btn-sm"
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
                                        <div class="col-md-6">
                                            <a href="home" class="btn btn-outline-secondary">
                                                <i class="fa-solid fa-arrow-left"></i> Continue Shopping
                                            </a>
                                        </div>
                                        <div class="col-md-6 text-end">
                                            <span class="fs-5 me-2">Grand Total:</span>
                                            <span class="total-price">
                                                <span id="grand-total">
                                                    <fmt:formatNumber value="${grandTotal}" pattern="#,###" />
                                                </span> đ
                                            </span>
                                            <br><br>
                                            <a href="checkout" class="btn btn-success btn-lg">
                                                Place Order <i class="fa-solid fa-check"></i>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <c:if test="${not empty sessionScope.cartMessage}">
                        <div id="toast"
                            class="${sessionScope.messageType == 'error' ? 'toast-error' : 'toast-success'}">
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

                        <%-- Clean session --%>
                            <c:remove var="cartMessage" scope="session" />
                            <c:remove var="messageType" scope="session" />
                    </c:if>

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

                    <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js"
                        type="text/javascript"></script>
                </body>

                </html>