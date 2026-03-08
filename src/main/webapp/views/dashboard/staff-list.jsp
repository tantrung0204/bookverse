<%-- Document : staff-list Created on : Feb 9, 2026, 4:23:54 PM Author : TrungNT - CE200064 --%>

    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

                <div class="container-fluid">
                    <!--    Header-->
                    <div class="page-header">
                        <p class="title">Manage Staffs</p>
                        <p class="subtitle">Manage and monitor all staff accounts</p>
                    </div>

                    <!--                    Mô tả về Staff-->
                    <div class="content-card">
                        <!--        Toolbar-->
                        <div class="toolbar">
                            <!--            Nút Add Staff-->
                            <a class="btn-add" href="#" data-bs-toggle="modal" data-bs-target="#addStaffModal">
                                <i class="bi bi-plus-lg me-1"></i> Add New Staff
                            </a>
                            <!--            Nút Search-->
                            <form method="get" action="staff" class="search-form">
                                <input type="hidden" name="action" value="search">
                                <div class="search-box">
                                    <i class="bi bi-search"></i>
                                    <input type="text" name="keyword" placeholder="Search by name..."
                                        value="${searchKeyword}">
                                </div>
                            </form>
                        </div>

                        <table class="custom-table">
                            <!--                Tiêu Đề bảng -->
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
                                                <!--                                Nút View Detail Staff-->
                                                <a href="staff?action=view&staffId=${s.staffId}"
                                                    class="btn-action btn-detail">
                                                    <i class="bi bi-eye"></i>
                                                </a>
                                                <!--                                Nút Edit Staff-->
                                                <button type="button" class="btn-action btn-edit"
                                                    onclick="openEditStaffPopup('${s.staffId}', '${s.username}', '${s.fullName}', '${s.roleName}')">
                                                    <i class="bi bi-pencil"></i>
                                                </button>

                                                <!--                                Nút Delete Staff-->
                                                <form action="staff" method="post" style="display:inline;"
                                                    onsubmit="return confirm('Xóa nhân viên: ${s.staffId}, ${s.fullName}?')">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="staffId" value="${s.staffId}">
                                                    <button type="submit" class="btn-action btn-delete">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>

                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <!--            Phân trang-->
                        <nav aria-label="Page navigation" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="staff?action=list&page=${currentPage - 1}">Previous</a>
                                </li>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="staff?action=list&page=${i}">${i}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="staff?action=list&page=${currentPage + 1}">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

                <!--                   Pop-up Add New Staff-->
                <div class="modal fade" id="addStaffModal" tabindex="-1" aria-labelledby="addStaffLabel"
                    aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <form action="${pageContext.request.contextPath}/staff" method="POST"
                                enctype="multipart/form-data">
                                <input type="hidden" name="action" value="create">

                                <div class="modal-header border-0 pb-0">
                                    <h5 class="modal-title fw-bold fs-4" id="addStaffLabel">Add Staff</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                                </div>
                                <div class="modal-body pt-0">
                                    <p class="text-muted mb-4"></p>
                                    <!--                    Username-->
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Username</label>
                                        <input type="text" class="form-control" name="username" required
                                            placeholder="Enter your username">
                                    </div>
                                    <!--                    Full Name-->
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Full Name</label>
                                        <input type="text" class="form-control" name="fullName" required
                                            placeholder="Enter your full name">
                                    </div>
                                    <!--                    Password-->
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Password</label>
                                        <input type="password" class="form-control" name="password" required
                                            placeholder="Enter your password">
                                    </div>
                                    <!--                    Role-->
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Role</label>
                                        <select class="form-control" name="roleName" required>
                                            <option value="admin">Admin</option>
                                            <option value="warehouse">Warehouse</option>
                                            <option value="staff">Seller</option>
                                        </select>
                                    </div>

                                    <!--                    <div class="mb-3">
                                            <label class="form-label fw-semibold">Upload Image</label>
                                            <div class="input-group w-100">
                                                <input type="text" class="form-control" id="fileNameDisplay" placeholder="PNG, JPG up to 10MB" readonly>
                                                <button class="btn btn-primary" type="button" onclick="document.getElementById('fileInput').click()">
                                                    <i class="fas fa-upload me-1"></i> Chosen Image
                                                </button>
                                            </div>
                                            <input type="file" id="fileInput" name="avatarFile" accept="image/png, image/jpeg" style="display: none" onchange="updateFileName(this)">
                                            <div id="fileSizeError" class="text-danger small mt-1" style="display:none">File is too large! Max 10MB.</div>
                                        </div>-->

                                    <div class="modal-footer border-0">
                                        <button type="button" class="btn btn-cancel px-4"
                                            data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-add px-4">Add</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!--               Pop-up View Detail Staff-->
                <div id="viewDetailPopup" class="modal-overlay" style="${openViewModal ? 'display:flex' : 'none'}">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3>Staff Detail</h3>
                            <button type="button" class="btn-close" onclick="closeViewPopup()"></button>
                        </div>

                        <div class="modal-body text-center" style="padding: 20px;">
                            <div class="mb-3">
                                <img src="${pageContext.request.contextPath}/uploads/${staffDetail.profileImageUrl}"
                                    class="rounded-circle shadow-sm"
                                    style="width: 120px; height: 120px; object-fit: cover; border: 3px solid #f8f9fa;"
                                    onerror="this.src=''">
                            </div>

                            <h4>${staffDetail.fullName}</h4>
                            <p>@${staffDetail.username}</p>

                            <div style="text-align: left; background: #fdfaf7; padding: 15px; border-radius: 10px;">
                                <p class="mb-2"><strong>Role:</strong> ${staffDetail.roleName}</p>
                                <p class="mb-2"><strong>Status:</strong>
                                    <span
                                        class="badge-status ${staffDetail.status == 1 ? 'badge-active' : 'badge-inactive'}">
                                        ${staffDetail.status == 1 ? 'Active' : 'Blocked'}
                                    </span>
                                </p>

                                <p class="mb-2"><strong>Account Creation Date:</strong>
                                    <fmt:formatDate value="${staffDetail.createdAt}" pattern="dd-MM-yyyy HH:mm" />
                                </p>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button class="btn-cancel" onclick="closeViewPopup()">Close</button>
                        </div>
                    </div>
                </div>

                <!--                Pop-up edit Staff-->
                <div id="editStaffPopup" class="modal-overlay" style="display: none;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3>Edit Staff</h3>
                        </div>

                        <form action="staff" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="edit">

                            <input type="hidden" id="editStaffId" name="staffId">

                            <div class="form-group mb-3">
                                <label>Username</label>
                                <input type="text" id="editStaffUsername" name="username" class="form-control" readonly
                                    style="background-color: #dfdfdf; color: #777; cursor: not-allowed; border: 1px solid #ccc;">
                            </div>

                            <div class="form-group mb-3">
                                <label>Full Name</label>
                                <input type="text" id="editStaffFullName" name="fullName" class="form-control"
                                    placeholder="Enter full name" required>
                            </div>

                            <div class="form-group mb-3">
                                <label>Role</label>
                                <select id="editStaffRole" name="roleName" class="form-control" required>
                                    <option value="warehouse">Warehouse</option>
                                    <option value="admin">Admin</option>
                                    <option value="manager">Manager</option>
                                </select>
                            </div>

                            <div class="form-group mb-3">
                                <label>Password</label>
                                <input type="password" id="editStaffPassword" name="password" class="form-control"
                                    placeholder="Enter new password (optional)">
                            </div>

                            <div class="form-group mb-3">
                                <label class="form-label fw-semibold">Upload Image</label>
                                <div class="input-group w-100">
                                    <input type="text" class="form-control" id="fileNameDisplay"
                                        placeholder="PNG, JPG up to 10MB" readonly>
                                    <button class="btn btn-primary" type="button"
                                        onclick="document.getElementById('fileInput').click()">
                                        <i class="fas fa-upload me-1"></i> Choose Image
                                    </button>
                                </div>
                                <input type="file" id="fileInput" name="avatarFile" accept="image/png, image/jpeg"
                                    style="display: none" onchange="updateFileName(this)">
                                <div id="fileSizeError" class="text-danger small mt-1" style="display:none">File is too
                                    large! Max 10MB.</div>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn-cancel" onclick="closeEditStaffPopup()">Cancel</button>
                                <button type="submit" class="btn-save">Save Changes</button>
                            </div>
                        </form>
                    </div>
                </div>



                <script>

                    function closeViewPopup() {
                        var modalElement = document.getElementById('viewDetailPopup');
                        if (modalElement) {
                            modalElement.style.display = 'none';
                        }
                    }


                    <c:if test="${openViewModal}">
                        window.onload = function () {
        var modal = document.getElementById('viewDetailPopup');
                        if (modal) {
                            modal.style.display = 'flex';
        }
    };
                    </c:if>

                    function updateFileName(input) {
                        const fileName = input.files[0] ? input.files[0].name : "No file chosen";
                        const fileSize = input.files[0] ? input.files[0].size / 1024 / 1024 : 0;
                        const displayInput = document.getElementById('fileNameDisplay');
                        const errorDiv = document.getElementById('fileSizeError');

                        displayInput.value = fileName;

                        if (fileSize > 10) {
                            errorDiv.style.display = 'block';
                            input.value = "";
                            displayInput.value = "PNG, JPG up to 10MB";
                        } else {
                            errorDiv.style.display = 'none';
                        }
                    }
                </script>


                <script>

                    function closeViewPopup() {
                        var modalElement = document.getElementById('viewDetailPopup');
                        if (modalElement) {
                            modalElement.style.display = 'none';
                        }
                    }

                    <c:if test="${openViewModal}">
                        window.onload = function () {
        var modal = document.getElementById('viewDetailPopup');
                        if (modal) {
                            modal.style.display = 'flex';
        }
    };
                    </c:if>


                    // Đóng Popup khi click ra ngoài vùng trắng
                    window.onclick = function (event) {
                        var modal = document.getElementById("editStaffPopup");
                        if (event.target == modal) {
                            closeEditStaffPopup();
                        }
                    }

                    // Hàm mở Popup (Bỏ phone, thêm role)
                    function openEditStaffPopup(id, user, name, email, role) {
                        const idField = document.getElementById("editStaffId");
                        const userField = document.getElementById("editStaffUsername");
                        const nameField = document.getElementById("editStaffFullName");
                        const emailField = document.getElementById("editStaffEmail");
                        const roleField = document.getElementById("editStaffRole");
                        const passwordField = document.getElementById("editStaffPassword");
                        const popup = document.getElementById("editStaffPopup");

                        // Kiểm tra xem có tìm thấy đủ Element không
                        if (idField && userField && nameField && popup) {
                            idField.value = id;
                            userField.value = user;
                            nameField.value = name;

                            if (emailField) emailField.value = email;
                            if (roleField) roleField.value = role;
                            if (passwordField) passwordField.value = "";

                            popup.style.display = "flex";
                        } else {
                            console.error("Lỗi: Kiểm tra lại ID trong HTML, có cái chưa khớp!");
                        }
                    }

                    // Hàm đóng Popup
                    function closeEditStaffPopup() {
                        document.getElementById("editStaffPopup").style.display = "none";
                    }
                </script>