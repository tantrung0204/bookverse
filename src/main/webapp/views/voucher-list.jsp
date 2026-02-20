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


<c:if test="${not empty sessionScope.successMessage}">
    <div style="padding:10px;margin:10px 0;
         background:#d4edda;color:#155724;
         border:1px solid #c3e6cb;border-radius:5px;">
        ${sessionScope.successMessage}
    </div>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty searchMessage}">
    <div style="padding:10px;margin:10px 0;
         background:#f8d7da;color:#721c24;
         border:1px solid #f5c6cb;border-radius:5px;">
        ${searchMessage}
    </div>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div style="padding:10px;margin:10px 0;
         background:#f8d7da;color:#721c24;
         border:1px solid #f5c6cb;border-radius:5px;">
        ${sessionScope.errorMessage}
    </div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>

<c:if test="${not empty editError}">
    <script>
        alert("${editError}");
    </script>
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
                    <button type="button"
                            onclick="location.href = '${pageContext.request.contextPath}/voucher?action=detail&id=${v.voucherId}'">
                        View Detail
                    </button>

                    <!-- EDIT BUTTON -->
                    <button type="button"
                            onclick="openEditModal(
                                            '${v.voucherId}',
                                            '${v.voucherCode}',
                                            '${v.discountPercent}',
                                            '${v.availableQuantity}',
                                            '${v.status}',
                                            '${v.expiryDate}'
                                            )">
                        Edit
                    </button>

                    <!-- DELETE -->
                    <form action="${pageContext.request.contextPath}/voucher"
                          method="post"
                          style="display:inline;">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${v.voucherId}"/>

                        <button type="submit"
                                onclick="return confirm('Delete this voucher?')">
                            Delete
                        </button>
                    </form>


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

        <c:if test="${not empty createError}">
            <p style="color:red">${createError}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/voucher" method="post">
            <input type="hidden" name="action" value="create"/>

            Code:
            <input type="text" name="code" required/><br/><br/>

            Discount (%):
            <input type="number" step="0.1" name="discount" required/><br/><br/>

            Quantity:
            <input type="number" name="quantity" required/><br/><br/>

            Status:
            <select name="status">
                <option value="1">Active</option>
                <option value="0">Inactive</option>
            </select><br/><br/>

            Expiry Date:
            <input type="date" name="expiryDate" required/><br/><br/>

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

<!-- EDIT MODAL -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeEditModal()">&times;</span>

        <h3>Edit Voucher</h3>



        <form action="${pageContext.request.contextPath}/voucher"
              method="post">

            <input type="hidden" name="action" value="edit"/>
            <input type="hidden" id="editId" name="id"/>
            <input type="hidden" id="page" name="page" value="listPage"/>


            Code:
            <input type="text" id="editCode" name="code" required/><br/><br/>

            Discount (%):
            <input type="number" step="0.1"
                   id="editDiscount" name="discount" required/><br/><br/>

            Quantity:
            <input type="number"
                   id="editQuantity" name="quantity" required/><br/><br/>

            Status:
            <select id="editStatus" name="status">
                <option value="1">Active</option>
                <option value="0">Inactive</option>
            </select><br/><br/>

            Expiry Date:
            <input type="date"
                   id="editExpiry" name="expiryDate" required/><br/><br/>

            <button type="submit">Update</button>
        </form>
    </div>
</div>
<script>
    function openEditModal(id, code, discount, quantity, status, expiry) {

        document.getElementById("editId").value = id;
        document.getElementById("editCode").value = code;
        document.getElementById("editDiscount").value = discount;
        document.getElementById("editQuantity").value = quantity;
        document.getElementById("editStatus").value = status;
        document.getElementById("editExpiry").value = expiry;

        document.getElementById("editModal").style.display = "block";
    }

    function closeEditModal() {
        document.getElementById("editModal").style.display = "none";
    }
</script>
<c:if test="${openEdit}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            document.getElementById("editModal").style.display = "block";
        });
    </script>
</c:if>