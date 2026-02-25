<%-- 
    Document   : staff-list
    Created on : Feb 9, 2026, 4:23:54 PM
    Author     : TrungNT - CE200064
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Staffs</p> 
        <p class="subtitle">Manage and monitor all staff accounts</p> 
    </div>

    <div id="viewDetailPopup" class="modal-overlay" style="${openViewModal ? 'display:flex' : 'none'}">
        <div class="modal-content" style="max-width: 500px;">
            <div class="modal-header">
                <h3 class="title" style="font-size: 20px;">Staff Detail</h3>
                <button type="button" class="btn-close" onclick="closeViewPopup()"></button>
            </div>

            <div class="modal-body text-center" style="padding: 20px;">
                <div class="mb-3">
                    <img src="${pageContext.request.contextPath}/uploads/${staffDetail.profileImageUrl}" 
                         class="rounded-circle shadow-sm" 
                         style="width: 120px; height: 120px; object-fit: cover; border: 3px solid #f8f9fa;"
                         onerror="this.src='https://via.placeholder.com/120'">
                </div>

                <h4 class="title mb-1">${staffDetail.fullName}</h4>
                <p class="subtitle mb-4" style="color: #888;">@${staffDetail.username}</p>

                <div style="text-align: left; background: #fdfaf7; padding: 15px; border-radius: 10px;">
                    <p class="mb-2"><strong>Role:</strong> ${staffDetail.roleName}</p>
                    <p class="mb-2"><strong>Status:</strong> 
                        <span class="badge-status ${staffDetail.status == 1 ? 'badge-active' : 'badge-inactive'}">
                            ${staffDetail.status == 1 ? 'Active' : 'Blocked'}
                        </span>
                    </p>

                </div>
            </div>

            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeViewPopup()">Close</button>
            </div>
        </div>
    </div>

    <div class="content-card">
        <div class="toolbar">
            <a class="btn-add" href="#" data-bs-toggle="modal" data-bs-target="#addStaffModal">
                <i class="bi bi-plus-lg me-1"></i> Add New User
            </a>

            <form method="get" action="staff" class="search-form">
                <input type="hidden" name="action" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search by name..." value="${searchKeyword}">
                </div>
            </form>
        </div>

        <table class="custom-table">
            <thead>
                <tr>
                    <th width="10%">ID</th>
                    <th width="25%">Full Name</th>
                    <th width="25%">Role</th> 
                    <th width="15%">Status</th>
                    <th width="25%">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${staffs}">
                    <tr>
                        <td><strong>${s.staffId}</strong></td>
                        <td>${s.fullName}</td>
                        <td>${s.roleName}</td> 
                        <td>
                            <span class="badge-status ${s.status == 1 ? 'badge-active' : 'badge-inactive'}">
                                ${s.status == 1 ? 'Active' : 'Blocked'}
                            </span>
                        </td>
                        <td>
                            <div class="action-buttons">
                                <a href="staff?action=view&staffId=${s.staffId}" class="btn-action btn-detail">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <button class="btn-action btn-edit"><i class="bi bi-pencil"></i></button>
                                <button class="btn-action btn-delete"><i class="bi bi-trash"></i></button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade" id="addStaffModal" tabindex="-1" aria-labelledby="addStaffLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/staff" method="POST">
                <input type="hidden" name="action" value="create">

                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold fs-4" id="addStaffLabel">Add Staff</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body pt-0">
                    <p class="text-muted mb-4">Update Staff information and details</p>

                    <div class="row">
                        <div class="col-md-7">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Username</label>
                                <input type="text" class="form-control" name="username" required placeholder="Enter your username">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Full Name</label>
                                <input type="text" class="form-control" name="fullName" required placeholder="Enter your full name">
                            </div>
                            <!--                            <div class="mb-3">
                                                            <label class="form-label fw-semibold">Email</label>
                                                            <input type="email" class="form-control" name="email" required placeholder="Enter your email">
                                                        </div>-->
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Password</label>
                                <input type="password" class="form-control" name="password" required placeholder="Enter your password">
                            </div>
                        </div>

                        <div class="col-md-5 d-flex flex-column align-items-center justify-content-center">
                            <label class="form-label fw-semibold align-self-start">Avatar</label>
                            <div class="border border-dashed rounded p-4 text-center w-100" style="cursor: pointer;">
                                <i class="bi bi-image fs-1 text-muted"></i>
                                <p class="mb-0 mt-2">Upload new image</p>
                                <small class="text-muted">PNG, JPG up to 10MB</small>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary px-4">Save Changes</button>
                </div>
            </form>
        </div>
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
</script>


