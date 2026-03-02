<%-- 
    Document   : voucher-list
    Created on : Feb 10, 2026, 2:37:07 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"href="${pageContext.request.contextPath}/styles/voucher-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Vouchers</p>
        <p class="subtitle">Create and manage discount vouchers</p>
    </div>

    <div class="content-card">

        <div class="toolbar">
            <button class="btn-add" onclick="openPopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Voucher
            </button>

            <form class="search-form"
                  action="${pageContext.request.contextPath}/voucher"
                  method="get">
                <input type="hidden" name="action" value="search"/>
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword"
                           placeholder="Search vouchers...">
                </div>
            </form>
        </div>


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


        <c:if test="${not empty vouchers}">
            <table class="custom-table">
                <thead>
                    <tr>
                        <th width="10%">ID</th>
                        <th width="20%">Code</th>
                        <th width="15%">Discount</th>
                        <th width="15%">Quantity</th>
                        <th width="15%">Status</th>
                        <th width="25%">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="v" items="${vouchers}">
                        <tr>
                            <td><strong>${v.voucherId}</strong></td>
                            <td>${v.voucherCode}</td>
                            <td>${v.discountPercent}%</td>
                            <td>${v.availableQuantity}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${v.status == 1}">
                                        <span class="badge-status badge-active">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-status badge-inactive">Inactive</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <div class="action-buttons">

                                    <!-- VIEW -->
                                    <button class="btn-action btn-detail"
                                            onclick="openDetailModal(
                                                            '${v.voucherId}',
                                                            '${v.voucherCode}',
                                                            '${v.discountPercent}',
                                                            '${v.availableQuantity}',
                                                            '${v.status}',
                                                            '${v.startDate}',
                                                            '${v.expiryDate}'
                                                            )">
                                        <i class="bi bi-eye"></i>
                                    </button>

                                    <!-- EDIT -->
                                    <button class="btn-action btn-edit"
                                            onclick="openEditModal(
                                                            '${v.voucherId}',
                                                            '${v.voucherCode}',
                                                            '${v.discountPercent}',
                                                            '${v.availableQuantity}',
                                                            '${v.status}',
                                                            '${v.expiryDate}'
                                                            )">
                                        <i class="bi bi-pencil"></i>
                                    </button>

                                    <!-- DELETE -->
                                    <form action="${pageContext.request.contextPath}/voucher"
                                          method="post"
                                          style="display:inline;">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="id" value="${v.voucherId}"/>

                                        <button type="submit"
                                                class="btn-action btn-delete"
                                                onclick="return confirm('Delete this voucher?')">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>

                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- CREATE VOUCHER POPUP -->
        <div id="createModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Create Voucher</h3>
                </div>

                <c:if test="${not empty createError}">
                    <div class="alert alert-danger p-2 mb-3"
                         id="createErrorMsg"
                         style="font-size:13px;">
                        ${createError}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/voucher"
                      method="post">

                    <input type="hidden" name="action" value="create"/>

                    <!-- CODE -->
                    <div class="form-group">
                        <label>Code</label>
                        <input type="text"
                               name="code"
                               class="form-control"
                               required>
                    </div>

                    <!-- DISCOUNT -->
                    <div class="form-group">
                        <label>Discount (%)</label>
                        <input type="number"
                               step="0.1"
                               name="discount"
                               class="form-control"
                               required>
                    </div>

                    <!-- QUANTITY -->
                    <div class="form-group">
                        <label>Quantity</label>
                        <input type="number"
                               name="quantity"
                               class="form-control"
                               required>
                    </div>

                    <!-- STATUS -->
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status"
                                class="form-control">
                            <option value="1">Active</option>
                            <option value="0">Inactive</option>
                        </select>
                    </div>

                    <!-- EXPIRY -->
                    <div class="form-group">
                        <label>Expiry Date</label>
                        <input type="date"
                               name="expiryDate"
                               class="form-control"
                               required>
                    </div>

                    <!-- FOOTER -->
                    <div class="modal-footer">
                        <button type="button"
                                class="btn-cancel"
                                onclick="closeCreateModal()">
                            Cancel
                        </button>

                        <button type="submit"
                                class="btn-save">
                            Create
                        </button>
                    </div>

                </form>
            </div>
        </div>
        <script>
            function openPopup() {
                document.getElementById("createModal").style.display = "flex";
            }

            function closeCreateModal() {
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
            .modal-overlay {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
                justify-content: center;
                align-items: center;
            }

            .modal-content {
                background: #fff;
                width: 500px;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.15);
            }

            .modal-header h3 {
                margin-bottom: 20px;
            }

            .form-group {
                margin-bottom: 18px;
            }

            .form-group label {
                display: block;
                margin-bottom: 6px;
                font-weight: 500;
            }

            .form-control {
                width: 100%;
                padding: 10px;
                border-radius: 8px;
                border: 1px solid #ddd;
            }

            .modal-footer {
                display: flex;
                justify-content: flex-end;
                gap: 10px;
                margin-top: 20px;
            }

            .btn-cancel {
                padding: 8px 18px;
                background: #e9ecef;
                border: none;
                border-radius: 8px;
            }

            .btn-save {
                padding: 8px 18px;
                background: #a67c52;
                color: white;
                border: none;
                border-radius: 8px;
            }
            .close {
                float: right;
                font-size: 22px;
                cursor: pointer;
            }
        </style>

        <!-- EDIT VOUCHER POPUP -->
        <div id="editModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Edit Voucher</h3>
                </div>

                <c:if test="${not empty editError}">
                    <div class="alert alert-danger p-2 mb-3" 
                         id="editErrorMsg" 
                         style="font-size:13px;">
                        ${editError}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/voucher"
                      method="post">

                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" id="editId" name="id"
                           value="${voucher.voucherId}"/>

                    <!-- CODE -->
                    <div class="form-group">
                        <label>Code</label>
                        <input type="text"
                               id="editCode"
                               name="code"
                               class="form-control"
                               value="${voucher.voucherCode}"
                               required>
                    </div>

                    <!-- DISCOUNT -->
                    <div class="form-group">
                        <label>Discount (%)</label>
                        <input type="number"
                               step="0.1"
                               id="editDiscount"
                               name="discount"
                               class="form-control"
                               value="${voucher.discountPercent}"
                               required>
                    </div>

                    <!-- QUANTITY -->
                    <div class="form-group">
                        <label>Quantity</label>
                        <input type="number"
                               id="editQuantity"
                               name="quantity"
                               class="form-control"
                               value="${voucher.availableQuantity}"
                               required>
                    </div>

                    <!-- STATUS -->
                    <div class="form-group">
                        <label>Status</label>
                        <select id="editStatus"
                                name="status"
                                class="form-control">
                            <option value="1"
                                    ${voucher.status == 1 ? 'selected' : ''}>
                                Active
                            </option>
                            <option value="0"
                                    ${voucher.status == 0 ? 'selected' : ''}>
                                Inactive
                            </option>
                        </select>
                    </div>

                    <!-- EXPIRY -->
                    <div class="form-group">
                        <label>Expiry Date</label>
                        <input type="date"
                               id="editExpiry"
                               name="expiryDate"
                               class="form-control"
                               value="${voucher.expiryDate}"
                               required>
                    </div>

                    <!-- FOOTER -->
                    <div class="modal-footer">
                        <button type="button"
                                class="btn-cancel"
                                onclick="closeEditModal()">
                            Cancel
                        </button>

                        <button type="submit"
                                class="btn-save">
                            Save Changes
                        </button>
                    </div>

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
                document.getElementById("editExpiry").value = expiry.split(" ")[0];
                document.getElementById("editModal").style.display = "flex";
            }

            function closeEditModal() {
                document.getElementById("editModal").style.display = "none";
            }
        </script>
        <c:if test="${openEdit}">
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    document.getElementById("editModal").style.display = "flex";
                });
            </script>
        </c:if>

        <!-- DETAIL VOUCHER POPUP -->
        <div id="detailModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Voucher Detail</h3>
                </div>

                <table class="detail-table">
                    <tr><th>ID:</th><td id="detailId"></td></tr>
                    <tr><th>Code:</th><td id="detailCode"></td></tr>
                    <tr><th>Discount:</th><td id="detailDiscount"></td></tr>
                    <tr><th>Quantity:</th><td id="detailQuantity"></td></tr>
                    <tr><th>Status:</th><td id="detailStatus"></td></tr>
                    <tr><th>Start Date:</th><td id="detailStart"></td></tr>
                    <tr><th>Expiry Date:</th><td id="detailExpiry"></td></tr>
                </table>

                <div class="modal-footer">
                    <button class="btn-cancel" onclick="closeDetailModal()">Close</button>
                </div>

            </div>
        </div>

        <script>
            function openDetailModal(id, code, discount, quantity, status, start, expiry) {

                document.getElementById("detailId").innerText = id;
                document.getElementById("detailCode").innerText = code;
                document.getElementById("detailDiscount").innerText = discount + "%";
                document.getElementById("detailQuantity").innerText = quantity;
                document.getElementById("detailStatus").innerText =
                        status == 1 ? "Active" : "Inactive";

                document.getElementById("detailStart").innerText = start;
                document.getElementById("detailExpiry").innerText = expiry;

                document.getElementById("detailModal").style.display = "flex";
            }

            function closeDetailModal() {
                document.getElementById("detailModal").style.display = "none";
            }
        </script>