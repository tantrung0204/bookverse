<%-- 
    Document   : suppliers
    Created on : 26 Feb 2026, 18:34:05
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Suppliers</p>
        <p class="subtitle">Create and manage suppliers for your library</p>
    </div>

    <div class="content-card">

        <div class="toolbar">
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Suppliers
            </button>

            <form action="${pageContext.request.contextPath}/supplier" method="get">
                <input type="hidden" name="action" value="search" />
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search supplier..." value="${keyword}">
                </div>
                <button type="submit" hidden></button>
            </form>
        </div>

        <c:choose>
            <c:when test="${not empty suppliers}">
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success">
                        ${successMsg}
                    </div>
                    <c:remove var="successMsg" scope="session" />
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-error">
                        ${errorMsg}
                    </div>
                    <c:remove var="errorMsg" scope="session" />
                </c:if>
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="20%">Supplier Name</th>
                            <th width="15%">Supplier Email</th>
                            <th width="15%">Supplier Phone</th>
                            <th width="35%">Supplier Address</th>
                            <th width="20%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${suppliers}">
                            <tr>
                                <td><strong>${s.supplierId}</strong></td>
                                <td>${s.supplierName}</td>
                                <td>${s.supplierEmail}</td>
                                <td>${s.supplierPhone}</td>
                                <td>${s.supplierAddress}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${s.status == 1}">
                                            <span class="badge-status badge-active">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-inactive">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail"
                                                title="View Detail" onclick="openDetailPopup(
                                                                '${s.supplierId}',
                                                                '${s.supplierName}',
                                                                '${s.supplierEmail}',
                                                                '${s.supplierPhone}'
                                                                '${s.supplierAddress}'
                                                                )">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit" title="Edit"
                                                onclick="openEditPopup(
                                                                '${s.supplierId}',
                                                                '${s.supplierName}',
                                                                '${s.supplierEmail}',
                                                                '${s.supplierPhone}'
                                                                '${s.supplierAddress}'
                                                                '${s.status}'
                                                                )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/supplier"
                                              method="post" style="display:inline;"
                                              onsubmit="return confirmDelete('${s.supplierId}', '${s.supplierName}')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${s.supplierId}">
                                            <button type="submit" class="btn-action btn-delete"
                                                    title="Delete">
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
                    <p class="mt-2">No suppliers found.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty deleteError}">
            <div class="alert alert-danger mt-3 ">${deleteError}</div>
            <c:remove var="deleteError" scope="session" />
        </c:if>
    </div>
</div>

<!-- ================= CREATE POPUP ================= -->
<!--<div id="createPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Add new Category</h3>
        </div>

        <c:if test="${not empty createError}">
            <div class="alert alert-danger" id="createErrorMsg">
                ${createError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/category" method="post">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
                <label>Category Name</label>
                <input type="text" name="categoryName" class="form-control" value="${createName}">
            </div>

            <div class="form-group">
                <label>Description</label>
                <input type="text" name="descriptionText" class="form-control" value="${createDes}">
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="1" ${createStatus==1 ? "selected" : "" }>Active</option>
                    <option value="0" ${createStatus==0 ? "selected" : "" }>Inactive</option>
                </select>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeCreatePopup()">Cancel</button>
                <button type="submit" class="btn-save">Create</button>
            </div>
        </form>
    </div>
</div>

 ================= DETAIL POPUP ================= 
<div id="detailPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Category Detail</h3>
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
                <th>Description:</th>
                <td id="detailDesc"></td>
            </tr>
            <tr>
                <th>Status:</th>
                <td id="detailStatus"></td>
            </tr>
            <tr>
                <th>Quantity:</th>
                <td id="detailQuantity"></td>
            </tr>
        </table>

        <div class="modal-footer">
            <button type="button" class="btn-cancel" onclick="closeDetailPopup()">Close</button>
        </div>
    </div>
</div>
-->
<!--<div id="editPopup" class="modal-overlay">
    <div class="modal-content ">
        <div class="modal-header">
            <h3>Edit Category</h3>
        </div>

        <c:if test="${not empty editError}">
            <div class="alert alert-danger p-2 mb-3 alert-error" id="editErrorMsg" style="font-size: 13px;">
                ${editError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/category" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="categoryId" id="editCategoryId">

            <div class="form-group">
                <label>Category Name</label>
                <input type="text" name="categoryName" id="editCategoryName" class="form-control">
            </div>

            <div class="form-group">
                <label>Description</label>
                <input type="text" name="descriptionText" id="editDescription" class="form-control">
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" id="editStatus" class="form-control">
                    <option value="1">Active</option>
                    <option value="0">Inactive</option>
                </select>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeEditPopup()">Cancel</button>
                <button type="submit" class="btn-save">Save Changes</button>
            </div>
        </form>
    </div>
</div>-->

<!--<script>
    function openCreatePopup() {
        document.getElementById("createPopup").style.display = "flex";
        const err = document.getElementById("createErrorMsg");
        if (err)
            err.style.display = 'none';
    }
    function closeCreatePopup() {
        document.getElementById("createPopup").style.display = "none";
    }

    function openDetailPopup(id, name, desc, status) {
        document.getElementById("detailId").innerText = id;
        document.getElementById("detailName").innerText = name;
        document.getElementById("detailDesc").innerText = desc;
        document.getElementById("detailStatus").innerText = status == 1 ? "Active" : "Inactive";
        document.getElementById("detailQuantity").innerText = "Loading...";

        fetch('${pageContext.request.contextPath}/category?action=detail&categoryId=' + id)
                .then(response => response.json())
                .then(data => {
                    document.getElementById("detailQuantity").innerText = data.quantity + " products";
                })
                .catch(error => {
                    document.getElementById("detailQuantity").innerText = "Error";
                });


        document.getElementById("detailPopup").style.display = "flex";
    }
    function closeDetailPopup() {
        document.getElementById("detailPopup").style.display = "none";
    }
    // Hàm mở Popup và điền dữ liệu
    function openEditPopup(id, name, description, status) {
        document.getElementById("editCategoryId").value = id;
        document.getElementById("editCategoryName").value = name;
        document.getElementById("editDescription").value = description;
        document.getElementById("editStatus").value = status;

        // Ẩn thông báo lỗi cũ nếu có
        const err = document.getElementById("editErrorMsg");
        if (err)
            err.style.display = 'none';

        // Hiển thị modal (sử dụng Flex để căn giữa)
        document.getElementById("editPopup").style.display = "flex";
    }

    // Hàm đóng Popup
    function closeEditPopup() {
        document.getElementById("editPopup").style.display = "none";
    }

    // Xử lý confirm xóa
    function confirmDelete(id, name) {
        return confirm("Are you sure you want to delete category:\n" + name + " (ID: " + id + ")");
    }

    let popupTimer = null;


    // Đóng Popup khi click ra ngoài vùng trắng
    window.onclick = function (event) {
        var modal = document.getElementById("editPopup");
        if (event.target == modal) {
            closeEditPopup();
        }
    }
</script>-->

<%--<c:if test="${openCreatePopup}">
    <script>
        window.onload = function () {
            setTimeout(function () {
                openCreatePopup(
                        '${createName}',
                        '${createDesc}',
                        '${createStatus}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("createErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>--%>

<%--<c:if test="${openEditPopup}">
    <script>
        window.onload = function () {
            // Đảm bảo DOM đã load xong
            setTimeout(function () {
                openEditPopup(
                        '${editId}',
                        '${editName}',
                        '${editDesc}',
                        '${editStatus}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("editErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>--%>
