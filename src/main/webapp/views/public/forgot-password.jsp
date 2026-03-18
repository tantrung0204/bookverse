<%-- 
    Document   : forgot-password
    Created on : Mar 18, 2026
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forgot Password - Bookverse</title>

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
            <h2>Forgot Password</h2>
            <p class="subtitle">Enter your email address and we'll send you an OTP to reset your password.</p>

            <!-- Step Indicator -->
            <div class="step-indicator">
                <div class="step-dot active"></div>
                <div class="step-dot"></div>
                <div class="step-dot"></div>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="fp-alert fp-alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i> ${requestScope.errorMessage}
                </div>
            </c:if>

            <!-- Session Error (redirected from failed attempts) -->
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="fp-alert fp-alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.errorMessage}
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <form action="${pageContext.request.contextPath}/forgot-password" method="POST" id="forgotPasswordForm">
                <div class="form-group">
                    <label for="email"><i class="fa-solid fa-envelope"></i> Email Address</label>
                    <input type="email" id="email" name="email" value="${email}" 
                           placeholder="Enter your registered email" required />
                </div>

                <button type="submit" class="fp-btn-primary" id="submitBtn">
                    <i class="fa-solid fa-paper-plane"></i> Send OTP
                </button>
            </form>

            <a href="${pageContext.request.contextPath}/signin" class="fp-link">
                <i class="fa-solid fa-arrow-left"></i> Back to Sign In
            </a>
        </div>

        <jsp:include page="footer-index.jsp" />

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
        <script>
            // Show loading state on form submit
            document.getElementById('forgotPasswordForm').addEventListener('submit', function () {
                var btn = document.getElementById('submitBtn');
                btn.disabled = true;
                btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Sending...';
            });
        </script>
    </body>
</html>
