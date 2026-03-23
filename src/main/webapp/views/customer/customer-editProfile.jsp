<%-- 
    Document   : customer-editProfile
    Created on : Mar 15, 2026, 2:07:31 PM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-md-9">
    <div class="row justify-content-center mb-4">
        <div class="col-md-7">

            <div class="edit-profile-card">

                <h3 class="mb-2">Edit Profile</h3>
                <p class="text-muted mb-4">
                    Update your personal information and preferences
                </p>

                <form id="avatarForm"
                      action="${pageContext.request.contextPath}/profile?action=changeAvatar"
                      method="post"
                      enctype="multipart/form-data">

                    <div class="mb-4 d-flex flex-column align-items-center">

                        <div class="avatar">
                            <img src="${user.profileImageUrl}" alt="Avatar" onerror="this.src='${pageContext.request.contextPath}/assets/images/default-avt.jpg';">
                        </div>

                        <button type="button"
                                class="btn btn-sm mt-2 changeAvatar"
                                onclick="document.getElementById('fileInput').click()"><!-- giả lập việc click vào input có id là fileInput -->
                            Change Avatar
                        </button>
                        <!-- thực hiện việc mở file và chọn những file là ảnh png, jpeg, sau đó gửi vào uploadavartar. -->
                        <input type="file"
                               id="fileInput"
                               name="avatarFile"
                               accept="image/png, image/jpeg"
                               style="display:none"
                               onchange="uploadAvatar(this)">

                        <div id="fileSizeError"
                             class="text-danger small mt-1"
                             style="display:none">
                            File is too large! Max 10MB.
                        </div>

                    </div>

                </form>

                <form 
                    action="${pageContext.request.contextPath}/profile?action=editInfo"
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
                        <label class="form-label">Full name</label>
                        <input type="text" class="form-control"
                               placeholder="Enter your full name"
                               value="${user.fullName}"
                               name="fullName">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Phone number</label>
                        <input type="text" class="form-control"
                               placeholder="Enter your phone number"
                               value="${user.phoneNumber}"
                               name="phoneNumber">
                    </div>         

                    <div class="mb-3">
                        <label class="form-label">Address</label>
                        <input type="text" class="form-control"
                               placeholder="Enter your Address"
                               value="${user.address}"
                               name="address">
                    </div> 
                    <div class="d-flex gap-3">
                        <button class="btn flex-fill saveChanges" type="submit">
                            Save Changes
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