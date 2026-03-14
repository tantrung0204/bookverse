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
            <th>Action</th>
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

                <td>

                    <!-- Show Review button only when order completed -->

                    <c:choose>

                        <c:when test="${order.orderStatus == 'Completed' && !i.reviewed}">
                            <button class="btn-review"
                                    onclick="openReviewModal(
                                    ${i.productId.productId},
                                                    '${i.productId.name}',
                                                    '${i.productId.imageUrl}'
                                                    )">
                                Review
                            </button>
                        </c:when>

                        <c:when test="${order.orderStatus == 'Completed' && i.reviewed}">
                            <span class="reviewed">Reviewed</span>
                        </c:when>

                        <c:otherwise>
                            <span>-</span>
                        </c:otherwise>

                    </c:choose>

                </td>

            </tr>
        </c:forEach>
        <tr class="total-row">
            <td colspan="3"><b>Total:</b></td>
            <td colspan="2"><b>${order.totalAmount} đ</b></td>
        </tr>
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

<div id="reviewModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeReviewModal()">&times;</span>
        <h3>Write Review</h3>
        <div class="review-product">
            <img id="reviewImg">
            <span id="reviewName"></span>
        </div>
        <form id="reviewForm" class="review-form">
            <input type="hidden" name="action" value="create">
            <input type="hidden" id="productId" name="productId">
            <div class="form-group">
                <label>Rating</label>
                <select name="rating" required>
                    <option value="5">⭐⭐⭐⭐⭐</option>
                    <option value="4">⭐⭐⭐⭐</option>
                    <option value="3">⭐⭐⭐</option>
                    <option value="2">⭐⭐</option>
                    <option value="1">⭐</option>
                </select>
            </div>
            <div class="form-group">
                <label>Comment</label>
                <textarea name="content" rows="4"
                          placeholder="Write your review..."></textarea>
            </div>
            <button type="submit" class="btn-confirm submit-review">
                Submit Review
            </button>
        </form>
    </div>
</div>
<script>

    function openReviewModal(id, name, img) {
        console.log("productId:", id);
        document.getElementById("productId").value = id;
        document.getElementById("reviewName").innerText = name;
        document.getElementById("reviewImg").src =
                "${pageContext.request.contextPath}/images/" + img;
        document.getElementById("reviewModal").style.display = "block";
    }
    
    function closeReviewModal() {
        document.getElementById("reviewModal").style.display = "none";
    }
    document.addEventListener("DOMContentLoaded", function () {
        document.getElementById("reviewForm").onsubmit = function (e) {
            e.preventDefault();
            let formData = new FormData(this);
            fetch("${pageContext.request.contextPath}/feedback", {
                method: "POST",
                body: formData
            })
                    .then(r => r.text())
                    .then(data => {
                        data = data.trim();
                        if (data === "success") {
                            alert("Feedback submitted successfully!");
                            closeReviewModal();
                            location.reload();
                        } else {
                            alert(data);
                        }
                    });
        };
    });

</script>
