<%-- 
    Document   : category-list
    Created on : 10 Feb 2026, 18:33:04
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Categories</p>
        <p class="subtitle">Create and manage book categories for your library</p>
    </div>

    <div class="content-card" >

        <div class="toolbar">
            <button type="button"
                    class="btn-add"
                    onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Category
            </button>

            <form method="get" action="category" class="search-form">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search categories..." value="${keyword}">
                </div>
            </form>
        </div>

        <div id="messagePopup" class="popup-overlay">
            <div class="popup-box">
                <h4 id="popupTitle"></h4>
                <p id="popupMessage"></p>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty categories}">
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
                                        <button type="button"
                                                class="btn-action btn-detail"
                                                title="View Detail"
                                                onclick="openDetailPopup(
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
                                                                '${c.status}'
                                                                )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/category" method="post" style="display:inline;"
                                              onsubmit="return confirmDelete('${c.categoryId}', '${c.categoryName}')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${c.categoryId}">
                                            <button type="submit" class="btn-action btn-delete" title="Delete">
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
                    <p class="mt-2">No categories found.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty deleteError}">
            <div class="alert alert-danger mt-3 ">${deleteError}</div>
            <c:remove var="deleteError" scope="session"/>
        </c:if>
    </div>
</div>

<!-- ================= CREATE POPUP ================= -->
<div id="createPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Create Category</h3>
        </div>

        <c:if test="${not empty createError}">
            <div class="alert alert-danger" id="createErrorMsg" >
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
                <input type="text" name="descriptionText" class="form-control" value="${createDes}" >
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="1" ${createStatus == 1 ? "selected" : ""} >Active</option>
                    <option value="0" ${createStatus == 0 ? "selected" : ""} >Inactive</option>
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

        <div class="form-group">
            <label>ID</label>
            <input type="text" id="detailId" class="form-control" readonly>
        </div>

        <div class="form-group">
            <label>Name</label>
            <input type="text" id="detailName" class="form-control" readonly>
        </div>

        <div class="form-group">
            <label>Description</label>
            <input type="text" id="detailDesc" class="form-control" readonly>
        </div>

        <div class="form-group">
            <label>Status</label>
            <input type="text" id="detailStatus" class="form-control" readonly>
        </div>

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
        document.getElementById("detailId").value = id;
        document.getElementById("detailName").value = name;
        document.getElementById("detailDesc").value = desc;
        document.getElementById("detailStatus").value = status == 1 ? "Active" : "Inactive";
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

    function showMessage(type, title, msg) {
        const popup = document.getElementById("messagePopup");
        const box = popup.querySelector(".popup-box");

        popupTitle.innerText = title;
        popupMessage.innerText = msg;

        //DÙ LÀ error HAY success → ĐỀU DÙNG SUCCESS STYLE
        box.classList.remove("popup-success", "popup-error");
        box.classList.add("popup-success");

        popup.style.display = "flex";

        // TỰ ĐỘNG TẮT SAU 4 GIÂY
        clearTimeout(popupTimer);
        popupTimer = setTimeout(() => {
            popup.style.display = "none";
        }, 3000);

        // 🔴 CLICK RA NGOÀI → TẮT
        popup.onclick = function (event) {
            if (event.target === popup) {
                popup.style.display = "none";
            }
        };
    }

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
                        '${createStatus}'
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
                        '${editStatus}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("editErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>
<c:if test="${not empty successMsg}">
    <script>
        showMessage("success", "Success", "${successMsg}");
    </script>
    <c:remove var="successMsg" scope="session"/>
</c:if>
    <c:if test="${not empty errorMsg}">
    <script>
        showMessage("error", "Error", "${errorMsg}");
    </script>
    <c:remove var="errorMsg" scope="session"/>
</c:if>
