<%-- Document : order-list Created on : Mar 16, 2026, 2:41:58 AM Author : huyqu --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/order-list.css">

<style>
/* Synchronize order status colors with customer side */
.badge-order-status {
    padding: 4px 10px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
    display: inline-block;
}
.badge-order-status.Pending { background: #f5e6bf; color: #9a6a00; } /* vàng */
.badge-order-status.Confirmed, .badge-order-status.Completed { background: #dbeee5; color: #2c7a63; } /* xanh lá */
.badge-order-status.Shipping { background: #e0d4f5; color: #5a3c87; } /* tím xanh */
.badge-order-status.Cancelled { background: #f5d6d6; color: #9a3333; } /* đỏ */
</style>

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Orders</p>
        <p class="subtitle">Manage and track customer orders efficiently</p>
    </div>

    <div class="content-card">
        <div class="toolbar">
            <form action="${pageContext.request.contextPath}/dashboard/order" method="GET"
                  class="search-form" style="width: 100%; max-width: 400px;">
                <input type="hidden" name="action" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search by Receiver Name, Phone..."
                           value="${searchKeyword}">
                </div>
            </form>
        </div>

        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.error_edit}">
            <div class="alert alert-danger">${sessionScope.error_edit}</div>
            <c:remove var="error_edit" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.success_edit}">
            <div class="alert alert-success">${sessionScope.success_edit}</div>
            <c:remove var="success_edit" scope="session" />
        </c:if>

        <c:choose>
            <c:when test="${not empty orders}">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Staff ID</th>
                            <th>Customer/Receiver</th>
                            <th>Contact</th>
                            <th>Total Amount</th>
                            <th>Payment</th>
                            <th>Paid Status</th>
                            <th>Order Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td>${o.orderId}</td>
                                <td>${empty o.staffId ? 'N/A' : o.staffId.staffId}</td>
                                <td>${o.receiverName}</td>
                                <td>${o.receiverPhone}</td>
                                <td style="color: #d9534f; font-weight: bold;">
                                    <fmt:formatNumber value="${o.totalAmount}" type="number"
                                                      pattern="#,##0" /> VND
                                </td>
                                <td>${o.paymentMethod}</td>
                                <td>
                                    <span
                                        class="badge-status ${o.isPaid ? 'badge-active' : 'badge-inactive'}">
                                        ${o.isPaid ? 'Paid' : 'Unpaid'}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge-order-status ${o.orderStatus}">
                                        ${o.orderStatus}
                                    </span>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail" title="View Detail"
                                                onclick="openViewDetailOrderPopup('${o.orderId}')">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <!-- CONFIRM: Pending + (COD or ONLINE+paid) -->
                                        <c:if test="${o.orderStatus == 'Pending'}">
                                            <c:choose>
                                                <c:when test="${o.paymentMethod == 'ONLINE' && !o.isPaid}">
                                                    <span class="badge-status badge-inactive" title="Waiting for online payment" style="font-size: 11px; cursor: default;">
                                                        Waiting Payment
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <form action="${pageContext.request.contextPath}/dashboard/order"
                                                          method="POST" style="display:inline;"
                                                          onsubmit="return confirm('Confirm Order #O00${o.orderId}?');">
                                                        <input type="hidden" name="action" value="confirm">
                                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                                        <button type="submit" class="btn-action btn-edit" title="Confirm Order">
                                                            <i class="bi bi-check-circle"></i>
                                                        </button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>

                                        <!-- SHIP: Confirmed -->
                                        <c:if test="${o.orderStatus == 'Confirmed'}">
                                            <form action="${pageContext.request.contextPath}/dashboard/order"
                                                  method="POST" style="display:inline;"
                                                  onsubmit="return confirm('Ship Order #O00${o.orderId}?');">
                                                <input type="hidden" name="action" value="ship">
                                                <input type="hidden" name="orderId" value="${o.orderId}">
                                                <button type="submit" class="btn-action btn-edit" title="Ship Order">
                                                    <i class="bi bi-truck"></i>
                                                </button>
                                            </form>
                                        </c:if>

                                        <!-- COMPLETE: Shipping -->
                                        <c:if test="${o.orderStatus == 'Shipping'}">
                                            <form action="${pageContext.request.contextPath}/dashboard/order"
                                                  method="POST" style="display:inline;"
                                                  onsubmit="return confirm('Complete Order #O00${o.orderId}?');">
                                                <input type="hidden" name="action" value="complete">
                                                <input type="hidden" name="orderId" value="${o.orderId}">
                                                <button type="submit" class="btn-action btn-edit" title="Complete Order">
                                                    <i class="bi bi-check-all"></i>
                                                </button>
                                            </form>
                                        </c:if>

                                        <!-- CANCEL: only Pending + not paid -->
                                        <c:if test="${o.orderStatus == 'Pending' && !o.isPaid}">
                                            <form action="${pageContext.request.contextPath}/dashboard/order"
                                                  method="POST" style="display:inline;"
                                                  onsubmit="return confirm('Are you sure you want to cancel Order #O00${o.orderId}?');">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="orderId" value="${o.orderId}">
                                                <button type="submit" class="btn-action btn-delete" title="Cancel Order">
                                                    <i class="bi bi-x-circle"></i>
                                                </button>
                                            </form>
                                        </c:if>
                                    </div>
                                    <!-- Hidden Details for Modal -->
                                    <div id="orderData_${o.orderId}" style="display:none;">
                                        <div class="o-receiver">${o.receiverName}</div>
                                        <div class="o-staff">${empty o.staffId ? 'N/A' : o.staffId.fullName}</div>
                                        <div class="o-contact">${o.receiverPhone}</div>
                                        <div class="o-payment-method">${o.paymentMethod}</div>
                                        <div class="o-is-paid">${o.isPaid ? 'Paid' : 'Unpaid'}</div>
                                        <div class="o-status">${o.orderStatus}</div>
                                        <div class="o-voucher-discount">
                                            <c:choose>
                                                <c:when test="${not empty o.voucherId}">
                                                    ${o.voucherId.discountValue} ${o.voucherId.discountType == 1 ? '%' : 'VND'}
                                                </c:when>
                                                <c:otherwise>0 VND</c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="o-total">
                                            <fmt:formatNumber value="${o.totalAmount}" type="number" pattern="#,##0" /> VND
                                        </div>
                                        <div class="o-items">
                                            <table class="order-items-table">
                                                <thead>
                                                    <tr>
                                                        <th>Product</th>
                                                        <th class="text-center">Qty</th>
                                                        <th class="text-right">Price</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="item" items="${o.orderItemCollection}">
                                                    <tr>
                                                        <td>${item.productId.name}</td>
                                                        <td class="text-center">${item.orderQuantity}</td>
                                                        <td class="text-right">
                                                            <fmt:formatNumber value="${item.productId.price}" type="number" pattern="#,##0" /> VND
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning">No orders found!</div>
            </c:otherwise>
        </c:choose>

        <c:if test="${totalPages > 1}">
            <c:set var="qAction" value="${not empty searchKeyword ? 'search' : 'list'}" />
            <c:set var="qKeyword" value="${not empty searchKeyword ? '&keyword='.concat(searchKeyword) : ''}" />
            <div class="pagination">
                <!-- Previous -->
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage - 1}" 
                       class="page-btn">«</a>
                </c:if>

                <!-- Page 1 -->
                <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=1"
                   class="page-btn ${currentPage == 1 ? 'active' : ''}">
                    1
                </a>

                <c:if test="${currentPage > 3}">
                    <span class="page-btn">...</span>
                </c:if>

                <c:if test="${currentPage - 1 > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage - 1}"
                       class="page-btn">
                        ${currentPage - 1}
                    </a>
                </c:if>

                <c:if test="${currentPage != 1 && currentPage != totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage}"
                       class="page-btn active">
                        ${currentPage}
                    </a>
                </c:if>

                <c:if test="${currentPage + 1 < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage + 1}"
                       class="page-btn">
                        ${currentPage + 1}
                    </a>
                </c:if>

                <c:if test="${currentPage < totalPages - 2}">
                    <span class="page-btn">...</span>
                </c:if>

                <c:if test="${totalPages > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${totalPages}"
                       class="page-btn ${currentPage == totalPages ? 'active' : ''}">
                        ${totalPages}
                    </a>
                </c:if>

                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage + 1}" 
                       class="page-btn">»</a>
                </c:if>
            </div>
        </c:if>

    </div>
