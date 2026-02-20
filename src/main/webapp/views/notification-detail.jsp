<%-- 
    Document   : notification-detail
    Created on : Feb 19, 2026, 10:44:41 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Notification Detail</title>
    </head>

    <body style="background:#eee; font-family: Arial; padding:20px;">

        <h2>Notification Detail</h2>

        <!-- SUCCESS MESSAGE -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div style="padding:10px;margin:10px 0;
                 background:#d4edda;color:#155724;
                 border:1px solid #c3e6cb;border-radius:5px;">
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <!-- DETAIL TABLE -->

        <c:if test="${not empty notification}">
            <table border="1">
                <tr><th>ID</th><td>${notification.notificationId}</td></tr>
                <tr><th>Title</th><td>${notification.title}</td></tr>
                <tr><th>Content</th><td>${notification.contentText}</td></tr>
                <tr><th>Image URL</th><td>${notification.imageUrl}</td></tr>
                <tr><th>Create At</th><td>${notification.createdAt}</td></tr>
            </table>
        </c:if>

        <br>

        <a href="${pageContext.request.contextPath}/notification">
            ← Back to list
        </a>

        <button onclick="openEdit()">Edit</button>

        <!-- DELETE -->
        <form action="${pageContext.request.contextPath}/notification"
              method="post"
              style="display:inline;">
            <input type="hidden" name="action" value="delete"/>
            <input type="hidden" name="id"
                   value="${notification.notificationId}"/>
            <button type="submit"
                    onclick="return confirm('Delete this notification?')">
                Delete
            </button>
        </form>

        <!-- ================= EDIT MODAL ================= -->
        <div id="editModal" class="modal">
            <div class="modal-content">

                <span class="close" onclick="closeEdit()">&times;</span>

                <h3>Edit Notification</h3>

                <c:if test="${not empty editError}">
                    <p style="color:red">${editError}</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/notification"
                      method="post">

                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" name="id"
                           value="${notification.notificationId}"/>

                    Title:<br>
                    <input type="text"
                           name="title"
                           value="${notification.title}"
                           required/><br><br>

                    Content:<br>
                    <textarea name="content"
                              rows="4"
                              style="width:100%;"
                              required>${notification.contentText}</textarea><br><br>

                    Image URL:<br>
                    <input type="text"
                           name="image"
                           value="${notification.imageUrl}"/><br><br>

                    <button type="submit">Save</button>
                </form>

            </div>
        </div>

        <!-- ================= SCRIPT ================= -->
        <script>
            function openEdit() {
                document.getElementById("editModal").style.display = "block";
            }

            function closeEdit() {
                document.getElementById("editModal").style.display = "none";
            }
        </script>

        <!-- Auto open modal if error -->
        <c:if test="${openEdit}">
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    openEdit();
                });
            </script>
        </c:if>

        <!-- ================= STYLE ================= -->
        <style>
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
            }

            .modal-content {
                background: #fff;
                width: 450px;
                margin: 8% auto;
                padding: 20px;
                border-radius: 6px;
            }

            .close {
                float: right;
                font-size: 20px;
                cursor: pointer;
            }
        </style>

    </body>
</html>

