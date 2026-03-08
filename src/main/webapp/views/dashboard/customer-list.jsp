<%-- 
    Document   : customer-list
    Created on : Feb 9, 2026, 4:23:54 PM
    Author     : TrungNT - CE200064
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">
    <!--    Header-->
    <div class="page-header">
        <p class="title">Manage Customers</p> 
        <p class="subtitle">View and manage all user accounts in the system</p> 
    </div>


    <!--    Mô tả những Customer-->
    <div class="content-card">
        <!--        Nút Add New Customer-->
        <div class="toolbar">
            <a class="btn-add" href="#" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
                <i class="bi bi-plus-lg me-1"></i> Add New Customer
            </a>
            
            <!--            Thanh tìm kiếm -->
            <form method="get" action="customer" class="search-form">
                <input type="hidden" name="action" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search by name or email..." value="${searchKeyword}">
                </div>
            </form>
        </div>

        <!--                Đặc tả về những thuộc tính trong Customer-->
        <table class="custom-table">
            <!--                Tiêu Đề bảng -->
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
                            <span class="badge-status ${c.status == 1 ? 'badge-active' : 'badge-inactive'}">
                                ${c.status == 1 ? 'Active' : 'Blocked'}
                            </span>
                        </td>
                        <td>
                            <div class="action-buttons">
<!--                                Nút View Detail Customer-->
                                <a href="customer?action=view&customerId=${c.customerId}" class="btn-action btn-detail">
                                    <i class="bi bi-eye"></i>
                                </a>
<!--                                Nút Edit Customer-->
                                <button type="button" class="btn-action btn-edit" 
                                        onclick="openEditCustomerPopup('${c.customerId}', '${c.username}', '${c.fullName}', '${c.phoneNumber}', '${c.email}')">
                                    <i class="bi bi-pencil"></i>
                                </button>
<!--                                    Nút Delete Customer-->
                                <form action="customer" method="post" style="display:inline;" 
                                      onsubmit="return confirm('Xóa khách hàng: ${c.customerId}, ${c.fullName}?')">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="customerId" value="${c.customerId}"> 
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
                        <a class="page-link" href="customer?action=list&page=${currentPage - 1}">Previous</a>
                    </li>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="customer?action=list&page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="customer?action=list&page=${currentPage + 1}">Next</a>
                    </li>
                </ul>
            </nav>    
    </div>
</div>


<!-- Pop-up Add Customer-->
<div class="modal fade" id="addCustomerModal" tabindex="-1" aria-labelledby="addCustomerLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/customer" method="POST" 
                  enctype="multipart/form-data">
                <input type="hidden" name="action" value="create">

                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold fs-4" id="addCustomerLabel">Add New Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body pt-0">
                    <p class="text-muted mb-4"></p>
                    <!--Username-->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Username</label>
                        <input type="text" class="form-control" name="username" required placeholder="Enter your username">
                    </div>
                    <!--Full Name-->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Full Name</label>
                        <input type="text" class="form-control" name="fullName" required placeholder="Enter your full name">
                    </div>

                    <!--Email-->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Email</label>
                        <input type="email" class="form-control" name="email" required placeholder="Enter your email">
                    </div>
                    <!--Mật khẩu-->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Password</label>
                        <input type="password" class="form-control" name="password" required placeholder="Enter your password">
                    </div>

                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-cancel px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-add px-4">Add</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!--Pop-up View Detail Customer-->

<div id="viewDetailPopup" class="modal-overlay" style="${openViewModal ? 'display:flex' : 'none'}">
    <div class="modal-content">

        <div class="modal-header">
            <h3>Customer Detail</h3>
            <button type="button" class="btn-close" onclick="closeViewPopup()"></button>
        </div>

        <div class="modal-body text-center" style="padding: 20px;">

            <div class="mb-3">
                <img src="${pageContext.request.contextPath}/uploads/${customerDetail.profileImageUrl}" 
                     class="rounded-circle shadow-sm" 
                     style="width: 120px; height: 120px; object-fit: cover; border: 3px solid #f8f9fa;"
                     onerror="this.src=''">
            </div>

            <h4>${customerDetail.fullName}</h4>
            <p>@${customerDetail.username}</p>

            <div style="text-align: left; background: #fdfaf7; padding: 15px; border-radius: 10px;">

                <p class="mb-2"><strong>Email:</strong> ${customerDetail.email}</p>
                <p class="mb-2"><strong>Status:</strong> 
                    <span class="badge-status ${customerDetail.status == 1 ? 'badge-active' : 'badge-inactive'}">
                        ${customerDetail.status == 1 ? 'Active' : 'Blocked'}
                    </span>
                <p class="mb-2"><strong>Phone Number:</strong> ${customerDetail.phoneNumber}</p>
                <p class="mb-2"><strong>Account Creation Date:</strong>
                    <fmt:formatDate value="${customerDetail.createdAt}" pattern="dd-MM-yyyy HH:mm" />
                </p>

                <div class="d-flex justify-content-between align-items-center">
                    <span class="subtitle" style="font-size: 14px;">Total Orders Purchased:</span>
                    <span style="font-weight: 700; color: #8B6B4C; font-size: 18px;">${totalOrders}</span>
                </div>

            </div>

        </div>
    </div>
