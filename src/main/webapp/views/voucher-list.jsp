<%-- 
    Document   : voucher-list
    Created on : Feb 10, 2026, 2:37:07 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Voucher Management</h2>

<button onclick="openPopup()">+ Create Voucher</button> <%-- button của create --%>

<form action="${pageContext.request.contextPath}/voucher" method="get">
    <input type="hidden" name="action" value="search"/>
    <input name="keyword">
    <button type="submit">Search</button>
</form>


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


<c:if test="${not empty vouchers}">
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Code</th>
            <th>Discount (%)</th>
            <th>Quantity</th>
            <th>Status</th>
            <th>Action</th>
        </tr>

        <c:forEach var="v" items="${vouchers}"> 
            <tr>
                <td>${v.voucherId}</td>
                <td>${v.voucherCode}</td>
                <td>${v.discountPercent}</td>
                <td>${v.availableQuantity}</td>
                <td>
                    <c:choose>
                        <c:when test="${v.status == 1}">Active</c:when>
                        <c:otherwise>Inactive</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/voucher?action=detail&id=${v.voucherId}">
                        View Detail
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%-- Create --%>
<div id="createModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closePopup()">&times;</span>

        <h3>Create Voucher</h3>

        <c:if test="${not empty message}">
            <p style="color:red">${message}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/voucher" method="post">
            <input type="hidden" name="action" value="create"/>

            Code:
            <input type="text" name="code" required/><br/>

            Discount (%):
            <input type="number" step="0.1" name="discount" required/><br/>

            Quantity:
            <input type="number" name="quantity" required/><br/>

            Status:
            <select name="status">
                <option value="1">Active</option>
                <option value="0">Inactive</option>
            </select><br/>

            Expiry Date:
            <input type="date" name="expiryDate" required/><br/>

            <button type="submit">Create</button>
        </form>
    </div>
</div>
<script>
    function openPopup() {
        document.getElementById("createModal").style.display = "block";
    }

    function closePopup() {
        document.getElementById("createModal").style.display = "none";
    }
</script>

<c:if test="${openCreate}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            openPopup();
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
