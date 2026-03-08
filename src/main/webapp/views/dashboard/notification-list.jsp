<%-- 
    Document   : notification-list
    Created on : Feb 19, 2026, 10:44:27 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/styles/notification-list.css">

<div class="container-fluid">

    <!-- HEADER -->
    <div class="page-header">
        <p class="title">Notification Management</p>
        <p class="subtitle">Create and manage system notifications</p>
    </div>

    <div class="content-card">

        <!-- TOOLBAR -->
        <div class="toolbar">

            <button class="btn-add" onclick="openPopup()">
                <i class="bi bi-plus-lg me-1"></i> Add new Notification
            </button>

            <form class="search-form"
                  action="${pageContext.request.contextPath}/notification"
                  method="get">

                <input type="hidden" name="action" value="search"/>

                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text"
                           name="keyword"
                           value="${param.keyword}"
                           placeholder="Search notifications...">
                </div>
            </form>

        </div>

        <!-- SUCCESS MESSAGE -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert-success">
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>


        <!-- TABLE -->
        <c:choose>

            <c:when test="${not empty notifications}">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="20%">Title</th>
                            <th width="30%">Content</th>
                            <th width="15%">Image</th>
                            <th width="25%">Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="n" items="${notifications}">
                            <tr>
                                <td><strong>${n.notificationId}</strong></td>
                                <td>${n.title}</td>
                                <td>${n.contentText}</td>
                                <td>
                                    <c:if test="${not empty n.imageUrl}">
                                        <img src="${pageContext.request.contextPath}/${n.imageUrl}"
                                             alt="Notification Image"
                                             style="width:100px; height:auto; border-radius:6px;">
                                    </c:if>
                                </td>

                                <td>
                                    <div class="action-buttons">

                                        <!-- VIEW -->
                                        <button class="btn-action btn-detail"
                                                onclick="openDetailModal(
                                                                '${n.notificationId}',
                                                                '${n.title}',
                                                                '${n.contentText}',
                                                                '${n.imageUrl}',
                                                                '${n.createdAt}'
                                                                )">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <!-- EDIT -->
                                        <button class="btn-action btn-edit"
                                                onclick="openEditModal(
                                                                '${n.notificationId}',
                                                                '${n.title}',
                                                                '${n.contentText}',
                                                                '${n.imageUrl}'
                                                                )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <!-- DELETE -->
                                        <form action="${pageContext.request.contextPath}/notification"
                                              method="post"
                                              style="display:inline;">
                                            <input type="hidden" name="action" value="delete"/>
                                            <input type="hidden" name="id" value="${n.notificationId}"/>

                                            <button type="submit"
                                                    class="btn-action btn-delete"
                                                    onclick="return confirm('Delete this notification?')">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>

                                    </div>
                                </td>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>

            <c:otherwise>
                <div style="text-align:center; padding: 40px; color: #999;">
                    <i class="bi bi-inbox" style="font-size: 40px;"></i>
                    <p class="mt-2">No notifications found.</p>
                </div>
            </c:otherwise>

        </c:choose>

        <!-- CREATE NOTIFICATION POPUP -->
        <div id="createModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Add new Notification</h3>
                </div>

                <c:if test="${not empty createError}">
                    <div class="alert alert-danger p-2 mb-3" style="font-size:13px;">
                        ${createError}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/notification"
                      method="post"
                      enctype="multipart/form-data">

                    <input type="hidden" name="action" value="create"/>

                    <!-- TITLE -->
                    <div class="form-group">
                        <label>Title</label>
                        <input type="text"
                               name="title"
                               class="form-control"
                               required>
                    </div>

                    <!-- CONTENT -->
                    <div class="form-group">
                        <label>Content</label>
                        <textarea name="content"
                                  class="form-control"
                                  rows="4"
                                  required></textarea>
                    </div>

                    <!-- IMAGE -->
                    <div class="form-group">
                        <label>Image</label>
                        <input type="file"
                               name="image"
                               class="form-control"
                               accept="image/*">
                    </div>

                    <!-- FOOTER -->
                    <div class="modal-footer">
                        <button type="button"
                                class="btn-cancel"
                                onclick="closePopup()">
                            Cancel
                        </button>

                        <button type="submit"
                                class="btn-save">
                            Create
                        </button>
                    </div>

                </form>
            </div>
        </div>

        <!-- EDIT NOTIFICATION POPUP -->
        <div id="editModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Edit Notification</h3>
                </div>

                <c:if test="${not empty editError}">
                    <div class="alert alert-danger p-2 mb-3" style="font-size:13px;">
                        ${editError}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/notification"
                      method="post"
                      enctype="multipart/form-data">

                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" id="editId" name="id"/>

                    <!-- TITLE -->
                    <div class="form-group">
                        <label>Title</label>
                        <input type="text"
                               id="editTitle"
                               name="title"
                               class="form-control"
                               required>
                    </div>

                    <!-- CONTENT -->
                    <div class="form-group">
                        <label>Content</label>
                        <textarea id="editContent"
                                  name="content"
                                  class="form-control"
                                  rows="4"
                                  required></textarea>
                    </div>

                    <!-- IMAGE -->
                    <div class="form-group">
                        <label>Current Image</label>
                        <img id="currentImagePreview"
                             src=""
                             style="width:100px; display:none; margin-bottom:10px;">
                    </div>

                    <!-- FOOTER -->
                    <div class="modal-footer">
                        <button type="button"
                                class="btn-cancel"
                                onclick="closeEditModal()">
                            Cancel
                        </button>

                        <button type="submit"
                                class="btn-save">
                            Save Changes
                        </button>
                    </div>

                </form>
            </div>
        </div>

        <!-- DETAIL NOTIFICATION MODAL -->
        <div id="detailModal" class="modal-overlay">
            <div class="modal-content">

                <div class="modal-header">
                    <h3>Notification Detail</h3>
                </div>

                <table class="detail-table">
                    <tr><th>ID:</th><td id="detailId"></td></tr>
                    <tr><th>Title:</th><td id="detailTitle"></td></tr>
                    <tr><th>Content:</th><td id="detailContent"></td></tr>
                    <tr>
                        <th>Image:</th>
                        <td>
                            <img id="detailImage"
                                 style="max-width:200px; max-height:200px; object-fit:cover; border-radius:6px; display:none;">
                            <span id="noDetailImage" style="color:gray; display:none;">No image</span>
                        </td>
                    </tr>
                    <tr><th>Created At:</th><td id="detailCreated"></td></tr>
                </table>

                <div class="modal-footer">
                    <button class="btn-cancel"
                            onclick="closeDetailModal()">Close</button>
                </div>

            </div>
        </div>

        <!-- ================= SCRIPT ================= -->

        <script>
            function openPopup() {
                document.getElementById("createModal").style.display = "flex";
            }

            function closePopup() {
                document.getElementById("createModal").style.display = "none";
            }

            function openEditModal(id, title, content, image) {

                document.getElementById("editId").value = id;
                document.getElementById("editTitle").value = title;
                document.getElementById("editContent").value = content;

                if (image && image !== "") {
                    document.getElementById("currentImagePreview").src =
                            "${pageContext.request.contextPath}/" + image;
                    document.getElementById("currentImagePreview").style.display = "block";
                } else {
                    document.getElementById("currentImagePreview").style.display = "none";
                }

                document.getElementById("editModal").style.display = "flex";
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

                    document.getElementById("editId").value = "${editNotification.notificationId}";
                    document.getElementById("editTitle").value = "${editNotification.title}";
                    document.getElementById("editContent").value = "${editNotification.contentText}";
                    document.getElementById("editImage").value = "${editNotification.imageUrl}";

                    document.getElementById("editModal").style.display = "flex";
                });
            </script>
        </c:if>

        <script>
            function openDetailModal(id, title, content, image, created) {

                document.getElementById("detailId").innerText = id;
                document.getElementById("detailTitle").innerText = title;
                document.getElementById("detailContent").innerText = content;
                document.getElementById("detailCreated").innerText = created;

                const imgTag = document.getElementById("detailImage");
                const noImgText = document.getElementById("noDetailImage");

                if (image && image !== "") {
                    imgTag.src = "${pageContext.request.contextPath}/" + image;
                    imgTag.style.display = "block";
                    noImgText.style.display = "none";
                } else {
                    imgTag.style.display = "none";
                    noImgText.style.display = "block";
                }

                document.getElementById("detailModal").style.display = "flex";
            }

            function closeDetailModal() {
                document.getElementById("detailModal").style.display = "none";
            }
        </script>

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

