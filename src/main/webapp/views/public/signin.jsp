<%-- 
    Document   : signin
    Created on : Mar 9, 2026, 12:28:18 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign in - Bookverse</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/signin.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css"  type="text/css"/>
    </head>
    <body>
        <div class="header-logo">
            <a href="${pageContext.request.contextPath}/home">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Bookverse Logo">
            </a>
        </div>
        <div class="login-container">
            <h2>Sign In</h2>

            <form action="${pageContext.request.contextPath}/signin" method="POST">

                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="alert alert-danger py-2 text-center" style="font-size: 0.9rem;" role="alert">
                        <i class="fa-solid fa-circle-exclamation me-1"></i> ${requestScope.errorMessage}
                    </div>
                </c:if>

                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" value="${username}" required />
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" minlength="8" required />
                </div>

                <label>Sign in as:</label>
                <div class="role-selection">
                    <label class="role-item" for="roleCustomer">
                        <input type="radio" id="roleCustomer" name="role" value="customer" 
                               ${empty selectedRole or selectedRole == 'customer' ? 'checked' : ''} />
                        Customer
                    </label>

                    <label class="role-item" for="roleStaff">
                        <input type="radio" id="roleStaff" name="role" value="staff" 
                               ${selectedRole == 'staff' ? 'checked' : ''} />
                        Staff
                    </label>
                </div>

                <div class="button-group">
                    <button type="submit">Sign In</button>
                    <a href="signup.jsp" class="signup-btn">Sign Up</a>
                </div>
            </form>
        </div>
        <jsp:include page="footer-index.jsp" />

        <div id="toast"></div>
        <c:if test="${not empty sessionScope.errorMessage}">
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    showToastJS('${sessionScope.errorMessage}', 'error');
                });
            </script>
            <c:remove var="errorMessage" scope="session"/>
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

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
    </body>
</html>
