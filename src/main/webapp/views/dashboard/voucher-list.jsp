<%-- Document : voucher-list Created on : Feb 10, 2026, 2:37:07 PM Author : Admin --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/voucher-list.css">

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

            <form class="search-form" action="${pageContext.request.contextPath}/dashboard/voucher" method="get">
                <input type="hidden" name="action" value="search" />
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" value="${param.keyword}"
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
            <c:remove var="successMessage" scope="session" />
        </c:if>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div style="padding:10px;margin:10px 0;
                 background:#f8d7da;color:#721c24;
                 border:1px solid #f5c6cb;border-radius:5px;">
                ${sessionScope.errorMessage}
            </div>
            <c:remove var="errorMessage" scope="session" />
        </c:if>


        <c:choose>

            <c:when test="${not empty vouchers}">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="20%">Name</th>
                            <th width="10%">Code</th>
                            <th width="15%">Discount</th>
                            <th width="10%">Quantity</th>
                            <th width="15%">Status</th>
                            <th width="20%">Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="v" items="${vouchers}">
                            <tr>
                                <td><strong>${v.voucherId}</strong></td>
                                <td>${v.voucherName}</td>
                                <td>${v.voucherCode}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.discountType == 1}">
                                            ${v.discountValue.intValue()}%
                                        </c:when>
                                        <c:otherwise>
                                            ${v.discountValue.intValue()}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
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
                                        <button class="btn-action btn-detail" title="View Detail" onclick="openDetailModal(
                                                        '${v.voucherId}',
                                                        '${v.voucherName}',
                                                        '${v.voucherCode}',
                                                        '${v.minOrderValue.intValue()}',
                                                        '${v.discountType}',
                                                        '${v.discountValue.intValue()}',
                                                        '${v.availableQuantity}',
                                                        '${v.status}',
                                                        '${v.startDate}',
                                                        '${v.expiryDate}',
                                                        '${v.usedCount}'
                                                        )">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <!-- EDIT -->
                                        <button class="btn-action btn-edit" title="Edit" onclick="openEditModal(
                                                        '${v.voucherId}',
                                                        '${v.voucherName}',
                                                        '${v.voucherCode}',
                                                        '${v.minOrderValue.intValue()}',
                                                        '${v.discountType}',
                                                        '${v.discountValue.intValue()}',
                                                        '${v.availableQuantity}',
                                                        '${v.status}',
                                                        '${v.expiryDate}'
                                                        )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <!-- DELETE -->
                                        <form action="${pageContext.request.contextPath}/dashboard/voucher"
                                              method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id" value="${v.voucherId}" />

                                            <button type="submit" class="btn-action btn-delete"
                                                    title="Delete"
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
            </c:when>

            <c:otherwise>
                <div style="text-align:center; padding: 40px; color: #999;">
                    <i class="bi bi-inbox" style="font-size: 40px;"></i>
                    <p class="mt-2">No notifications found.</p>
                </div>
            </c:otherwise>

        </c:choose>

        <c:set var="currentAction" value="${param.action == 'search' ? 'search' : 'list'}" />
        <c:set var="keywordParam" value="${param.keyword}" />

        <c:set var="startPage" value="${currentPage - 1}" />
        <c:set var="endPage" value="${currentPage + 1}" />

        <c:if test="${startPage < 2}">
            <c:set var="startPage" value="2"/>
            <c:set var="endPage" value="4"/>
        </c:if>

        <c:if test="${endPage > totalPages - 1}">
            <c:set var="endPage" value="${totalPages - 1}"/>
            <c:set var="startPage" value="${totalPages - 3}"/>
        </c:if>

        <c:if test="${startPage < 2}">
            <c:set var="startPage" value="2"/>
        </c:if>


        <c:if test="${totalPages > 1}">
            <div class="pagination">

                <!-- Prev -->
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/voucher?action=${currentAction}&keyword=${keywordParam}&page=${currentPage - 1}"
                       class="page-btn">«</a>
                </c:if>

                <!-- Nếu <=5 trang -->
                <c:if test="${totalPages <= 5}">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="${pageContext.request.contextPath}/dashboard/voucher?action=${currentAction}&keyword=${keywordParam}&page=${i}"
                           class="page-btn ${i == currentPage ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>
                </c:if>

                <!-- Nếu >5 trang -->
                <c:if test="${totalPages > 5}">

                    <!-- Page 1 -->
                    <a href="${pageContext.request.contextPath}/dashboard/voucher?action=${currentAction}&keyword=${keywordParam}&page=1"
                       class="page-btn ${currentPage == 1 ? 'active' : ''}">
                        1
                    </a>

                    <!-- ... -->
                    <c:if test="${startPage > 2}">
                        <span class="page-btn disabled">...</span>
                    </c:if>

                    <!-- Middle pages -->
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="${pageContext.request.contextPath}/dashboard/voucher?action=${currentAction}&keyword=${keywordParam}&page=${i}"
                           class="page-btn ${i == currentPage ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                    <!-- ... -->
                    <c:if test="${endPage < totalPages - 1}">
                        <span class="page-btn disabled">...</span>
                    </c:if>

                    <!-- Last page -->
                    <a href="${pageContext.request.contextPath}/dashboard/voucher?action=${currentAction}&keyword=${keywordParam}&page=${totalPages}"
                       class="page-btn ${currentPage == totalPages ? 'active' : ''}">
                        ${totalPages}
                    </a>

                </c:if>

                <!-- Next -->
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/voucher?action=${currentAction}&keyword=${keywordParam}&page=${currentPage + 1}"
                       class="page-btn">»</a>
                </c:if>

            </div>
        </c:if>



        <!-- CREATE VOUCHER POPUP -->
        <div id="createModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Add new Voucher</h3>
                </div>

                <c:if test="${not empty createError}">
                    <div class="alert alert-danger p-2 mb-3" id="createErrorMsg" style="font-size:13px;">
                        ${createError}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/dashboard/voucher" method="post">

                    <input type="hidden" name="action" value="create" />


                    <!-- VOUCHER NAME -->
                    <div class="form-group">
                        <label>Voucher Name</label>
                        <input type="text" name="voucherName" class="form-control" value="${voucherName}"
                               required>
                    </div>

                    <!-- CODE -->
                    <div class="form-group">
                        <label>Code</label>
                        <input type="text" name="code" class="form-control" value="${code}" required>
                    </div>

                    <!-- MIN ORDER VALUE -->
                    <div class="form-group">
                        <label>Minimum Order Value</label>
                        <input type="number" step="0.01" name="minOrderValue" class="form-control"
                               value="${minOrderValue}" required>
                    </div>

                    <!-- DISCOUNT TYPE -->
                    <div class="form-group">
                        <label>Discount Type</label>
                        <select name="discountType" id="discountType" class="form-control"
                                onchange="handleDiscountTypeChange()">
                            <option value="1" ${discountType=='1' ? 'selected' : '' }>
                                Percent (%)
                            </option>
                            <option value="2" ${discountType=='2' ? 'selected' : '' }>
                                Fixed Amount
                            </option>
                        </select>
                    </div>

                    <!-- DISCOUNT VALUE -->
                    <div class="form-group">
                        <label id="discountLabel">Discount Value</label>
                        <input type="number" step="0.01" name="discount" id="discountInput"
                               class="form-control" value="${discount}" required>
                    </div>

                    <!-- QUANTITY -->
                    <div class="form-group">
                        <label>Quantity</label>
                        <input type="number" name="quantity" class="form-control" value="${quantity}"
                               required>
                    </div>

                    <!-- STATUS -->
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status" class="form-control">
                            <option value="1" <c:if test="${status == '1'}">selected</c:if>>Active</option>
                            <option value="0" <c:if test="${status == '0'}">selected</c:if>>Inactive
                                </option>
                            </select>
                        </div>

                        <!-- EXPIRY -->
                        <div class="form-group">
                            <label>Expiry Date</label>
                            <input type="date" name="expiryDate" class="form-control" value="${expiryDate}"
                               required>
                    </div>

                    <!-- FOOTER -->
                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeCreateModal()">
                            Cancel
                        </button>

                        <button type="submit" class="btn-save">
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

        <script>
            function handleDiscountTypeChange() {
                const type = document.getElementById("discountType").value;
                const input = document.getElementById("discountInput");
                const label = document.getElementById("discountLabel");

                if (type == "1") {
                    label.innerText = "Discount Percent (%)";
                    input.max = 100;
                    input.min = 0.01;
                } else {
                    label.innerText = "Discount Amount";
                    input.removeAttribute("max");
                    input.min = 0.01;
                }
            }

            document.addEventListener("DOMContentLoaded", handleDiscountTypeChange);
        </script>

        <!-- EDIT VOUCHER POPUP -->
        <div id="editModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Edit Voucher</h3>
                </div>

                <c:if test="${not empty editError}">
                    <div class="alert alert-danger p-2 mb-3" style="font-size:13px;">
                        ${editError}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/dashboard/voucher" method="post">

                    <input type="hidden" name="action" value="edit" />
                    <input type="hidden" id="editId" name="id" value="${voucher.voucherId}" />

                    <!-- NAME -->
                    <div class="form-group">
                        <label>Voucher Name</label>
                        <input type="text" id="editName" name="voucherName" class="form-control"
                               value="${voucher.voucherName}" required>
                    </div>

                    <!-- CODE -->
                    <div class="form-group">
                        <label>Code</label>
                        <input type="text" id="editCode" name="code" class="form-control"
                               value="${voucher.voucherCode}" required>
                    </div>

                    <!-- MIN ORDER VALUE -->
                    <div class="form-group">
                        <label>Minimum Order Value</label>
                        <input type="number" step="0.01" id="editMinOrder" name="minOrderValue"
                               class="form-control" value="${voucher.minOrderValue.intValue()}" required>
                    </div>

                    <!-- DISCOUNT TYPE -->
                    <div class="form-group">
                        <label>Discount Type</label>
                        <select id="editDiscountType" name="discountType" class="form-control"
                                onchange="handleEditDiscountTypeChange()">

                            <option value="1" ${voucher.discountType==1 ? 'selected' : '' }>
                                Percent (%)
                            </option>

                            <option value="2" ${voucher.discountType==2 ? 'selected' : '' }>
                                Fixed Amount
                            </option>

                        </select>
                    </div>

                    <!-- DISCOUNT VALUE -->
                    <div class="form-group">
                        <label id="editDiscountLabel">Discount Value</label>
                        <input type="number" step="0.01" id="editDiscount" name="discount"
                               class="form-control" value="${voucher.discountValue.intValue()}" required>
                    </div>

                    <!-- QUANTITY -->
                    <div class="form-group">
                        <label>Quantity</label>
                        <input type="number" id="editQuantity" name="quantity" class="form-control"
                               value="${voucher.availableQuantity}" required>
                    </div>

                    <!-- STATUS -->
                    <div class="form-group">
                        <label>Status</label>
                        <select id="editStatus" name="status" class="form-control">
                            <option value="1" ${voucher.status==1 ? 'selected' : '' }>
                                Active
                            </option>
                            <option value="0" ${voucher.status==0 ? 'selected' : '' }>
                                Inactive
                            </option>
                        </select>
                    </div>

                    <!-- EXPIRY -->
                    <div class="form-group">
                        <label>Expiry Date</label>
                        <input type="date" id="editExpiry" name="expiryDate" class="form-control"
                               value="${voucher.expiryDate}" required>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn-cancel" onclick="closeEditModal()">
                            Cancel
                        </button>

                        <button type="submit" class="btn-save">
                            Save Changes
                        </button>
                    </div>

                </form>
            </div>
        </div>
        <script>
            function handleEditDiscountTypeChange() {
                const type = document.getElementById("editDiscountType").value;
                const label = document.getElementById("editDiscountLabel");
                const input = document.getElementById("editDiscount");

                if (type == "1") {
                    label.innerText = "Discount Percent (%)";
                    input.max = 100;
                    input.min = 0.01;
                } else {
                    label.innerText = "Discount Amount";
                    input.removeAttribute("max");
                    input.min = 0.01;
                }
            }

            function openEditModal(id, name, code, minOrder, type, discount, quantity, status, expiry) {

                document.getElementById("editId").value = id;
                document.getElementById("editName").value = name;
                document.getElementById("editCode").value = code;
                document.getElementById("editMinOrder").value = minOrder;
                document.getElementById("editDiscountType").value = type;
                document.getElementById("editDiscount").value = discount;
                document.getElementById("editQuantity").value = quantity;
                document.getElementById("editStatus").value = status;
                document.getElementById("editExpiry").value = expiry.split(" ")[0];

                handleEditDiscountTypeChange();

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
                    handleEditDiscountTypeChange();
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
                    <tr>
                        <th>ID:</th>
                        <td id="detailId"></td>
                    </tr>
                    <tr>
                        <th>Name:</th>
                        <td id="detailName"></td>
                    </tr>
                    <tr>
                        <th>Code:</th>
                        <td id="detailCode"></td>
                    </tr>
                    <tr>
                        <th>Minimum Order:</th>
                        <td id="detailMinOrder"></td>
                    </tr>
                    <tr>
                        <th>Type:</th>
                        <td id="detailType"></td>
                    </tr>
                    <tr>
                        <th>Discount:</th>
                        <td id="detailDiscount"></td>
                    </tr>
                    <tr>
                        <th>Quantity:</th>
                        <td id="detailQuantity"></td>
                    </tr>
                    <tr>
                        <th>Status:</th>
                        <td id="detailStatus"></td>
                    </tr>
                    <tr>
                        <th>Start Date:</th>
                        <td id="detailStart"></td>
                    </tr>
                    <tr>
                        <th>Expiry Date:</th>
                        <td id="detailExpiry"></td>
                    </tr>
                    <tr>
                        <th>Used:</th>
                        <td id="detailUsed"></td>
                    </tr>
                </table>

                <div class="modal-footer">
                    <button class="btn-cancel" onclick="closeDetailModal()">Close</button>
                </div>

            </div>
        </div>

        <script>
            function openDetailModal(id, name, code, minOrder, type, discount, quantity, status, start, expiry, used) {

                document.getElementById("detailId").innerText = id;
                document.getElementById("detailName").innerText = name;
                document.getElementById("detailCode").innerText = code;
                document.getElementById("detailMinOrder").innerText = minOrder;
                // Hiển thị Type đẹp hơn
                if (type == 1) {
                    document.getElementById("detailType").innerText = "Percent (%)";
                    document.getElementById("detailDiscount").innerText = discount + "%";
                } else {
                    document.getElementById("detailType").innerText = "Fixed Amount";
                    document.getElementById("detailDiscount").innerText = discount;
                }
                document.getElementById("detailQuantity").innerText = quantity;
                document.getElementById("detailStatus").innerText =
                        status == 1 ? "Active" : "Inactive";

                document.getElementById("detailStart").innerText = start;
                document.getElementById("detailExpiry").innerText = expiry;
                document.getElementById("detailUsed").innerText = used;

                document.getElementById("detailModal").style.display = "flex";
            }

            function closeDetailModal() {
                document.getElementById("detailModal").style.display = "none";
            }
        </script>