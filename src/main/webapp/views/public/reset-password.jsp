<%-- 
    Document   : reset-password
    Created on : Mar 18, 2026
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password - Bookverse</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/forgot-password.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" type="text/css"/>
    </head>
    <body>
        <div class="header-logo">
            <a href="${pageContext.request.contextPath}/home">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Bookverse Logo">
            </a>
        </div>

        <div class="fp-container">
            <h2>Reset Password</h2>
            <p class="subtitle">Create a new password for your account.</p>

            <!-- Step Indicator -->
            <div class="step-indicator">
                <div class="step-dot done"></div>
                <div class="step-dot done"></div>
                <div class="step-dot active"></div>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="fp-alert fp-alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i> ${requestScope.errorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="POST" id="resetForm">
                <div class="form-group">
                    <label for="newPassword"><i class="fa-solid fa-lock"></i> New Password</label>
                    <input type="password" id="newPassword" name="newPassword" minlength="8" 
                           placeholder="Enter new password" required />
                    <p class="password-hint"><i class="fa-solid fa-info-circle"></i> Must be at least 8 characters</p>
                </div>

                <div class="form-group">
                    <label for="confirmPassword"><i class="fa-solid fa-lock"></i> Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" 
                           placeholder="Re-enter new password" required />
                </div>

                <div id="matchError" class="fp-alert fp-alert-error" style="display:none;">
                    <i class="fa-solid fa-circle-exclamation"></i> Passwords do not match
                </div>

                <button type="submit" class="fp-btn-primary" id="resetBtn">
                    <i class="fa-solid fa-key"></i> Reset Password
                </button>
            </form>

            <a href="${pageContext.request.contextPath}/forgot-password" class="fp-link">
                <i class="fa-solid fa-arrow-left"></i> Start Over
            </a>
        </div>

        <jsp:include page="footer-index.jsp" />

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
        <script>
            // Client-side password match validation
            var newPwd = document.getElementById('newPassword');
            var confirmPwd = document.getElementById('confirmPassword');
            var matchError = document.getElementById('matchError');
            var resetBtn = document.getElementById('resetBtn');

            function validateMatch() {
                if (confirmPwd.value.length > 0 && newPwd.value !== confirmPwd.value) {
                    matchError.style.display = 'flex';
                    resetBtn.disabled = true;
                } else {
                    matchError.style.display = 'none';
                    resetBtn.disabled = false;
                }
            }

            newPwd.addEventListener('input', validateMatch);
            confirmPwd.addEventListener('input', validateMatch);

            // Show loading on submit
            document.getElementById('resetForm').addEventListener('submit', function (e) {
                if (newPwd.value !== confirmPwd.value) {
                    e.preventDefault();
                    matchError.style.display = 'flex';
                    return;
                }
                resetBtn.disabled = true;
                resetBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Resetting...';
            });
        </script>
    </body>
</html>
