<%-- 
    Document   : order-detail
    Created on : Mar 7, 2026, 4:53:26 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/order-detail.css">

<div class="detail-container">

    <h2>Order Detail</h2>

    <div class="order-info">

        <p><b>Order ID:</b> ${order.orderId}</p>
        <p><b>Date:</b> ${order.createdAt}</p>
        <p><b>Status:</b> ${order.orderStatus}</p>
        <p><b>Receiver:</b> ${order.receiverName}</p>
        <p><b>Phone:</b> ${order.receiverPhone}</p>
        <p><b>Address:</b> ${order.shippingAddress}</p>
        <p><b>Payment:</b> ${order.paymentMethod}</p>

    </div>

    <h3>Products</h3>

    <table class="item-table">

        <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Total</th>
        </tr>

        <c:forEach var="i" items="${items}">
            <tr>

                <td class="product-info">

                    <img class="product-img"
                         src="${pageContext.request.contextPath}/images/${i.productId.imageUrl}"
                         alt="${i.productId.name}">

                    <span>${i.productId.name}</span>

                </td>

                <td>${i.productId.price}</td>

                <td>${i.orderQuantity}</td>

                <td>${i.productId.price * i.orderQuantity}</td>

            </tr>
        </c:forEach>

    </table>

    <div class="order-buttons">

        <div class="left-btn">
            <button onclick="window.location.href = '${pageContext.request.contextPath}/order?action=list'">
                ← Back
            </button>
        </div>

        <div class="right-btn">

            <!-- Cancel Order -->
            <c:if test="${order.orderStatus == 'Pending'}">
                <form method="post"
                      action="${pageContext.request.contextPath}/order"
                      class="action-form">

                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="orderId" value="${order.orderId}">

                    <button type="submit"
                            class="btn-cancel"
                            onclick="return confirm('Are you sure you want to cancel this order?')">
                        Cancel Order
                    </button>

                </form>
            </c:if>

            <!-- Confirm Received -->
            <c:if test="${order.orderStatus == 'Shipping'}">
                <form method="post"
                      action="${pageContext.request.contextPath}/order"
                      class="action-form">

                    <input type="hidden" name="action" value="confirm">
                    <input type="hidden" name="orderId" value="${order.orderId}">

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
