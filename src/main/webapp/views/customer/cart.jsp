<%-- Document : cart.jsp Created on : Feb 22, 2026, 8:18:07 PM Author : TrungNT - CE200064 --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

                <fmt:setLocale value="vi_VN" />

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>My Shopping Cart - Bookverse</title>

                    <link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet"
                        type="text/css" />
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

                    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cart.css">
                </head>

                <body>

                    <jsp:include page="../public/header-index.jsp" />
                    <jsp:include page="../public/navbar.jsp">
                        <jsp:param name="activePage" value="" />
                    </jsp:include>

                    <div class="container py-5" style="max-width: 1200px;">
                        <h2 class="cart-page-title"><i class="fa-solid fa-cart-shopping me-2"></i> Your Shopping Cart
                        </h2>

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
                                            <c:set var="validItemCount" value="0" />

                                            <c:forEach items="${requestScope.cartList}" var="item">
                                                <c:set var="lineTotal"
                                                    value="${item.productId.price * item.cartQuantity}" />

                                                <c:set var="isAvailable"
                                                    value="${item.productId.status == 1 && item.productId.stockQuantity > 0}" />

                                                <c:if test="${isAvailable}">
                                                    <c:set var="grandTotal" value="${grandTotal + lineTotal}" />
                                                    <c:set var="validItemCount"
                                                        value="${validItemCount + item.cartQuantity}" />
                                                </c:if>

                                                <tr class="${!isAvailable ? 'product-disabled' : ''}">
                                                    <td>
                                                        <a href="product-detail?id=${item.productId.productId}">
                                                            <img src="${item.productId.imageUrl}"
                                                                alt="${item.productId.name}" class="img-product"
                                                                onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';">
                                                        </a>
                                                    </td>

                                                    <td>
                                                        <a href="product-detail?id=${item.productId.productId}"
                                                            class="product-name-link fw-bold">
                                                            ${item.productId.name}
                                                        </a>
                                                        <c:if test="${!isAvailable}">
                                                            <div class="text-danger mt-1"
                                                                style="font-size: 0.85rem; font-weight: bold;">
                                                                <i class="fa-solid fa-circle-xmark"></i> Unavailable /
                                                                Out of stock
                                                            </div>
                                                        </c:if>
                                                    </td>

                                                    <td class="price-text">
                                                        <fmt:formatNumber value="${item.productId.price}"
                                                            pattern="#,###" /> đ
                                                    </td>

                                                    <td class="text-center">
                                                        <div
                                                            class="input-group qty-group d-flex justify-content-center">
                                                            <button class="btn qty-btn-custom" type="button"
                                                                onclick="updateByButton(${item.cartId}, -1, ${item.productId.stockQuantity})"
                                                                ${!isAvailable ? 'disabled' : '' }>-</button>

                                                            <input type="number" min="1"
                                                                class="form-control text-center p-0 qty-input-custom"
                                                                id="qty-${item.cartId}" value="${item.cartQuantity}"
                                                                onchange="handleManualInput(${item.cartId}, this.value, ${item.productId.stockQuantity})"
                                                                ${!isAvailable ? 'disabled' : '' }>

                                                            <button class="btn qty-btn-custom" type="button"
                                                                onclick="updateByButton(${item.cartId}, 1, ${item.productId.stockQuantity})"
                                                                ${!isAvailable ? 'disabled' : '' }>+</button>
                                                        </div>
                                                    </td>

                                                    <td class="price-text">
                                                        <span id="item-total-${item.cartId}">
                                                            <c:choose>
                                                                <c:when test="${!isAvailable}">
                                                                    <del class="text-muted">
                                                                        <fmt:formatNumber value="${lineTotal}"
                                                                            pattern="#,###" />
                                                                    </del>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <fmt:formatNumber value="${lineTotal}"
                                                                        pattern="#,###" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </span> đ
                                                    </td>

                                                    <td class="text-center">
                                                        <form action="cart" method="post" style="display:inline;">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="cartId" value="${item.cartId}">

                                                            <button type="submit" class="btn-remove-item"
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
                                            <span class="grand-total-label me-2">Grand Total (<span
                                                    id="valid-item-count">${validItemCount}</span> items):</span>
                                            <span class="grand-total-amount">
                                                <span id="grand-total">
                                                    <fmt:formatNumber value="${grandTotal}" pattern="#,###" />
                                                </span> đ
                                            </span>
                                            <br><br>
                                            <a href="checkout"
                                                class="btn-brand-solid btn-lg px-5 ${validItemCount == 0 ? 'disabled' : ''}">
                                                Place Order
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <div id="toast"></div>

                    <c:if test="${not empty sessionScope.cartMessage}">
                        <script>
                            document.addEventListener("DOMContentLoaded", function () {
                                showToastJS('${sessionScope.cartMessage}', '${sessionScope.messageType}');
                            });
                        </script>
                        <c:remove var="cartMessage" scope="session" />
                        <c:remove var="messageType" scope="session" />
                    </c:if>

                    <jsp:include page="../public/footer-index.jsp" />

                    <script>
                        function showToastJS(message, type) {
                            var toast = document.getElementById("toast");

                            if (!toast) {
                                toast = document.createElement("div");
                                toast.id = "toast";
                                document.body.appendChild(toast);
                            }

                            // Set nội dung và màu sắc
                            toast.innerHTML = (type === 'error' ? '<i class="fa-solid fa-circle-exclamation"></i> ' : '<i class="fa-solid fa-circle-check"></i> ') + "<span>" + message + "</span>";
                            toast.className = type === 'error' ? 'toast-error show' : 'toast-success show';

                            // Tự động tắt sau 3 giây
                            setTimeout(function () {
                                toast.className = toast.className.replace(" show", "");
                            }, 1000);
                        }

                        function formatCurrency(number) {
                            return new Intl.NumberFormat('vi-VN').format(number);
                        }

                        function updateByButton(cartId, change, maxStock) {
                            let qtyInput = document.getElementById("qty-" + cartId);
                            let currentQty = parseInt(qtyInput.value) || 1;
                            let newQty = currentQty + change;

                            if (newQty < 1)
                                return;

                            if (newQty > maxStock) {
                                newQty = maxStock;
                                showToastJS("Maximum available stock reached (" + maxStock + ")!", "error");
                            }

                            // Gọi hàm gửi AJAX
                            sendAjaxUpdate(cartId, newQty, currentQty, qtyInput);
                        }

                        function handleManualInput(cartId, typedValue, maxStock) {
                            let qtyInput = document.getElementById("qty-" + cartId);
                            let currentQty = parseInt(qtyInput.getAttribute("data-current") || qtyInput.defaultValue);
                            let newQty = parseInt(typedValue);

                            if (isNaN(newQty) || newQty < 1) {
                                newQty = 1;
                                showToastJS("Quantity must be at least 1!", "error");
                            }

                            if (newQty > maxStock) {
                                newQty = maxStock;
                                showToastJS("Only " + maxStock + " items left in stock. Adjusted to maximum!", "error");
                            }

                            // Gọi hàm gửi AJAX
                            sendAjaxUpdate(cartId, newQty, currentQty, qtyInput);
                        }

                        function sendAjaxUpdate(cartId, newQty, currentQty, qtyInput) {
                            qtyInput.value = newQty; // Tạm thời update UI cho mượt

                            fetch('cart', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                                body: 'action=update&cartId=' + cartId + '&quantity=' + newQty
                            })
                                .then(response => {
                                    if (response.ok)
                                        return response.json();
                                    throw new Error('Network response was not ok');
                                })
                                .then(data => {
                                    if (data.status === 'success') {
                                        document.getElementById("item-total-" + cartId).innerText = formatCurrency(data.itemTotal);
                                        document.getElementById("grand-total").innerText = formatCurrency(data.grandTotal);

                                        // CẬP NHẬT TỔNG SỐ ITEM
                                        let countSpan = document.getElementById("valid-item-count");
                                        if (countSpan) {
                                            if (data.totalItems !== undefined) {
                                                countSpan.innerText = data.totalItems;
                                            } else {
                                                // Tự quét tất cả các ô input (không bị mờ) và cộng lại nếu Server lỗi
                                                let total = 0;
                                                document.querySelectorAll(".qty-input-custom:not([disabled])").forEach(input => {
                                                    total += parseInt(input.value) || 0;
                                                });
                                                countSpan.innerText = total;
                                            }
                                        }

                                        qtyInput.setAttribute("data-current", newQty);

                                    } else if (data.status === 'error') {
                                        showToastJS(data.message, "error");
                                        qtyInput.value = currentQty;
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    showToastJS("An error occurred!", "error");
                                    qtyInput.value = currentQty;
                                });
                        }
                    </script>

                    <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js"
                        type="text/javascript"></script>
                </body>

                </html>