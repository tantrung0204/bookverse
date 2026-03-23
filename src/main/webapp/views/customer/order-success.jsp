<%-- 
    Document   : order-success.jsp
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
    <title>Order Result - Bookverse</title>
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

    <div class="container py-5">

        <c:choose>
            <%-- ORDER SUCCESS --%>
            <c:when test="${not empty sessionScope.orderSuccess}">
                <c:set var="order" value="${sessionScope.orderSuccess}" />

                <div class="order-success-box shadow-sm">
                    <div class="success-icon">
                        <i class="fa-solid fa-circle-check"></i>
                    </div>
                    <h2>Order Placed Successfully!</h2>

                    <c:if test="${not empty sessionScope.paymentMessage}">
                        <div class="alert alert-success mt-3">
                            <i class="fa-solid fa-check-circle me-1"></i> ${sessionScope.paymentMessage}
                        </div>
                        <c:remove var="paymentMessage" scope="session"/>
                    </c:if>

                    <p class="text-muted mt-3">Thank you for your order. We will process it shortly.</p>

                    <div class="mt-4 mb-3">
                        <div class="mb-2">
                            <span class="text-muted">Order ID:</span>
                            <span class="order-id">#${order.orderId}</span>
                        </div>
                        <div class="mb-2">
                            <span class="text-muted">Total Amount:</span>
                            <span class="order-total">
                                <fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/> đ
                            </span>
                        </div>
                        <div class="mb-2">
                            <span class="text-muted">Payment Method:</span>
                            <span class="fw-bold">
                                ${order.paymentMethod == 'COD' ? 'Cash on Delivery' : 'Online (VNPay)'}
                            </span>
                        </div>
                        <div class="mb-2">
                            <span class="text-muted">Payment Status:</span>
                            <c:choose>
                                <c:when test="${order.isPaid}">
                                    <span class="payment-badge-paid">Paid</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="payment-badge-unpaid">Unpaid</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="mb-2">
                            <span class="text-muted">Order Status:</span>
                            <span class="order-status-badge">Pending</span>
                        </div>
                    </div>

                    <div class="mt-4 d-flex justify-content-center gap-3 flex-wrap">
                        <a href="${pageContext.request.contextPath}/home" class="btn-brand-solid">
                            <i class="fa-solid fa-house me-2"></i> Continue Shopping
                        </a>
                    </div>
                </div>

                <c:remove var="orderSuccess" scope="session"/>
            </c:when>

            <%-- PAYMENT ERROR --%>
            <c:when test="${not empty sessionScope.paymentError}">
                <div class="order-success-box shadow-sm">
                    <div class="fail-icon">
                        <i class="fa-solid fa-circle-xmark"></i>
                    </div>
                    <h2>Payment Failed</h2>
                    <p class="text-danger mt-3">${sessionScope.paymentError}</p>

                    <c:if test="${not empty sessionScope.failedOrderId}">
                        <p class="text-muted">
                            Your order <strong>#${sessionScope.failedOrderId}</strong> has been created
                            with status <strong>Pending</strong>. You can retry payment or choose
                            Cash on Delivery instead.
                        </p>
                    </c:if>

                    <div class="mt-4 d-flex justify-content-center gap-3 flex-wrap">
                        <a href="${pageContext.request.contextPath}/home" class="btn-brand-solid">
                            <i class="fa-solid fa-house me-2"></i> Go Home
                        </a>
                    </div>
                </div>

                <c:remove var="paymentError" scope="session"/>
                <c:remove var="failedOrderId" scope="session"/>
            </c:when>

            <%-- NO ORDER DATA --%>
            <c:otherwise>
                <div class="order-success-box shadow-sm">
                    <div class="fail-icon">
                        <i class="fa-solid fa-circle-question"></i>
                    </div>
                    <h2>No Order Information</h2>
                    <p class="text-muted">There is no order information to display.</p>
                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/home" class="btn-brand-solid">
                            <i class="fa-solid fa-house me-2"></i> Go Home
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <jsp:include page="../public/footer-index.jsp" />
    <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
</body>
</html>
