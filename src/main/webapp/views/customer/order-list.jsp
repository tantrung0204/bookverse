<%-- 
    Document   : order-list
    Created on : Mar 7, 2026, 3:54:31 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/order-list.css">

<div class="order-container">

    <h2 class="page-title">My Orders</h2>

    <!-- STATUS TABS -->

    <div class="order-tabs">

        <button class="tab active" onclick="filterOrder('all')">All</button>
        <button class="tab" onclick="filterOrder('pending')">Pending</button>
        <button class="tab" onclick="filterOrder('shipping')">Shipping</button>
        <button class="tab" onclick="filterOrder('completed')">Completed</button>
        <button class="tab" onclick="filterOrder('canceled')">Canceled</button>

    </div>

    <!-- ORDER LIST -->

    <c:forEach var="o" items="${orders}">

        <div class="order-card" data-status="${fn:toLowerCase(o.orderStatus)}">

            <div class="order-header">

                <span class="order-id">Order #${o.orderId}</span>

                <span class="status ${fn:toLowerCase(o.orderStatus)}">
                    ${o.orderStatus}
                </span>

            </div>

            <div class="order-body">

                <div class="order-info">

                    <p>Date: <fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd HH:mm"/></p>

                </div>

            </div>

            <div class="order-footer">

                <span class="order-total">
                    Total: ${o.totalAmount}
                </span>

                <div class="order-actions">

                    <a class="btn-view"
                       href="${pageContext.request.contextPath}/order?action=detail&id=${o.orderId}">
                        View Detail
                    </a>

                    <!-- Cancel Order -->
                    <c:if test="${o.orderStatus == 'Pending'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/order"
                              class="action-form">

                            <input type="hidden" name="action" value="cancel">
                            <input type="hidden" name="orderId" value="${o.orderId}">

                            <button type="submit"
                                    class="btn-cancel"
                                    onclick="return confirm('Are you sure you want to cancel this order?')">
                                Cancel Order
                            </button>

                        </form>
                    </c:if>

                    <!-- Confirm Received -->
                    <c:if test="${o.orderStatus == 'Shipping'}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/order"
                              class="action-form">

                            <input type="hidden" name="action" value="confirm">
                            <input type="hidden" name="orderId" value="${o.orderId}">

                            <button type="submit"
                                    class="btn-confirm"
                                    onclick="return confirm('Are you sure you want to confirm received?')">
                                Received
                            </button>

                        </form>
                    </c:if>

                </div>

            </div>

        </div>

    </c:forEach>

</div>

<script>
    function filterOrder(status) {

        let orders = document.querySelectorAll(".order-card");
        let tabs = document.querySelectorAll(".tab");

        // remove active khỏi tất cả tab
        tabs.forEach(t => t.classList.remove("active"));

        // thêm active cho tab được click
        event.target.classList.add("active");

        orders.forEach(o => {

            let s = o.dataset.status;

            if (status === "all" || s === status.toLowerCase()) {
                o.style.display = "block";
            } else {
                o.style.display = "none";
            }

        });

    }
</script>