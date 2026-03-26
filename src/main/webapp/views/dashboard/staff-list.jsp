<%-- Document : staff-list Created on : Feb 9, 2026, 4:23:54 PM Author : TrungNT - CE200064 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<div class="container-fluid">
    <!-- ============================================ Header ================================================= -->
    <div class="page-header">
        <p class="title">Manage Staffs</p>
        <p class="subtitle">Manage and monitor all staff accounts</p>
    </div>
    <!--  ============================================ Staff ================================================= -->
    <div class="content-card">
        <!-- ======================================== Add New Staff Button ======================================= -->
        <div class="toolbar">
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Staff
            </button>
            <!-- ======================================= Search Button ========================================== -->
            <form action="${pageContext.request.contextPath}/dashboard/staff" method="GET"
                  class="search-form">
                <input type="hidden" name="action" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" required maxlength="50"
                           placeholder="Search by name..." pattern=".*\S.*" value="${keyword}">
                </div>
            </form>
        </div>
        <!-- ====================================== Notification ================================================= -->
        <c:if test="${not empty message}">
            <div class="alert alert-error">
                ${message}
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">
                ${success}
            </div>
            <c:remove var="success" scope="session" />
        </c:if>
        <c:if test="${not empty deleteError}">
            <div class="alert alert-error">${deleteError}</div>
            <c:remove var="deleteError" scope="session" />
        </c:if>
        <!-- ========================================= Staff List ================================================ -->
        <c:choose>
            <c:when test="${not empty staffs}">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="25%">Full Name</th>
                            <th width="20%">Role</th>
                            <th width="20%">Status</th>
                            <th width="20%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${staffs}">
                            <c:if test="${s.staffId != sessionScope.user.staffId}">
                                <tr>
                                    <td><strong>${s.staffId}</strong></td>
                                    <td>${s.fullName}</td>
                                    <td>${s.roleName}</td>
                                    <td>
                                        <span
                                            class="badge-status ${s.status == 1 ? 'badge-active' : 'badge-inactive'}">
                                            ${s.status == 1 ? 'Active' : 'Blocked'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <!-- =============================================== View Detail Staff button ============================================== -->
                                            <button type="button" class="btn-action btn-detail"
                                                    title="Detail" onclick="openViewDetailStaffPopup(
                                                                '${s.staffId}', '${s.fullName}', '${s.username}',
                                                                '${s.roleName}', '${s.status}',
                                                                '<fmt:formatDate value="${s.createdAt}" pattern="dd-MM-yyyy HH:mm" />',
                                                                '${s.profileImageUrl}')">
                                                <i class="bi bi-eye"></i>
                                            </button>
                                            <!-- =============================================== Edit Staff button ============================================== -->
                                            <button type="button" class="btn-action btn-edit"
                                                    title="Edit"
                                                    onclick="openEditStaffPopup('${s.staffId}', '${s.username}', '${s.fullName}', '${s.roleName}')">
                                                <i class="bi bi-pencil"></i>
                                            </button>
                                            <!-- =============================================== Delete Staff button ============================================== -->
                                            <form
                                                action="${pageContext.request.contextPath}/dashboard/staff"
                                                method="post" style="display:inline;"
                                                onsubmit="return confirm('Delete Staff: ${s.fullName} (ID: ${s.staffId})?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="staffId"
                                                       value="${s.staffId}">
                                                <button type="submit" class="btn-action btn-delete"
                                                        title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
        </c:choose>
        <c:if test="${totalPages > 1}">
            <div class="pagination">

                <!-- Previous -->
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/staff?page=${currentPage - 1}"
                       class="page-btn">«</a>
                </c:if>

                <!-- Page 1 -->
                <a href="${pageContext.request.contextPath}/dashboard/staff?page=1"
                   class="page-btn ${currentPage == 1 ? 'active' : ''}">
                    1
                </a>

                <!-- ... trước -->
                <c:if test="${currentPage > 3}">
                    <span class="page-btn">...</span>
                </c:if>

                <!-- Trang trước current -->
                <c:if test="${currentPage - 1 > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/staff?page=${currentPage - 1}"
                       class="page-btn">
                        ${currentPage - 1}
                    </a>
                </c:if>

                <!-- Current -->
                <c:if test="${currentPage != 1 && currentPage != totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/staff?page=${currentPage}"
                       class="page-btn active">
                        ${currentPage}
                    </a>
                </c:if>

                <!-- Trang sau current -->
                <c:if test="${currentPage + 1 < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/staff?page=${currentPage + 1}"
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
                    <a href="${pageContext.request.contextPath}/dashboard/staff?page=${totalPages}"
                       class="page-btn ${currentPage == totalPages ? 'active' : ''}">
                        ${totalPages}
                    </a>
                </c:if>

                <!-- Next -->
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/staff?page=${currentPage + 1}"
                       class="page-btn">»</a>
                </c:if>

            </div>
        </c:if>
    </div>
