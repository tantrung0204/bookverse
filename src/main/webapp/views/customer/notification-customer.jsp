<%-- 
    Document   : notification-customer
    Created on : 10 Mar 2026, 18:41:18
    Author     : NganTTK-CE190411
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet"
      type="text/css" />
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/order-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/notification-customer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cart.css">

<body>
    <div class="order-container">

        <h2 class="page-title">My Notifications</h2>

        <!-- FILTER -->
        <div class="order-tabs">
            <button class="tab active" onclick="filterNotification('all', this)">All</button>
            <button class="tab" onclick="filterNotification('unread', this)">Unread</button>
            <button class="tab" onclick="filterNotification('read', this)">Read</button>
        </div>

        <!-- EMPTY -->
        <c:if test="${empty notifications}">
            <div class="empty-notification">
                No notifications available
            </div>
        </c:if>

        <!-- LIST -->
        <c:forEach var="n" items="${notifications}">
            <div class="order-card notification-card-item ${!n.isRead ? 'unread' : ''}"
                 data-status="${n.isRead ? 'read' : 'unread'}">

                <!-- HEADER -->
                <div class="order-header">

                    <div class="order-left">
                        <span class="order-id">
                            NOTI-${n.customerNotificationId}
                        </span>

                        <span class="status ${n.isRead ? 'completed' : 'pending'}">
                            ${n.isRead ? 'Read' : 'Unread'}
                        </span>
                    </div>

                    <div class="order-actions">
                        <a class="btn-view"
                           href="${pageContext.request.contextPath}/notification/customer?action=detail&id=${n.customerNotificationId}">
                            View Details
                        </a>
                    </div>

                </div>

                <!-- BODY -->
                <div class="order-body">

                    <div class="order-block">
                        <span>Title</span>
                        <p>${n.notificationId.title}</p>
                    </div>

                    <div class="order-block">
                        <span>Created At</span>
                        <p>${n.notificationId.createdAt}</p>
                    </div>

                    <div class="order-block" style="grid-column: span 2;">
                        <span>Content</span>
                        <p>${n.notificationId.contentText}</p>
                    </div>

                </div>

            </div>
        </c:forEach>

    </div>
</body>
<script>
    function filterNotification(status, el) {
        let items = document.querySelectorAll(".notification-card-item");
        let tabs = document.querySelectorAll(".tab");

        // remove active tab
        tabs.forEach(t => t.classList.remove("active"));

        // set active tab
        el.classList.add("active");

        // filter logic
        items.forEach(i => {
            let s = i.dataset.status;

            if (status === "all" || s === status) {
                i.style.display = "block";
            } else {
                i.style.display = "none";
            }
        });
    }
</script>