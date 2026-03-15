<%-- 
    Document   : customer-changePassword
    Created on : Mar 15, 2026, 7:52:47 PM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-md-9">
    <div class="row justify-content-center">
        <div class="col-md-7">

            <div class="edit-profile-card">

                <h3 class="mb-2">Edit Profile</h3>
                <p class="text-muted mb-4">
                    Update your personal information and preferences
                </p>

                <div class="mb-4 d-flex flex-column align-items-center">

                    <div class="avatar">
                        <img src="${user.profileImageUrl}" alt="Avatar">
                    </div>

                </div>



                <form 
                    action="${pageContext.request.contextPath}/profile?action=changePassword"
                    method="post"
                    >
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" >
                            ${error}
                        </div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success">
                            ${success}
                        </div>
                    </c:if>
                    <div class="mb-3">
                        <label class="form-label">Old Password</label>
                        <input type="text" class="form-control"
                               placeholder="Enter your old password"
                               name="oldPassword">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">New Password</label>
                        <input type="text" class="form-control"
                               placeholder="Enter your new password"
                               name="newPassword">
                    </div>         

                    <div class="mb-3">
                        <label class="form-label">Re-New Password</label>
                        <input type="text" class="form-control"
                               placeholder="Enter your re-new password"
                               name="reNewPassword">
                    </div> 
                    <div class="d-flex gap-3">
                        <button class="btn btn-success flex-fill" type="submit">
                            ✓ Save Changes
                        </button>

                        <button class="btn btn-outline-secondary flex-fill">
                            ✕ Cancel
                        </button>
                    </div>

                </form>

            </div>

        </div>
    </div>
</div>

<script>
    function uploadAvatar(input) {

        const file = input.files[0];
        const errorDiv = document.getElementById("fileSizeError");

        if (!file)//nếu bấm cancel thì thoát hành động đổi avt
            return;

        const fileSize = file.size / 1024 / 1024;

        if (fileSize > 10) {// nếu size file > 10Mb
            errorDiv.style.display = "block";//mở thông báo lỗi
            input.value = "";// xóa file đã chọn
            return;
        }

        errorDiv.style.display = "none";

        // tự submit form
        document.getElementById("avatarForm").submit();
    }
</script>