</div>
<script>
    // ----------------- CREATE POP-UP -----------------
    function openCreatePopup() {
        document.getElementById('createPopup').style.display = "flex";
        const err = document.getElementById("createErrorMsg");
        if (err)
            err.style.display = 'none';
    }
    function closeCreatePopup() {
        document.getElementById('createPopup').style.display = "none";
    }
    // ----------------- VIEW DETAIL POP-UP -----------------
    function openViewDetailStaffPopup(id, fullName, username, role, status, createdAt, avatarUrl) {
        document.getElementById('detailStaffId').innerText = id;
        document.getElementById('detailStaffFullNameTitle').innerText = fullName;
        document.getElementById('detailStaffUsernameTitle').innerText = username;
        document.getElementById('detailStaffCreated').innerText = createdAt;
        document.getElementById('detailStaffStatus').innerHTML = status == 1 ?
                '<span class="badge-status badge-active">Active</span>' :
                '<span class="badge-status badge-inactive">Blocked</span>';
        let avatarImg = document.getElementById('detailStaffAvatar');
        if (avatarUrl && avatarUrl.trim() !== '') {
            avatarImg.src = avatarUrl;
        } else {
            avatarImg.src = '${pageContext.request.contextPath}/assets/images/default-avt.jpg';
        }
        document.getElementById('viewDetailStaffPopup').style.display = "flex";
    }
    function closeViewDetailStaffPopup() {
        document.getElementById('viewDetailStaffPopup').style.display = "none";
    }
    // ----------------- EDIT POP-UP -----------------
    function openEditStaffPopup(id, username, fullName, roleName) {
        document.getElementById('editStaffId').value = id;
        document.getElementById('editStaffUsername').value = username || '';
        document.getElementById('editStaffFullName').value = fullName || '';
        if (roleName) {
            document.getElementById('editStaffRole').value = roleName;
        }
        const err = document.getElementById("editErrorMsg");
        if (err)
            err.style.display = 'none';
        document.getElementById('editStaffPopup').style.display = "flex";
    }
    function closeEditStaffPopup() {
        document.getElementById('editStaffPopup').style.display = "none";
    }
    // ----------------- CLICK OUTSIDE TO CLOSE -----------------
    window.onclick = function (event) {
        var modalCreate = document.getElementById("createPopup");
        var modalEdit = document.getElementById("editStaffPopup");
        var modalDetail = document.getElementById("viewDetailStaffPopup");
        if (event.target == modalCreate) {
            closeCreatePopup();
        } else if (event.target == modalEdit) {
            closeEditStaffPopup();
        } else if (event.target == modalDetail) {
            closeViewDetailStaffPopup();
        }
    }
