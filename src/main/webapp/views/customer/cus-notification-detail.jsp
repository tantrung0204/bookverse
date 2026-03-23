<%-- 
    Document   : cus-notification-detail.jsp
    Created on : 11 Mar 2026, 09:29:29
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet"
          type="text/css" />
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/notification-detail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css"  type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile.css">
</head>
<!-- HEADER (nếu có) -->
<jsp:include page="/views/public/header-index.jsp" />

<div class="detail-container">

    <!-- TITLE -->
    <h2 class="detail-title">${notification.title}</h2>

    <!-- TIME -->
    <p class="time">${notification.createdAt}</p>

    <!-- BODY 2 CỘT -->
    <div class="detail-body">

        <!-- LEFT -->
        <div class="left-column">
            <div class="notification-content">
                ${notification.contentText}
            </div>
        </div>

        <!-- RIGHT -->
        <c:if test="${not empty notification.imageUrl}">
            <div class="right-column">
                <img src="${pageContext.request.contextPath}/${notification.imageUrl}"
                     alt="Notification Image">
            </div>
        </c:if>

    </div>

    <!-- BACK -->
    <a class="back-btn"
       href="${pageContext.request.contextPath}/notification/customer">
        ← Back to notifications
    </a>

</div>

<!-- FOOTER -->
<jsp:include page="/views/public/footer-index.jsp" />

