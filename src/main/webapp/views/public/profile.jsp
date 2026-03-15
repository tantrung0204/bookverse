<%-- 
    Document   : profile
    Created on : Mar 14, 2026, 3:05:05 PM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css"  type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
    </head>
    <body class="d-flex flex-column min-vh-100">
        <c:if test="${not empty openCustomerProfile}">
            <jsp:include page="header-index.jsp" />
        </c:if>
        <c:if test="${not empty openStaffProfile}">
            <jsp:include page="../dashboard/header.jsp" />
        </c:if>
        <main class="flex-grow-1">
            <div class="container mt-5">
                <div class="row g-4">

                    <!-- Sidebar -->
                    <div class="col-md-3">

                        <div class="account-sidebar">

                            <h4>Account Settings</h4>
                            <c:if test="${not empty openCustomerProfile}">
                                <a href="${pageContext.request.contextPath}/profile"
                                   class="sidebar-link ${activeMenu eq 'view'? 'active':''}">View Profile</a>

                                <a href="${pageContext.request.contextPath}/profile?view=edit"
                                   class="sidebar-link ${activeMenu eq 'edit'? 'active':''}">Edit Profile</a>

                                <a href="${pageContext.request.contextPath}/profile?view=changePassword"
                                   class="sidebar-link ${activeMenu eq 'changePassword'? 'active':''}">Change Password</a>

                                <a href="#">Orders History</a>
                                <a href="#">Review</a>
                            </c:if>
                            <c:if test="${not empty openStaffProfile}">
                                <a href="${pageContext.request.contextPath}/profile"
                                   class="sidebar-link ${activeMenu eq 'staffView'? 'active':''}">Profile</a>
                            </c:if>
                        </div>

                    </div>


                    <!-- Main Content -->
                    <c:if test="${not empty openCustomerEditProfile}">
                        <jsp:include page="../customer/customer-editProfile.jsp"/>
                    </c:if>
                    <c:if test="${not empty openCustomerChangePasswordProfile}">
                        <jsp:include page="../customer/customer-changePassword.jsp"/>
                    </c:if>
                    <c:if test="${not empty openCustomerViewProfile}">
                        <jsp:include page="../customer/customer-viewProfile.jsp"/>
                    </c:if>
                    <c:if test="${not empty openStaffProfile}">
                        <jsp:include page="../dashboard/staff-viewProfile.jsp"/>
                    </c:if>
                </div>
            </div>
        </main>
        <c:if test="${not empty openCustomerProfile}">
            <jsp:include page="footer-index.jsp" />
        </c:if>
    </body>
</html>
