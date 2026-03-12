<%-- 
    Document   : checkout.jsp
    Created on : Mar 11, 2026
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
    <title>Checkout - Bookverse</title>
    <link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/checkout.css">
</head>
<body>

    <jsp:include page="../public/header-index.jsp" />
    <jsp:include page="../public/navbar.jsp">
        <jsp:param name="activePage" value="" />
    </jsp:include>

    <div class="container py-5" style="max-width: 1000px;">
        <h2 class="checkout-page-title">
            <i class="fa-solid fa-clipboard-check me-2"></i> Order Confirmation
        </h2>

        <%-- Error message --%>
        <c:if test="${not empty sessionScope.checkoutError}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fa-solid fa-circle-exclamation me-2"></i>${sessionScope.checkoutError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="checkoutError" scope="session"/>
        </c:if>

        <form id="checkoutForm" action="${pageContext.request.contextPath}/checkout" method="post">
            <input type="hidden" name="action" value="placeOrder" />
            <input type="hidden" name="checkoutSource" value="${checkoutSource}" />

            <%-- Hidden fields for product IDs and quantities --%>
            <c:forEach items="${checkoutItems}" var="item">
                <input type="hidden" name="productIds" value="${item.product.productId}" />
                <input type="hidden" name="quantities" value="${item.quantity}" />
            </c:forEach>

            <%-- ============ SECTION 1: RECEIVER INFO ============ --%>
            <div class="checkout-section">
                <h4><i class="fa-solid fa-user"></i> Receiver Information</h4>
                <div class="receiver-form">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Receiver Name <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="receiverName"
                                   value="${customer.fullName}" required maxlength="100" />
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Phone Number <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="receiverPhone"
                                   value="${customer.phoneNumber}" required maxlength="20" />
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Shipping Address <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="shippingAddress"
                               value="${customer.address}" required maxlength="255" />
                    </div>
                </div>
            </div>

            <%-- ============ SECTION 2: ORDER ITEMS ============ --%>
            <div class="checkout-section">
                <h4><i class="fa-solid fa-box-open"></i> Order Items</h4>
                <div class="table-responsive">
                    <table class="table checkout-table align-middle">
                        <thead>
                            <tr>
                                <th style="width: 70px;">Image</th>
                                <th>Product</th>
                                <th>Unit Price</th>
                                <th class="text-center">Qty</th>
                                <th class="text-end">Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${checkoutItems}" var="item">
                                <tr>
                                    <td>
                                        <img src="${item.product.imageUrl}" alt="${item.product.name}"
                                             class="checkout-img"
                                             onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';">
                                    </td>
                                    <td class="checkout-product-name">${item.product.name}</td>
                                    <td class="checkout-price">
                                        <fmt:formatNumber value="${item.product.price}" pattern="#,###"/> đ
                                    </td>
                                    <td class="text-center">${item.quantity}</td>
                                    <td class="text-end checkout-price">
                                        <fmt:formatNumber value="${item.lineTotal}" pattern="#,###"/> đ
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <%-- ============ SECTION 3: VOUCHER ============ --%>
            <div class="checkout-section">
                <h4><i class="fa-solid fa-ticket"></i> Voucher</h4>
                <div class="voucher-section">
                    <div class="voucher-input-wrapper">
                        <input type="text" id="voucherCodeInput" placeholder="Enter voucher code..."
                               autocomplete="off" />
                        <button type="button" class="btn-apply-voucher" onclick="applyVoucherCode()">
                            <i class="fa-solid fa-check me-1"></i> Apply
                        </button>
                    </div>
                    <button type="button" class="btn-view-vouchers" onclick="openVoucherModal()">
                        <i class="fa-solid fa-tags me-1"></i> View Vouchers
                    </button>
                </div>

                <%-- Applied voucher display --%>
                <div id="appliedVoucherInfo" class="applied-voucher-info" style="display: none;">
                    <div class="voucher-detail">
                        <i class="fa-solid fa-circle-check me-1"></i>
                        <span id="appliedVoucherText"></span>
                    </div>
                    <button type="button" class="btn-remove-voucher" onclick="removeVoucher()">
                        <i class="fa-solid fa-times"></i> Remove
                    </button>
                </div>

                <%-- Hidden field for applied voucher code --%>
                <input type="hidden" id="voucherCodeHidden" name="voucherCode" value="" />
            </div>

            <%-- ============ SECTION 4: PAYMENT METHOD ============ --%>
            <div class="checkout-section">
                <h4><i class="fa-solid fa-credit-card"></i> Payment Method</h4>
                <div class="payment-options">
                    <label class="payment-option selected" id="paymentCOD" onclick="selectPayment('COD')">
                        <input type="radio" name="paymentMethod" value="COD" checked />
                        <div><i class="fa-solid fa-truck-fast"></i></div>
                        <div class="payment-label">Cash on Delivery (COD)</div>
                        <div class="payment-desc">Pay when you receive the order</div>
                    </label>
                    <label class="payment-option" id="paymentONLINE" onclick="selectPayment('ONLINE')">
                        <input type="radio" name="paymentMethod" value="ONLINE" />
                        <div><i class="fa-solid fa-building-columns"></i></div>
                        <div class="payment-label">Online Payment (VNPay)</div>
                        <div class="payment-desc">Pay securely via VNPay</div>
                    </label>
                </div>
            </div>

            <%-- ============ SECTION 5: ORDER SUMMARY ============ --%>
            <div class="checkout-section">
                <h4><i class="fa-solid fa-receipt"></i> Order Summary</h4>
                <div class="order-summary">
                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span><span id="summarySubtotal"><fmt:formatNumber value="${subtotal}" pattern="#,###"/></span> đ</span>
                    </div>
                    <div class="summary-row discount" id="discountRow" style="display: none;">
                        <span>Voucher Discount</span>
                        <span>- <span id="summaryDiscount">0</span> đ</span>
                    </div>
                    <div class="summary-row total">
                        <span>Total</span>
                        <span><span id="summaryTotal"><fmt:formatNumber value="${subtotal}" pattern="#,###"/></span> đ</span>
                    </div>
                </div>
            </div>

            <%-- ============ PLACE ORDER BUTTON ============ --%>
            <div class="text-center mt-4 mb-5">
                <button type="submit" class="btn-place-order" id="placeOrderBtn">
                    <i class="fa-solid fa-bag-shopping me-2"></i> Place Order
                </button>
            </div>
        </form>
    </div>

    <%-- ============ VOUCHER MODAL ============ --%>
    <div class="voucher-modal-overlay" id="voucherModalOverlay" onclick="closeVoucherModal(event)">
        <div class="voucher-modal" onclick="event.stopPropagation()">
            <div class="voucher-modal-header">
                <h5><i class="fa-solid fa-tags me-2"></i> Available Vouchers</h5>
                <button class="voucher-modal-close" onclick="closeVoucherModal()">&times;</button>
            </div>
            <div class="voucher-modal-body" id="voucherModalBody">
                <div class="text-center py-4">
                    <i class="fa-solid fa-spinner fa-spin fa-2x text-muted"></i>
                    <p class="mt-2 text-muted">Loading vouchers...</p>
                </div>
            </div>
        </div>
    </div>

    <div id="toast"></div>

    <jsp:include page="../public/footer-index.jsp" />

    <script>
        // ============ GLOBAL STATE ============
        var currentSubtotal = ${subtotal};
        var currentDiscount = 0;
        var appliedVoucherCode = '';

        // ============ TOAST ============
        function showToast(message, type) {
            var toast = document.getElementById("toast");
            toast.innerHTML = (type === 'error'
                ? '<i class="fa-solid fa-circle-exclamation"></i> '
                : '<i class="fa-solid fa-circle-check"></i> ')
                + "<span>" + message + "</span>";
            toast.className = type === 'error' ? 'toast-error show' : 'toast-success show';
            setTimeout(function() {
                toast.className = toast.className.replace(" show", "");
            }, 3000);
        }

        // ============ FORMAT CURRENCY ============
        function formatCurrency(number) {
            return new Intl.NumberFormat('vi-VN').format(number);
        }

        // ============ UPDATE SUMMARY ============
        function updateSummary() {
            var total = currentSubtotal - currentDiscount;
            if (total < 0) total = 0;
            document.getElementById('summaryTotal').innerText = formatCurrency(total);

            if (currentDiscount > 0) {
                document.getElementById('discountRow').style.display = 'flex';
                document.getElementById('summaryDiscount').innerText = formatCurrency(currentDiscount);
            } else {
                document.getElementById('discountRow').style.display = 'none';
            }
        }

        // ============ PAYMENT METHOD ============
        function selectPayment(method) {
            document.querySelectorAll('.payment-option').forEach(function(opt) {
                opt.classList.remove('selected');
            });
            document.getElementById('payment' + method).classList.add('selected');
            document.querySelector('input[name="paymentMethod"][value="' + method + '"]').checked = true;
        }

        // ============ APPLY VOUCHER BY CODE ============
        function applyVoucherCode() {
            var code = document.getElementById('voucherCodeInput').value.trim();
            if (!code) {
                showToast('Please enter a voucher code.', 'error');
                return;
            }
            applyVoucher(code);
        }

        function applyVoucher(code) {
            fetch('${pageContext.request.contextPath}/checkout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=applyVoucher&voucherCode=' + encodeURIComponent(code)
                    + '&subtotal=' + currentSubtotal
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                if (data.status === 'success') {
                    appliedVoucherCode = data.voucherCode;
                    currentDiscount = data.discount;

                    document.getElementById('voucherCodeHidden').value = data.voucherCode;
                    document.getElementById('voucherCodeInput').value = data.voucherCode;

                    var info = data.voucherName + ' (' + data.voucherCode + ') — ';
                    if (data.discountType === 1) {
                        info += data.discountValue + '% off';
                    } else {
                        info += formatCurrency(data.discountValue) + ' đ off';
                    }
                    info += ' — Save ' + formatCurrency(data.discount) + ' đ';

                    document.getElementById('appliedVoucherText').innerText = info;
                    document.getElementById('appliedVoucherInfo').style.display = 'flex';

                    updateSummary();
                    showToast(data.message, 'success');

                    // Close modal if open
                    document.getElementById('voucherModalOverlay').classList.remove('active');
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(function(error) {
                showToast('An error occurred. Please try again.', 'error');
            });
        }

        // ============ REMOVE VOUCHER ============
        function removeVoucher() {
            appliedVoucherCode = '';
            currentDiscount = 0;
            document.getElementById('voucherCodeHidden').value = '';
            document.getElementById('voucherCodeInput').value = '';
            document.getElementById('appliedVoucherInfo').style.display = 'none';
            updateSummary();
            showToast('Voucher removed.', 'success');
        }

        // ============ VOUCHER MODAL ============
        function openVoucherModal() {
            document.getElementById('voucherModalOverlay').classList.add('active');
            loadVouchers();
        }

        function closeVoucherModal(event) {
            if (event && event.target !== document.getElementById('voucherModalOverlay')) {
                return;
            }
            document.getElementById('voucherModalOverlay').classList.remove('active');
        }

        function loadVouchers() {
            var body = document.getElementById('voucherModalBody');
            body.innerHTML = '<div class="text-center py-4"><i class="fa-solid fa-spinner fa-spin fa-2x text-muted"></i><p class="mt-2 text-muted">Loading vouchers...</p></div>';

            fetch('${pageContext.request.contextPath}/checkout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'action=getAvailableVouchers'
            })
            .then(function(response) { return response.json(); })
            .then(function(vouchers) {
                if (vouchers.length === 0) {
                    body.innerHTML = '<div class="voucher-empty"><i class="fa-solid fa-ticket fa-3x mb-3" style="color:#ccc;"></i><p>No vouchers available at the moment.</p></div>';
                    return;
                }

                var html = '';
                for (var i = 0; i < vouchers.length; i++) {
                    var v = vouchers[i];
                    var discountText = '';
                    if (v.discountType === 1) {
                        discountText = v.discountValue + '% discount';
                    } else {
                        discountText = formatCurrency(v.discountValue) + ' đ discount';
                    }

                    html += '<div class="voucher-card" onclick="toggleVoucherDetail(this)">';
                    html += '<div class="d-flex justify-content-between align-items-start">';
                    html += '<div>';
                    html += '<div class="voucher-card-name">' + v.voucherName + '</div>';
                    html += '<div class="voucher-card-code">' + v.voucherCode + '</div>';
                    html += '<div class="voucher-card-detail">' + discountText + '</div>';
                    html += '</div>';
                    html += '<button type="button" class="btn-apply-modal" onclick="event.stopPropagation(); applyVoucher(\'' + v.voucherCode + '\')">Apply</button>';
                    html += '</div>';
                    html += '<div class="voucher-detail-expand" style="display:none; margin-top:10px; padding-top:10px; border-top:1px solid #eee;">';
                    html += '<div class="voucher-card-detail">Min. order: ' + formatCurrency(v.minOrderValue) + ' đ</div>';
                    html += '<div class="voucher-card-detail">Remaining: ' + v.availableQuantity + '</div>';
                    html += '<div class="voucher-card-detail">Expires: ' + v.expiryDate + '</div>';
                    html += '</div>';
                    html += '</div>';
                }
                body.innerHTML = html;
            })
            .catch(function(error) {
                body.innerHTML = '<div class="voucher-empty"><p>Failed to load vouchers. Please try again.</p></div>';
            });
        }

        function toggleVoucherDetail(card) {
            var detail = card.querySelector('.voucher-detail-expand');
            if (detail.style.display === 'none') {
                detail.style.display = 'block';
            } else {
                detail.style.display = 'none';
            }
        }

        // ============ FORM VALIDATION ============
        document.getElementById('checkoutForm').addEventListener('submit', function(e) {
            var name = document.querySelector('input[name="receiverName"]').value.trim();
            var phone = document.querySelector('input[name="receiverPhone"]').value.trim();
            var address = document.querySelector('input[name="shippingAddress"]').value.trim();

            if (!name || !phone || !address) {
                e.preventDefault();
                showToast('Please fill in all required fields.', 'error');
                return false;
            }

            // Disable button to prevent double submit
            document.getElementById('placeOrderBtn').disabled = true;
            document.getElementById('placeOrderBtn').innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i> Processing...';
        });
    </script>

    <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
</body>
</html>
