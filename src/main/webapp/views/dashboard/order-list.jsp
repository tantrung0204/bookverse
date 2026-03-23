<%-- Document : order-list Created on : Mar 16, 2026, 2:41:58 AM Author : huyqu --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/order-list.css">

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
        <c:if test="${not empty sessionScope.deleteError}">
            <div class="alert alert-danger">${sessionScope.deleteError}</div>
            <c:remove var="deleteError" scope="session" />
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
                                    <span
                                        style="font-weight: bold; color: ${o.orderStatus == 'Completed' ? '#28a745' : (o.orderStatus == 'Pending' ? '#ffc107' : '#dc3545')};">
                                        ${o.orderStatus}
                                    </span>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail" title="View Detail"
                                                onclick="openViewDetailOrderPopup('${o.orderId}')">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit"
                                                title="Edit Order" onclick="openEditOrderPopup('${o.orderId}', '${not empty o.receiverName ? o.receiverName : o.customerId.fullName}',
                                                                            '<fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd" />',
                                                                            '${o.totalAmount}', '${o.paymentMethod}', '${o.isPaid}',
                                                                            '${o.orderStatus}', '${not empty o.receiverPhone ?
                                                   o.receiverPhone : o.customerId.phoneNumber}')">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <c:if test="${o.orderStatus != 'Cancelled'}">
                                            <form
                                                action="${pageContext.request.contextPath}/dashboard/order"
                                                method="POST" style="display:inline;"
                                                onsubmit="return confirm('Are you sure you want to cancel Order #O00${o.orderId}?');">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="orderId"
                                                       value="${o.orderId}">
                                                <button type="submit" class="btn-action btn-delete"
                                                        title="Cancel Order">
                                                    <i class="bi bi-trash"></i>
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

                <!-- ... trước -->
                <c:if test="${currentPage > 3}">
                    <span class="page-btn">...</span>
                </c:if>

                <!-- Trang trước current -->
                <c:if test="${currentPage - 1 > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage - 1}"
                       class="page-btn">
                        ${currentPage - 1}
                    </a>
                </c:if>

                <!-- Current -->
                <c:if test="${currentPage != 1 && currentPage != totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage}"
                       class="page-btn active">
                        ${currentPage}
                    </a>
                </c:if>

                <!-- Trang sau current -->
                <c:if test="${currentPage + 1 < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage + 1}"
                       class="page-btn">
                        ${currentPage + 1}
                    </a>
                </c:if>

                <!-- ... sau -->
                <c:if test="${currentPage < totalPages - 2}">
                    <span class="page-btn">...</span>
                </c:if>

                <!-- Last page -->
                <c:if test="${totalPages > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${totalPages}"
                       class="page-btn ${currentPage == totalPages ? 'active' : ''}">
                        ${totalPages}
                    </a>
                </c:if>

                <!-- Next -->
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/order?action=${qAction}${qKeyword}&page=${currentPage + 1}" 
                       class="page-btn">»</a>
                </c:if>
            </div>
        </c:if>

    </div>
</div>

<div id="editOrderPopup" class="modal-overlay" style="display: none;">
    <div class="modal-content" style="max-width: 600px;">
        <div class="modal-header">
            <h3>Edit Order</h3>
            <p style="font-size: 12px; color: gray;">Update Orders information and settings</p>
        </div>

        <form action="${pageContext.request.contextPath}/dashboard/order" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" id="editOrderId" name="orderId">

            <div style="display: flex; gap: 20px;">
                <div class="form-group" style="flex: 1;">
                    <label>Order ID (Readonly)</label>
                    <input type="text" id="editOrderDisplayId" class="form-control" readonly
                           style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
                <div class="form-group" style="flex: 1;">
                    <label>Order Date (Readonly)</label>
                    <input type="text" id="editOrderDate" class="form-control" readonly
                           style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
            </div>

            <div style="display: flex; gap: 20px;">
                <div class="form-group" style="flex: 1;">
                    <label>Customer (Readonly)</label>
                    <input type="text" id="editOrderCustomer" class="form-control" readonly
                           style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
                <div class="form-group" style="flex: 1;">
                    <label>Total Amount (Readonly)</label>
                    <input type="text" id="editOrderTotal" class="form-control" readonly
                           style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
            </div>

            <div class="form-group">
                <label>Payment Method (Readonly)</label>
                <input type="text" id="editOrderPayment" class="form-control" readonly
                       style="background-color: #dfdfdf; cursor: not-allowed;">
            </div>

            <div style="display: flex; gap: 20px;">
                <div class="form-group" style="flex: 1;">
                    <label>Paid Status</label>
                    <select id="editIsPaid" name="isPaid" class="form-control">
                        <option value="1">Paid (Active)</option>
                        <option value="0">Unpaid (Inactive)</option>
                    </select>
                </div>
                <div class="form-group" style="flex: 1;">
                    <label>Order Status (Actions)</label>
                    <select id="editOrderStatus" name="orderStatus" class="form-control">
                        <option value="Pending">Pending</option>
                        <option value="Completed">Completed</option>
                        <option value="Cancelled">Cancelled</option>
                    </select>
                </div>
            </div>

            <div class="modal-footer" style="margin-top: 20px;">
                <button type="button" class="btn-cancel" onclick="closeEditOrderPopup()">Cancel</button>
                <button type="submit" class="btn-save"
                        style="background-color: #6ea8fe; border: none; padding: 10px 20px; border-radius: 5px; color: white;">Save
                    Changes</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openEditOrderPopup(id, customer, date, total, payment, isPaid, status) {
        document.getElementById('editOrderId').value = id;
        document.getElementById('editOrderDisplayId').value = "O00" + id;
        document.getElementById('editOrderCustomer').value = customer;
        document.getElementById('editOrderDate').value = date;
        // Format số tiền (thêm dấu phẩy nếu muốn)
        document.getElementById('editOrderTotal').value = Number(total).toLocaleString('en-US') + " VND";
        document.getElementById('editOrderPayment').value = payment;

        // Gán dropdown
        document.getElementById('editIsPaid').value = isPaid;
        document.getElementById('editOrderStatus').value = status;

        document.getElementById('editOrderPopup').style.display = "flex";
    }

    function closeEditOrderPopup() {
        document.getElementById('editOrderPopup').style.display = "none";
    }

    // Đóng modal khi bấm ra ngoài
    window.onclick = function (event) {
        var modalEdit = document.getElementById("editOrderPopup");
        var modalDetail = document.getElementById("viewDetailOrderPopup");

        if (event.target == modalEdit) {
            closeEditOrderPopup();
        } else if (event.target == modalDetail) {
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