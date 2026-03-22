<%-- Document : customer-list Created on : Feb 9, 2026, 4:23:54 PM Author : TrungNT - CE200064 --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
                    <div class="container-fluid">
                        <!-- ======================== Header ============================= -->
                        <div class="page-header">
                            <p class="title">Manage Customers</p>
                            <p class="subtitle">View and manage all user accounts in the system</p>
                        </div>
                        <!-- ==================== Customer ======================== -->
                        <div class="content-card">
                            <!-- ====================== Add New Customer button =========================== -->
                            <div class="toolbar">
                                <button type="button" class="btn-add" onclick="openCreatePopup()">
                                    <i class="bi bi-plus-lg me-1"></i> Add New Customer
                                </button>
                                <!-- =============================== Search button =================================== -->
                                <form action="${pageContext.request.contextPath}/dashboard/customer" method="GET"
                                    class="search-form">
                                    <input type="hidden" name="action" value="search">
                                    <div class="search-box">
                                        <i class="bi bi-search"></i>
                                        <input type="text" name="keyword" required maxlength="50"
                                            placeholder="Search by name..." pattern=".*\S.*" value="${keyword}">
                                    </div>
                                </form>
                            </div>
                            <!-- ======================================== Notification ================================================== -->
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
                            <!-- ==================================================== CUSTOMER LIST =========================================== -->
                            <c:choose>
                                <c:when test="${not empty customers}">
                                    <table class="custom-table">
                                        <thead>
                                            <tr>
                                                <th width="10%">ID</th>
                                                <th width="15%">Full Name</th>
                                                <th width="15%">Phone Number</th>
                                                <th width="20%">Email</th>
                                                <th width="20%">Status</th>
                                                <th width="20%">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="c" items="${customers}">
                                                <tr>
                                                    <td><strong>${c.customerId}</strong></td>
                                                    <td>${c.fullName}</td>
                                                    <td>${c.phoneNumber}</td>
                                                    <td>${c.email}</td>
                                                    <td>
                                                        <span
                                                            class="badge-status ${c.status == 1 ? 'badge-active' : 'badge-inactive'}">
                                                            ${c.status == 1 ? 'Active' : 'Blocked'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="action-buttons">
                                                            <!-- =============================================== View Detail Customer button ============================================== -->
                                                            <button type="button" class="btn-action btn-detail"
                                                                title="Detail" onclick="openViewDetailCustomerPopup(
                                                                '${c.customerId}', '${c.fullName}', '${c.username}', '${c.email}',
                                                                '${c.phoneNumber}', '${c.status}',
                                                                '<fmt:formatDate value="${c.createdAt}"
                                                                pattern="dd-MM-yyyy HH:mm" />',
                                                            '${c.address}',
                                                            '${c.profileImageUrl}')">
                                                            <i class="bi bi-eye"></i>
                                                            </button>
                                                            <!-- =============================================== Edit Customer button ============================================== -->
                                                            <button type="button" class="btn-action btn-edit"
                                                                title="Edit" onclick="openEditCustomerPopup('${c.customerId}',
                                                                '${c.username}',
                                                                '${c.fullName}',
                                                                '${c.email}',
                                                                '${c.phoneNumber}',
                                                                '${c.address}')">
                                                                <i class="bi bi-pencil"></i>
                                                            </button>
                                                            <!-- =============================================== Delete Customer button ============================================== -->
                                                            <form
                                                                action="${pageContext.request.contextPath}/dashboard/customer"
                                                                method="post" style="display:inline;"
                                                                onsubmit="return confirm('Delete Customer: ${c.fullName} (ID: ${c.customerId})?');">
                                                                <input type="hidden" name="action" value="delete">
                                                                <input type="hidden" name="customerId"
                                                                    value="${c.customerId}">
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
                            </c:choose>
                            <!-- ======================================================= Pagination ================================================ -->
                            <c:set var="startPage" value="${currentPage - 1}" />
                            <c:set var="endPage" value="${currentPage + 1}" />
                            <c:if test="${startPage < 2}">
                                <c:set var="startPage" value="2" />
                                <c:set var="endPage" value="4" />
                            </c:if>
                            <c:if test="${endPage > totalPages - 1}">
                                <c:set var="endPage" value="${totalPages - 1}" />
                                <c:set var="startPage" value="${totalPages - 3}" />
                            </c:if>
                            <c:if test="${startPage < 2}">
                                <c:set var="startPage" value="2" />
                            </c:if>
                            <nav class="d-flex justify-content-center">
                                <ul class="pagination">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link"
                                            href="${pageContext.request.contextPath}/dashboard/customer?page=${currentPage - 1}">&laquo;</a>
                                    </li>
                                    <c:if test="${totalPages <= 5}">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link"
                                                    href="${pageContext.request.contextPath}/dashboard/customer?page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${totalPages > 5}">
                                        <li class="page-item ${currentPage == 1 ? 'active' : ''}">
                                            <a class="page-link"
                                                href="${pageContext.request.contextPath}/dashboard/customer?page=1">1</a>
                                        </li>
                                        <c:if test="${startPage > 2}">
                                            <li class="page-item disabled">
                                                <span class="page-link">...</span>
                                            </li>
                                        </c:if>
                                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link"
                                                    href="${pageContext.request.contextPath}/dashboard/customer?page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <c:if test="${endPage < totalPages - 1}">
                                            <li class="page-item disabled">
                                                <span class="page-link">...</span>
                                            </li>
                                        </c:if>
                                        <li class="page-item ${currentPage == totalPages ? 'active' : ''}">
                                            <a class="page-link"
                                                href="${pageContext.request.contextPath}/dashboard/customer?page=${totalPages}">
                                                ${totalPages}
                                            </a>
                                        </li>
                                    </c:if>
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link"
                                            href="${pageContext.request.contextPath}/dashboard/customer?page=${currentPage + 1}">&raquo;</a>
                                    </li>
                                </ul>
                            </nav>
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
                        function openViewDetailCustomerPopup(id, fullName, username, email, phone, status, createdAt, address, avatarUrl) {
                            document.getElementById('detailCustomerId').innerText = id;
                            document.getElementById('detailCustomerFullNameTitle').innerText = fullName;
                            document.getElementById('detailCustomerUsernameTitle').innerText = username;
                            document.getElementById('detailCustomerEmail').innerText = email;
                            document.getElementById('detailCustomerPhone').innerText = phone || 'Not Update yet';
                            document.getElementById('detailCustomerCreated').innerText = createdAt;
                            document.getElementById('detailCustomerAddress').innerText = address || 'Not Update yet';
                            document.getElementById('detailCustomerStatus').innerHTML = status == 1 ?
                                '<span class="badge-status badge-active">Active</span>' :
                                '<span class="badge-status badge-inactive">Blocked</span>';
                            let avatarImg = document.getElementById('detailCustomerAvatar');
                            if (avatarUrl && avatarUrl.startsWith('http')) {
                                avatarImg.src = avatarUrl;
                            } else if (avatarUrl && avatarUrl.trim() !== '') {
                                avatarImg.src = '${pageContext.request.contextPath}/' + avatarUrl;
                            } else {
                                avatarImg.src = '${pageContext.request.contextPath}/assets/images/default-avt.jpg';
                            }
                            document.getElementById('viewDetailCustomerPopup').style.display = "flex";
                        }
                        function closeViewDetailCustomerPopup() {
                            document.getElementById('viewDetailCustomerPopup').style.display = "none";
                        }
                        // ----------------- EDIT POP-UP -----------------
                        function openEditCustomerPopup(id, username, fullName, email, phone, address) {
                            document.getElementById('editCustomerId').value = id;
                            document.getElementById('editCustomerUsername').value = username || '';
                            document.getElementById('editCustomerFullName').value = fullName || '';
                            document.getElementById('editCustomerEmail').value = email || '';
                            document.getElementById('editCustomerPhone').value = phone || '';
                            document.getElementById('editCustomerAddress').value = address || '';
                            const err = document.getElementById("editErrorMsg");
                            if (err)
                                err.style.display = 'none';
                            document.getElementById('editCustomerPopup').style.display = "flex";
                        }
                        function closeEditCustomerPopup() {
                            document.getElementById('editCustomerPopup').style.display = "none";
                        }
                        // ----------------- CLICK OUTSIDE TO CLOSE -----------------
                        window.onclick = function (event) {
                            var modalCreate = document.getElementById("createPopup");
                            var modalEdit = document.getElementById("editCustomerPopup");
                            var modalDetail = document.getElementById("viewDetailCustomerPopup");
                            if (event.target == modalCreate) {
                                closeCreatePopup();
                            } else if (event.target == modalEdit) {
                                closeEditCustomerPopup();
                            } else if (event.target == modalDetail) {
                                closeViewDetailCustomerPopup();
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
                                    openEditCustomerPopup(
                                        '${editCustomerId}',
                                        '${editCustomerUsername}',
                                        '${editCustomerFullName}',
                                        '${editCustomerEmail}',
                                        '${editCustomerPhone}',
                                        '${editCustomerAddress}'
                                    );
                                    const err = document.getElementById("editErrorMsg");
                                    if (err)
                                        err.style.display = 'block';
                                }, 100);
                            };
                        </script>
                    </c:if>
                    <!-- =================== Pop-up Add Customer =========================-->
                    <div id="createPopup" class="modal-overlay" style="display: none;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3>Add New Customer</h3>
                            </div>
                            <c:if test="${not empty createError}">
                                <div class="alert alert-danger" id="createErrorMsg">
                                    ${createError}
                                </div>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/dashboard/customer" method="POST">
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
                                    <label>Email</label>
                                    <input type="email" name="email" class="form-control" value="${createEmail}"
                                        pattern="[a-zA-Z0-9][a-zA-Z0-9._\-]*@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}" required
                                        placeholder="Enter your email">
                                </div>
                                <div class="form-group">
                                    <label>Password</label>
                                    <input type="password" name="password" class="form-control"
                                        pattern="^[a-zA-Z0-9!@#$%^&*]{8,20}$" required placeholder="Enter new password">
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn-cancel"
                                        onclick="closeCreatePopup()">Cancel</button>
                                    <button type="submit" class="btn-save">Create</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!--  ================== Pop-up edit Customer ==================== -->
                    <div id="editCustomerPopup" class="modal-overlay" style="display: none;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3>Edit Customer</h3>
                            </div>
                            <c:if test="${not empty editError}">
                                <div class="alert alert-danger" id="editErrorMsg">
                                    ${editError}
                                </div>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/dashboard/customer" method="POST"
                                enctype="multipart/form-data">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" id="editCustomerId" name="customerId">
                                <div class="form-group">
                                    <label>Username</label>
                                    <input type="text" id="editCustomerUsername" name="username" class="form-control"
                                        readonly
                                        style="background-color: #dfdfdf; color: #777; cursor: not-allowed; border: 1px solid #ccc;">
                                </div>
                                <div class="form-group">
                                    <label>Full Name</label>
                                    <input type="text" id="editCustomerFullName" name="fullName" class="form-control"
                                        pattern="[a-zA-Zà-ỹ][a-zA-Zà-ỹ\s]*" placeholder="Enter your Full Name">
                                </div>
                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" id="editCustomerEmail" name="email" class="form-control"
                                        pattern="[a-zA-Z0-9][a-zA-Z0-9._\-]*@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}"
                                        placeholder="Enter your email">
                                </div>
                                <div class="form-group">
                                    <label>Phone Number</label>
                                    <input type="text" id="editCustomerPhone" name="phone" class="form-control"
                                        placeholder="Enter your phone number" pattern="^0(3|5|7|8|9)[0-9]{8}$">
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input type="text" id="editCustomerAddress" name="address" class="form-control"
                                        placeholder="Enter your address">
                                </div>
                                <div class="form-group">
                                    <label>Password</label>
                                    <input type="password" id="editCustomerPassword" name="password"
                                        class="form-control" pattern="^[a-zA-Z0-9!@#$%^&*]{8,20}$"
                                        placeholder="Enter new password">
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
                                        onclick="closeEditCustomerPopup()">Cancel</button>
                                    <button type="submit" class="btn-save">Save Changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!-- ============================== Pop-up View Detail Customer ====================================== -->
                    <div id="viewDetailCustomerPopup" class="modal-overlay" style="display: none;">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3>Customer Detail</h3>
                            </div>
                            <div style="text-align: center; margin-bottom: 20px; margin-top: 10px;">
                                <img id="detailCustomerAvatar"
                                    src="${pageContext.request.contextPath}/assets/images/default-avt.jpg" alt="Avatar"
                                    class="rounded-circle shadow-sm"
                                    style="width: 120px; height: 120px; object-fit: cover; border: 3px solid #f8f9fa;">
                                <h4 id="detailCustomerFullNameTitle" style="margin-top: 15px; margin-bottom: 5px;"></h4>
                                <p id="detailCustomerUsernameTitle" style="color: #777; margin-bottom: 0;"></p>
                            </div>
                            <table class="detail-table" style="width: 100%;">
                                <tr>
                                    <th style="width: 40%;">ID:</th>
                                    <td id="detailCustomerId"></td>
                                </tr>
                                <tr>
                                    <th>Email:</th>
                                    <td id="detailCustomerEmail"></td>
                                </tr>
                                <tr>
                                    <th>Phone Number:</th>
                                    <td id="detailCustomerPhone"></td>
                                </tr>
                                <tr>
                                    <th>Address:</th>
                                    <td id="detailCustomerAddress"></td>
                                </tr>
                                <tr>
                                    <th>Status:</th>
                                    <td id="detailCustomerStatus"></td>
                                </tr>
                                <tr>
                                    <th>Account Created:</th>
                                    <td id="detailCustomerCreated"></td>
                                </tr>
                            </table>
                            <div class="modal-footer" style="margin-top: 20px;">
                                <button type="button" class="btn-cancel"
                                    onclick="closeViewDetailCustomerPopup()">Close</button>
                            </div>
                        </div>
                    </div>