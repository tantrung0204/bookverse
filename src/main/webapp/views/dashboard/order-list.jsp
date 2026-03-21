<%-- 
    Document   : order-list
    Created on : Mar 16, 2026, 2:41:58 AM
    Author     : huyqu
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Orders</p>
        <p class="subtitle">Manage and track customer orders efficiently</p>
    </div>

    <div class="content-card">
        <div class="toolbar">
            <form action="${pageContext.request.contextPath}/dashboard/order" method="GET" class="search-form" style="width: 100%; max-width: 400px;">
                <input type="hidden" name="action" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search by Receiver Name, Phone..." value="${searchKeyword}">
                </div>
            </form>
        </div>

        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">${sessionScope.success}</div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.deleteError}">
            <div class="alert alert-danger">${sessionScope.deleteError}</div>
            <c:remove var="deleteError" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${not empty orders}">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Customer/Receiver</th>
                            <th>Contact</th>
                            <th>Order Date</th>
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
                                <td><strong>#O00${o.orderId}</strong></td>
                                <td>${o.receiverName}</td>
                                <td>${o.receiverPhone}</td>
                                <td><fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td style="color: #d9534f; font-weight: bold;">
                                    <fmt:formatNumber value="${o.totalAmount}" type="number" pattern="#,##0"/> VND
                                </td>
                                <td>${o.paymentMethod}</td>
                                <td>
                                    <span class="badge-status ${o.isPaid ? 'badge-active' : 'badge-inactive'}">
                                        ${o.isPaid ? 'Paid' : 'Unpaid'}
                                    </span>
                                </td>
                                <td>
                                    <span style="font-weight: bold; color: ${o.orderStatus == 'Completed' ? '#28a745' : (o.orderStatus == 'Pending' ? '#ffc107' : '#dc3545')};">
                                        ${o.orderStatus}
                                    </span>
                                </td>
                                <td>
                                    <div class="action-buttons">
                                    <button type="button" class="btn-action btn-edit" title="Edit Order"
                                            onclick="openEditOrderPopup('${o.orderId}', '${not empty o.receiverName ? o.receiverName : o.customerId.fullName}', 
                                            '<fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd"/>', 
                                            '${o.totalAmount}', '${o.paymentMethod}', '${o.isPaid}', '${o.orderStatus}', '${not empty o.receiverPhone ? o.receiverPhone : o.customerId.phoneNumber}')">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <c:if test="${o.orderStatus != 'Cancelled'}">
                                            <form action="${pageContext.request.contextPath}/dashboard/order" method="POST" style="display:inline;" onsubmit="return confirm('Are you sure you want to cancel Order #O00${o.orderId}?');">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="orderId" value="${o.orderId}">
                                                <button type="submit" class="btn-action btn-delete" title="Cancel Order">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </c:if>
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
                
            <c:set var="startPage" value="${currentPage - 2}" />
                <c:set var="endPage" value="${currentPage + 2}" />

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                    <c:set var="endPage" value="5" />
                </c:if>

                <c:if test="${endPage > totalPages}">
                    <c:set var="endPage" value="${totalPages}" />
                    <c:set var="startPage" value="${totalPages - 4}" />
                </c:if>

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>

                <c:if test="${totalPages > 1}">
                    <nav class="d-flex justify-content-center" style="margin-top: 20px;">
                        <ul class="pagination">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dashboard/order?action=${not empty searchKeyword ? 'search' : 'list'}${not empty searchKeyword ? '&keyword='.concat(searchKeyword) : ''}&page=${currentPage - 1}">&laquo;</a>
                            </li>

                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/dashboard/order?action=${not empty searchKeyword ? 'search' : 'list'}${not empty searchKeyword ? '&keyword='.concat(searchKeyword) : ''}&page=${i}">${i}</a>
                                </li>
                            </c:forEach>

                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/dashboard/order?action=${not empty searchKeyword ? 'search' : 'list'}${not empty searchKeyword ? '&keyword='.concat(searchKeyword) : ''}&page=${currentPage + 1}">&raquo;</a>
                            </li>
                        </ul>
                    </nav>
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
                    <input type="text" id="editOrderDisplayId" class="form-control" readonly style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
                <div class="form-group" style="flex: 1;">
                    <label>Order Date (Readonly)</label>
                    <input type="text" id="editOrderDate" class="form-control" readonly style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
            </div>

            <div style="display: flex; gap: 20px;">
                <div class="form-group" style="flex: 1;">
                    <label>Customer (Readonly)</label>
                    <input type="text" id="editOrderCustomer" class="form-control" readonly style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
                <div class="form-group" style="flex: 1;">
                    <label>Total Amount (Readonly)</label>
                    <input type="text" id="editOrderTotal" class="form-control" readonly style="background-color: #dfdfdf; cursor: not-allowed;">
                </div>
            </div>

            <div class="form-group">
                <label>Payment Method (Readonly)</label>
                <input type="text" id="editOrderPayment" class="form-control" readonly style="background-color: #dfdfdf; cursor: not-allowed;">
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
                <button type="submit" class="btn-save" style="background-color: #6ea8fe; border: none; padding: 10px 20px; border-radius: 5px; color: white;">Save Changes</button>
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
        if (event.target == modalEdit) {
            closeEditOrderPopup();
        }
    }
</script>