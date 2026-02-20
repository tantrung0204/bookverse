<%-- 
    Document   : notification-list
    Created on : Feb 19, 2026, 10:44:27 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Notification Management</h2>

<button onclick="openPopup()">+ Create Notification</button>

<form action="${pageContext.request.contextPath}/notification" method="get">
    <input type="hidden" name="action" value="search"/>
    <input name="keyword">
    <button type="submit">Search</button>
</form>

<c:if test="${not empty sessionScope.successMessage}">
    <div style="padding:10px;margin:10px 0;
         background:#d4edda;color:#155724;
         border:1px solid #c3e6cb;border-radius:5px;">
        ${sessionScope.successMessage}
    </div>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div style="padding:10px;margin:10px 0;
         background:#f8d7da;color:#721c24;
         border:1px solid #f5c6cb;border-radius:5px;">
        ${sessionScope.errorMessage}
    </div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>

<c:if test="${not empty notifications}">
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Content</th>
            <th>Image URL</th>
            <th>Created At</th>
            <th>Action</th>
        </tr>

        <c:forEach var="n" items="${notifications}">
            <tr>
                <td>${n.notificationId}</td>
                <td>${n.title}</td>
                <td>${n.contentText}</td>
                <td>${n.imageUrl}</td>
                <td>${n.createdAt}</td>
                <td>

                    <!-- VIEW -->
                    <button type="button"
                            onclick="location.href = '${pageContext.request.contextPath}/notification?action=detail&id=${n.notificationId}'">
                        View Detail
                    </button>

                    <!-- EDIT -->
                    <button type="button"
                            onclick="openEditModal(
                                            '${n.notificationId}',
                                            '${n.title}',
                                            '${n.contentText}',
                                            '${n.imageUrl}'
                                            )">
                        Edit
                    </button>

                    <!-- DELETE -->
                    <form action="${pageContext.request.contextPath}/notification"
                          method="post"
                          style="display:inline;">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${n.notificationId}"/>
                        <button type="submit"
                                onclick="return confirm('Delete this notification?')">
                            Delete
                        </button>
                    </form>

                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<!-- ================= CREATE MODAL ================= -->

<div id="createModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closePopup()">&times;</span>

        <h3>Create Notification</h3>

        <c:if test="${not empty createError}">
            <p style="color:red">${createError}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/notification" method="post">
            <input type="hidden" name="action" value="create"/>

            Title:
            <input type="text" name="title" required/><br/><br/>

            Content:
            <textarea name="content" required></textarea><br/><br/>

            Image URL:
            <input type="text" name="image"/><br/><br/>

            <button type="submit">Create</button>
        </form>
    </div>
</div>

<!-- ================= EDIT MODAL ================= -->

<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeEditModal()">&times;</span>

        <h3>Edit Notification</h3>

        <c:if test="${not empty editError}">
            <p style="color:red">${editError}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/notification"
              method="post">

            <input type="hidden" name="action" value="edit"/>
            <input type="hidden" id="editId" name="id"/>
            <input type="hidden" id="page" name="page" value="listPage"/>

            
            Title:
            <input type="text" id="editTitle" name="title" required/><br/><br/>

            Content:
            <textarea id="editContent" name="content" required></textarea><br/><br/>

            Image URL:
            <input type="text" id="editImage" name="image"/><br/><br/>

            <button type="submit">Update</button>
        </form>
    </div>
</div>

<!-- ================= SCRIPT ================= -->

<script>
    function openPopup() {
        document.getElementById("createModal").style.display = "block";
    }

    function closePopup() {
        document.getElementById("createModal").style.display = "none";
    }

    function openEditModal(id, title, content, image) {

        document.getElementById("editId").value = id;
        document.getElementById("editTitle").value = title;
        document.getElementById("editContent").value = content;
        document.getElementById("editImage").value = image;

        document.getElementById("editModal").style.display = "block";
    }

    function closeEditModal() {
        document.getElementById("editModal").style.display = "none";
    }
</script>

<c:if test="${openCreate}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            openPopup();
        });
    </script>
</c:if>

<c:if test="${openEdit}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            document.getElementById("editModal").style.display = "block";
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
        width: 400px;
        margin: 10% auto;
        padding: 20px;
        border-radius: 8px;
    }

    .close {
        float: right;
        font-size: 22px;
        cursor: pointer;
    }
</style>
