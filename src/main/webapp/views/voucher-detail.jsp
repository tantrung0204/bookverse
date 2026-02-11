<%-- 
    Document   : voucher-detail
    Created on : Feb 10, 2026, 3:27:09 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<h2>Voucher Detail</h2>

<c:if test="${not empty sessionScope.message}">
    <div style="padding:10px;margin:10px 0;
         background:#d4edda;color:#155724;
         border:1px solid #c3e6cb;border-radius:5px;">
        ${sessionScope.message}
    </div>
    <c:remove var="message" scope="session"/>
</c:if>

<c:if test="${not empty message}">
    <div style="padding:10px;margin:10px 0;
         background:#f8d7da;color:#721c24;
         border:1px solid #f5c6cb;border-radius:5px;">
        ${message}
    </div>
</c:if>

<c:if test="${not empty voucher}">
    <table border="1">
        <tr><th>ID</th><td>${voucher.voucherId}</td></tr>
        <tr><th>Code</th><td>${voucher.voucherCode}</td></tr>
        <tr><th>Discount</th><td>${voucher.discountPercent}%</td></tr>
        <tr><th>Quantity</th><td>${voucher.availableQuantity}</td></tr>
        <tr>
            <th>Status</th>
            <td>
                <c:choose>
                    <c:when test="${voucher.status == 1}">
                        Active
                    </c:when>
                    <c:otherwise>
                        Inactive
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr><th>Start Date</th><td>${voucher.startDate}</td></tr>
        <tr><th>Expiry Date</th><td>${voucher.expiryDate}</td></tr>
    </table>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/voucher">
    ← Back to list
</a>
<button onclick="openEdit()">Edit</button>

<%-- Edit --%>
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeEdit()">&times;</span>

        <h3>Edit Voucher</h3>

        <form action="${pageContext.request.contextPath}/voucher" method="post">
            <input type="hidden" name="action" value="edit"/>
            <input type="hidden" name="id" value="${voucher.voucherId}"/>

            Code:
            <input type="text" name="code" value="${voucher.voucherCode}" required/><br/>

            Discount:
            <input type="number" step="0.1" name="discount"
                   value="${voucher.discountPercent}" required/><br/>

            Quantity:
            <input type="number" name="quantity"
                   value="${voucher.availableQuantity}" required/><br/>

            Status:
            <select name="status">
                <option value="1" ${voucher.status==1?'selected':''}>Active</option>
                <option value="0" ${voucher.status==0?'selected':''}>Inactive</option>
            </select><br/>

            Expiry Date:
            <input type="date" name="expiryDate" required/><br/>

            <button type="submit">Save</button>
        </form>
    </div>
</div>
<script>
    function openEdit() {
        document.getElementById("editModal").style.display = "block";
    }
    function closeEdit() {
        document.getElementById("editModal").style.display = "none";
    }
</script>

<c:if test="${openEdit}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            openEdit();
        });
    </script>
</c:if>

<style>
    .modal {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0,0,0,0.5);
    }

    .modal-content {
        background: #fff;
        width: 400px;
        margin: 10% auto;
        padding: 20px;
        border-radius: 8px;
    }

    .close {
        float: right;
        font-size: 22px;
        cursor: pointer;
    }
</style>

<%-- Delete --%>
<form action="${pageContext.request.contextPath}/voucher" method="post">
    <input type="hidden" name="action" value="delete"/>
    <input type="hidden" name="id" value="${voucher.voucherId}"/>
    <button type="submit" onclick="return confirm('Delete this voucher?')">
        Delete
    </button>
</form>