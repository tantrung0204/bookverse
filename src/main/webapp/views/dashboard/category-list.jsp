<%-- Document : category-list 
Created on : 10 Feb 2026, 18:33:04 
Author : NganTTK-CE190411 --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Categories</p>
        <p class="subtitle">Create and manage book categories for your library</p>
    </div>

    <div class="content-card">

        <div class="toolbar">
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Category
            </button>

            <form action="${pageContext.request.contextPath}/dashboard/category" method="get">
                <input type="hidden" name="action" value="search" />
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search categories..." value="${keyword}">
                </div>
                <button type="submit" hidden></button>
            </form>
        </div>

        <c:choose>
            <c:when test="${not empty categories}">
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
                            <th width="20%">Category Name</th>
                            <th width="35%">Description</th>
                            <th width="15%">Status</th>
                            <th width="20%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${categories}">
                            <tr>
                                <td><strong>${c.categoryId}</strong></td>
                                <td>${c.categoryName}</td>
                                <td>${c.descriptionText}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${c.status == 1}">
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
                                                                '${c.categoryId}',
                                                                '${c.categoryName}',
                                                                '${c.descriptionText}',
                                                                '${c.status}'
                                                                )">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit" title="Edit"
                                                onclick="openEditPopup(
                                                                '${c.categoryId}',
                                                                '${c.categoryName}',
                                                                '${c.descriptionText}',
                                                                '${c.status}',
                                                                '${c.parent.categoryId}'
                                                                )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/dashboard/category"
                                              method="post" style="display:inline;"
                                              onsubmit="return confirmDelete('${c.categoryId}', '${c.categoryName}')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${c.categoryId}">
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


                <c:if test="${totalPages > 1}">
                    <div class="pagination">

                        <!-- Previous -->
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/dashboard/category?page=${currentPage - 1}" 
                               class="page-btn">«</a>
                        </c:if>

                        <!-- Page 1 -->
                        <a href="${pageContext.request.contextPath}/dashboard/category?page=1"
                           class="page-btn ${currentPage == 1 ? 'active' : ''}">
                            1
                        </a>

                        <!-- ... trước -->
                        <c:if test="${currentPage > 3}">
                            <span class="page-btn">...</span>
                        </c:if>

                        <!-- Trang trước current -->
                        <c:if test="${currentPage - 1 > 1}">
                            <a href="${pageContext.request.contextPath}/dashboard/category?page=${currentPage - 1}"
                               class="page-btn">
                                ${currentPage - 1}
                            </a>
                        </c:if>

                        <!-- Current -->
                        <c:if test="${currentPage != 1 && currentPage != totalPages}">
                            <a href="${pageContext.request.contextPath}/dashboard/category?page=${currentPage}"
                               class="page-btn active">
                                ${currentPage}
                            </a>
                        </c:if>

                        <!-- Trang sau current -->
                        <c:if test="${currentPage + 1 < totalPages}">
                            <a href="${pageContext.request.contextPath}/dashboard/category?page=${currentPage + 1}"
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
                            <a href="${pageContext.request.contextPath}/dashboard/category?page=${totalPages}"
                               class="page-btn ${currentPage == totalPages ? 'active' : ''}">
                                ${totalPages}
                            </a>
                        </c:if>

                        <!-- Next -->
                        <c:if test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/dashboard/category?page=${currentPage + 1}" 
                               class="page-btn">»</a>
                        </c:if>

                    </div>
                </c:if>

            </c:when>
            <c:otherwise>
                <div style="text-align:center; padding: 40px; color: #999;">
                    <i class="bi bi-inbox" style="font-size: 40px;"></i>
                    <p class="mt-2">No categories found.</p>
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
<div id="createPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Add new Category</h3>
        </div>

        <c:if test="${not empty createError}">
            <div class="alert alert-danger" id="createErrorMsg">
                ${createError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/dashboard/category" method="post">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
                <label>Category Name</label>
                <input type="text" name="categoryName" class="form-control" value="${createName}">
            </div>

            <div class="form-group">
                <label>Description</label>
                <input type="text" name="descriptionText" class="form-control" value="${createDesc}">
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="1" ${createStatus==1 ? "selected" : "" }>Active</option>
                    <option value="0" ${createStatus==0 ? "selected" : "" }>Inactive</option>
                </select>
            </div>

            <div class="form-group">
                <label>Parent</label>
                <select name="parent" class="form-control">
                    <option value="">-- Select Parent Category --</option>

                    <option value="1" ${createParent == 1 ? "selected" : ""}>Book</option>
                    <option value="2" ${createParent == 2 ? "selected" : ""}>Stationery</option>
                </select>
            </div>


            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeCreatePopup()">Cancel</button>
                <button type="submit" class="btn-save">Create</button>
            </div>
        </form>
    </div>
</div>

<!-- ================= DETAIL POPUP ================= -->
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

<div id="editPopup" class="modal-overlay">
    <div class="modal-content ">
        <div class="modal-header">
            <h3>Edit Category</h3>
        </div>

        <c:if test="${not empty editError}">
            <div class="alert alert-danger p-2 mb-3 alert-error" id="editErrorMsg" style="font-size: 13px;">
                ${editError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/dashboard/category" method="post">
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

            <div class="form-group">
                <label>Parent</label>
                <select name="parent" id="editParent" class="form-control">
                    <option value="1">BOOK</option>
                    <option value="2">Văn phòng phẩm</option>
                </select>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeEditPopup()">Cancel</button>
                <button type="submit" class="btn-save">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<script>
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

        fetch('${pageContext.request.contextPath}/dashboard/category?action=detail&categoryId=' + id)
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
    function openEditPopup(id, name, description, status, parent) {
        document.getElementById("editCategoryId").value = id;
        document.getElementById("editCategoryName").value = name;
        document.getElementById("editDescription").value = description;
        document.getElementById("editStatus").value = status;
        document.getElementById("editParent").value = parent;

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
</script>

<c:if test="${openCreatePopup}">
    <script>
        window.onload = function () {
            setTimeout(function () {
                openCreatePopup(
                        '${createName}',
                        '${createDesc}',
                        '${createStatus}',
                        '${createParent}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("createErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>

<c:if test="${openEditPopup}">
    <script>
        window.onload = function () {
            // Đảm bảo DOM đã load xong
            setTimeout(function () {
                openEditPopup(
                        '${editId}',
                        '${editName}',
                        '${editDesc}',
                        '${editStatus}',
                        '${editParent}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("editErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>