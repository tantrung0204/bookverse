<%-- 
    Document   : voucher-detail
    Created on : Feb 10, 2026, 3:27:09 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<h2>Voucher Detail</h2>

<c:if test="${not empty message}">
    <p style="color:red">${message}</p>
</c:if>

<c:if test="${not empty voucher}">
    <table border="1">
        <tr><th>ID</th><td>${voucher.voucherId}</td></tr>
        <tr><th>Code</th><td>${voucher.voucherCode}</td></tr>
        <tr><th>Discount</th><td>${voucher.discountPercent}%</td></tr>
        <tr><th>Quantity</th><td>${voucher.availableQuantity}</td></tr>
        <tr><th>Status</th>
            <td>
                <c:choose>
                    <c:when test="${voucher.status == 1}">Active</c:when>
                    <c:otherwise>Inactive</c:otherwise>
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

        Start Date:
        <input type="date" name="startDate"
               value="${voucher.startDate}"/><br/>

        Expiry Date:
        <input type="date" name="expiryDate"
               value="${voucher.expiryDate}"/><br/>

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

<style>
.modal {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0; top: 0;
    width: 100%; height: 100%;
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
<form action="${pageContext.request.contextPath}/voucher" method="post">
    <input type="hidden" name="action" value="delete"/>
    <input type="hidden" name="id" value="${voucher.voucherId}"/>
    <button type="submit" onclick="return confirm('Delete this voucher?')">
        Delete
    </button>
</form>