</div>

<script>
    // Close modal when clicking outside
    window.onclick = function (event) {
        var modalDetail = document.getElementById("viewDetailOrderPopup");
        if (event.target == modalDetail) {
            closeViewDetailOrderPopup();
        }
    }
</script>

<div id="viewDetailOrderPopup" class="modal-overlay" style="display: none;">
    <div class="modal-content" style="width: 650px; max-width: 90%; max-height: 90vh; display: flex; flex-direction: column; padding: 25px;">
        <div class="modal-header">
            <h3>Order Detail</h3>
        </div>

        <div class="modal-body" style="overflow-y: auto; padding-right: 10px; margin-bottom: 15px;">
            <table class="detail-table" style="width: 100%; margin-bottom: 20px;">
                <tr>
                    <th style="width: 35%;">Receiver Name:</th>
                    <td id="detailOrderReceiver"></td>
                </tr>
                <tr>
                    <th>Staff Name:</th>
                    <td id="detailOrderStaff"></td>
                </tr>
                <tr>
                    <th>Contact:</th>
                    <td id="detailOrderContact"></td>
                </tr>
                <tr>
                    <th>Payment Method:</th>
                    <td id="detailOrderPaymentMethod"></td>
                </tr>
                <tr>
                    <th>Payment Status:</th>
                    <td id="detailOrderPaymentStatus"></td>
                </tr>
                <tr>
                    <th>Order Status:</th>
                    <td id="detailOrderStatus"></td>
                </tr>
                <tr>
                    <th>Voucher Discount:</th>
                    <td id="detailOrderVoucher"></td>
                </tr>
                <tr>
                    <th>Total Amount:</th>
                    <td id="detailOrderTotal" style="color: #d9534f; font-weight: bold;"></td>
                </tr>
            </table>
            
            <div style="margin-bottom: 10px; font-weight: bold;">Products:</div>
            <div id="detailOrderItems"></div>
        </div>

        <div class="modal-footer" style="margin-top: 0;">
            <button type="button" class="btn-cancel" onclick="closeViewDetailOrderPopup()">Close</button>
        </div>
    </div>
