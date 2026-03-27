<%-- 
    Document   : signup
    Created on : Mar 20, 2026, 7:35:05 PM
    Author     : huyqu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign Up - Bookverse</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/signup.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" type="text/css"/>
    </head>
    <div class="header-logo">
        <a href="${pageContext.request.contextPath}/home">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Bookverse Logo">
        </a>
    </div>

    <div class="login-container" style="max-width: 500px;"> <h2>Sign Up</h2>

        <form action="${pageContext.request.contextPath}/signup" method="POST">

            <c:if test="${not empty requestScope.errorMsg}">
                <div class="alert alert-danger py-2 text-center" style="font-size: 0.9rem;" role="alert">
                    <i class="fa-solid fa-circle-exclamation me-1"></i> ${requestScope.errorMsg}
                </div>
            </c:if>

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" value="${username}" pattern="[a-zA-Z0-9][a-zA-Z0-9._\-]{4,}" required />
            </div>

            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="${fullName}" pattern="[a-zA-Zà-ỹ][a-zA-Zà-ỹ\s]*" required />
            </div>

            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" value="${email}" pattern="[a-zA-Z0-9][a-zA-Z0-9._\-]*@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}" required />
            </div>

            <div class="form-group">
                <label for="phone">Phone Number</label>
                <input type="text" id="phone" name="phone" value="${phone}" pattern="[0-9]{10,11}" required />
            </div>

            <div class="form-group">
                <label>Gender</label>
                <div style="display: flex; gap: 20px; padding: 10px 0;">
                    <label style="display: flex; align-items: center; gap: 5px; font-weight: normal; margin: 0; cursor: pointer;">
                        <input type="radio" name="gender" value="1" ${gender == '1' || empty gender ? 'checked' : ''} style="margin: 0; width: auto; scale: 1.2;"> Male
                    </label>
                    <label style="display: flex; align-items: center; gap: 5px; font-weight: normal; margin: 0; cursor: pointer;">
                        <input type="radio" name="gender" value="0" ${gender == '0' ? 'checked' : ''} style="margin: 0; width: auto; scale: 1.2;"> Female
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" minlength="8" pattern="^[a-zA-Z0-9!@#$%^&*]{8,20}$" required />
            </div>

            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" pattern="^[a-zA-Z0-9!@#$%^&*]{8,20}$" required />
            </div>

            <div class="button-group" style="margin-top: 30px;">
                <button type="submit" style="width: 100%;">Create Account</button>
            </div>

            <div style="text-align: center; margin-top: 15px; font-size: 14px;">
                Already have an account? 
                <a href="${pageContext.request.contextPath}/signin" style="color: #8B6B4C; font-weight: 600; text-decoration: none;">Sign In</a>
            </div>
        </form>

    </div> <jsp:include page="footer-index.jsp" />



    <div id="toast"></div>
    <c:if test="${not empty sessionScope.errorMsg}">
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                showToastJS('${sessionScope.errorMsg}', 'error');
            });
        </script>
        <c:remove var="errorMsg" scope="session"/>
    </c:if>

    <script>
        function showToastJS(message, type) {
            var toast = document.getElementById("toast");
            if (!toast) {
                toast = document.createElement("div");
                toast.id = "toast";
                document.body.appendChild(toast);
            }
            toast.innerHTML = (type === 'error' ? '<i class="fa-solid fa-circle-exclamation"></i> ' : '<i class="fa-solid fa-circle-check"></i> ') + "<span>" + message + "</span>";
            toast.className = type === 'error' ? 'toast-error show' : 'toast-success show';
            setTimeout(function () {
                toast.className = toast.className.replace(" show", "");
            }, 3000);
        }
    </script>

    <script src="${pageContext.request.contextPath}/bootstrap/bootstrap.bundle.min.js" type="text/javascript"></script>
</body>
</html>