</div>

<!--                Pop-up edit Customer-->
<div id="editCustomerPopup" class="modal-overlay" style="display: none;">

    <div class="modal-content">
        <div class="modal-header">
            <h3>Edit Customer</h3>
        </div>

        <form action="customer" method="post">

            <input type="hidden" name="action" value="edit">

            <input type="hidden" id="editCustomerId" name="customerId">

            <div class="form-group mb-3">
                <label>Username</label>
                <input type="text" id="editCustomerUsername" name="username" class="form-control" 
                       readonly style="background-color: #dfdfdf; color: #777; cursor: not-allowed; border: 1px solid #ccc;">
            </div>

            <div class="form-group mb-3">
                <label>Full Name</label>
                <input type="text" id="editCustomerFullName" name="fullName" class="form-control" placeholder="Enter your Full Name">
            </div>

            <div class="form-group mb-3">
                <label>Email</label>
                <input type="email" id="editCustomerEmail" name="email" class="form-control" placeholder="Enter your email">
            </div>

            <div class="form-group mb-3">
                <label>Phone Number</label>
                <input type="text" id="editCustomerPhone" name="phone" class="form-control" placeholder="Enter your phone number" pattern="[0-9]{10,11}">
            </div>

            <div class="form-group mb-3">
                <label>Password</label>
                <input type="password" id="editCustomerPassword" name="password" class="form-control" placeholder="Enter new password">
            </div>

            <div class="mb-3">
                <label class="form-label fw-semibold">Upload Image</label>
                <div class="input-group w-100">
                    <input type="text" class="form-control" id="fileNameDisplay" placeholder="PNG, JPG up to 10MB" readonly>
                    <button class="btn btn-primary" type="button" onclick="document.getElementById('fileInput').click()">
                        <i class="fas fa-upload me-1"></i> Choose Image
                    </button>
                </div>
                <input type="file" id="fileInput" name="avatarFile" accept="image/png, image/jpeg" style="display: none" onchange="updateFileName(this)">
                <div id="fileSizeError" class="text-danger small mt-1" style="display:none">File is too large! Max 10MB.</div>
            </div>



            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeEditCustomerPopup()">Cancel</button>
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

    // Đóng Popup khi click ra ngoài vùng trắng
    window.onclick = function (event) {
        var modal = document.getElementById("editPopup");
        if (event.target == modal) {
            closeEditPopup();
        }
    }

    function openEditCustomerPopup(id, user, name, phone, email) {

        const idField = document.getElementById("editCustomerId");
        const userField = document.getElementById("editCustomerUsername");
        const nameField = document.getElementById("editCustomerFullName");
        const phoneField = document.getElementById("editCustomerPhone");
        const emailField = document.getElementById("editCustomerEmail");
        const passwordField = document.getElementById("editCustomerPassword");
        const popup = document.getElementById("editCustomerPopup");



        // 2. Kiểm tra xem có tìm thấy ID và Popup không

        if (idField && userField && nameField && popup) {

            idField.value = id;
            userField.value = user;
            nameField.value = name;
            if (phoneField)
                phoneField.value = phone;
            if (emailField)
                emailField.value = email;
            passwordField.value = "";
            popup.style.display = "flex";
        } else {

            console.error("Lỗi: Kiểm tra lại ID trong HTML, có cái chưa khớp!");
        }
    }

    function closeEditCustomerPopup() {
        // Ẩn Modal đi
        document.getElementById("editCustomerPopup").style.display = "none";
    }
</script>

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