</div>

<script>
    function openViewDetailOrderPopup(orderId) {
        var dataDiv = document.getElementById("orderData_" + orderId);
        if(!dataDiv) return;

        document.getElementById('detailOrderReceiver').innerHTML = dataDiv.querySelector('.o-receiver').innerHTML;
        document.getElementById('detailOrderStaff').innerHTML = dataDiv.querySelector('.o-staff').innerHTML;
        document.getElementById('detailOrderContact').innerHTML = dataDiv.querySelector('.o-contact').innerHTML;
        document.getElementById('detailOrderPaymentMethod').innerHTML = dataDiv.querySelector('.o-payment-method').innerHTML;
        document.getElementById('detailOrderPaymentStatus').innerHTML = dataDiv.querySelector('.o-is-paid').innerHTML;
        document.getElementById('detailOrderStatus').innerHTML = dataDiv.querySelector('.o-status').innerHTML;
        document.getElementById('detailOrderVoucher').innerHTML = dataDiv.querySelector('.o-voucher-discount').innerHTML;
        document.getElementById('detailOrderTotal').innerHTML = dataDiv.querySelector('.o-total').innerHTML;
        document.getElementById('detailOrderItems').innerHTML = dataDiv.querySelector('.o-items').innerHTML;

        document.getElementById('viewDetailOrderPopup').style.display = "flex";
    }

    function closeViewDetailOrderPopup() {
        document.getElementById('viewDetailOrderPopup').style.display = "none";
    }
</script>