</script>
<c:if test="${openCreatePopup}">
    <script>
        window.onload = function () {
            // Đảm bảo DOM đã load xong
            setTimeout(function () {
                openCreatePopup();
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
                openEditStaffPopup(
                        '${editStaffId}',
                        '${editStaffUsername}',
                        '${editStaffFullName}'
                        );
                const err = document.getElementById("editErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>
<!-- =================== Pop-up Add Staff =========================-->
<div id="createPopup" class="modal-overlay" style="display: none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Add New Staff</h3>
        </div>
        <c:if test="${not empty createError}">
            <div class="alert alert-danger" id="createErrorMsg">
                ${createError}
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/dashboard/staff" method="POST">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" class="form-control" value="${createUsername}"
                       pattern="[a-zA-Z0-9][a-zA-Z0-9._\-]{4,}" required
                       placeholder="Enter your username">
            </div>
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" class="form-control" value="${createFullName}"
                       pattern="[a-zA-Zà-ỹ][a-zA-Zà-ỹ\s]*" required placeholder="Enter your full name">
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control"
                       pattern="^[a-zA-Z0-9!@#$%^&*]{8,20}$" required placeholder="Enter new password">
            </div>
            <div class="form-group">
                <label>Role</label>
                <select class="form-control" name="roleName" required>
                    <option value="admin">Admin</option>
                    <option value="warehouse">Warehouse</option>
                    <option value="staff">Seller</option>
                </select>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel"
                        onclick="closeCreatePopup()">Cancel</button>
                <button type="submit" class="btn-save">Create</button>
            </div>
        </form>
    </div>
</div>
<!-- ============================== Pop-up View Detail Staff ====================================== -->
<div id="viewDetailStaffPopup" class="modal-overlay" style="display: none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Staff Detail</h3>
        </div>
        <div style="text-align: center; margin-bottom: 20px; margin-top: 10px;">
            <img id="detailStaffAvatar"
                 src="${pageContext.request.contextPath}/assets/images/default-avt.jpg" alt="Avatar"
                 class="rounded-circle shadow-sm"
                 style="width: 120px; height: 120px; object-fit: cover; border: 3px solid #f8f9fa;"
                 onerror="this.src='${pageContext.request.contextPath}/assets/images/default-avt.jpg';">
            <h4 id="detailStaffFullNameTitle" style="margin-top: 15px; margin-bottom: 5px;"></h4>
            <p id="detailStaffUsernameTitle" style="color: #777; margin-bottom: 0;"></p>
        </div>
        <table class="detail-table" style="width: 100%;">
            <tr>
                <th style="width: 40%;">ID:</th>
                <td id="detailStaffId"></td>
            </tr>
            <tr>
                <th>Status:</th>
                <td id="detailStaffStatus"></td>
            </tr>
            <tr>
                <th>Account Created:</th>
                <td id="detailStaffCreated"></td>
            </tr>
        </table>
        <div class="modal-footer" style="margin-top: 20px;">
            <button type="button" class="btn-cancel"
                    onclick="closeViewDetailStaffPopup()">Close</button>
        </div>
    </div>
</div>
<!--  ================== Pop-up edit Staff ==================== -->
<div id="editStaffPopup" class="modal-overlay" style="display: none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Edit Staff</h3>
        </div>
        <c:if test="${not empty editError}">
            <div class="alert alert-danger" id="editErrorMsg">
                ${editError}
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/dashboard/staff" method="POST"
              enctype="multipart/form-data">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" id="editStaffId" name="staffId">
            <div class="form-group">
                <label>Username</label>
                <input type="text" id="editStaffUsername" name="username" class="form-control"
                       readonly
                       style="background-color: #dfdfdf; color: #777; cursor: not-allowed; border: 1px solid #ccc;">
            </div>
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" id="editStaffFullName" name="fullName" class="form-control"
                       pattern="[a-zA-Zà-ỹ][a-zA-Zà-ỹ\s]*" placeholder="Enter your Full Name">
            </div>
            <div class="form-group">
                <label>Role</label>
                <select id="editStaffRole" name="roleName" class="form-control" required>
                    <option value="warehouse">Warehouse</option>
                    <option value="admin">Admin</option>
                    <option value="seller">Seller</option>
                </select>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" id="editStaffPassword" name="password" class="form-control"
                       pattern="^[a-zA-Z0-9!@#$%^&*]{8,20}$" placeholder="Enter new password">
            </div>
            <div class="form-group">
                <label>Upload Image</label>
                <div class="input-group w-100">
                    <input type="text" class="form-control" id="fileNameDisplayAdd"
                           placeholder="PNG, JPG up to 10MB" readonly>
                    <button class="btn btn-primary" type="button"
                            onclick="document.getElementById('fileInputAdd').click()">
                        <i class="fas fa-upload me-1"></i> Chosen Image
                    </button>
                </div>
                <input type="file" id="fileInputAdd" name="avatarFile"
                       accept="image/png, image/jpeg" style="display: none"
                       onchange="updateFileNameAdd(this)">
                <div id="fileSizeErrorAdd" class="text-danger small mt-1" style="display:none">File
                    is too large! Max 10MB.</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel"
                        onclick="closeEditStaffPopup()">Cancel</button>
                <button type="submit" class="btn-save">Save Changes</button>
            </div>
        </form>
    </div>
</div>