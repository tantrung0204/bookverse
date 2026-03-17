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
                <i class="bi bi-plus-lg me-1"></i> Add New Notification
            </button>

            <form class="search-form"
                  action="${pageContext.request.contextPath}/dashboard/notification"
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
                            <th width="20%">Image</th>
                            <th width="20%">Actions</th>
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
                                             class="notification-img">
                                    </c:if>
                                </td>

                                <td>
                                    <div class="action-buttons">

                                        <!-- VIEW -->
                                        <button class="btn-action btn-detail" title="View Detail"
                                                onclick="openDetailModal(
                                                                '${n.notificationId}',
                                                                '${n.title}',
                                                                '${n.contentText}',
                                                                '${n.imageUrl}',
                                                                '${n.createdAt}',
                                                                '${n.totalSent}',
                                                                '${n.totalRead}'
                                                                )">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <!-- DELETE -->
                                        <form action="${pageContext.request.contextPath}/dashboard/notification"
                                              method="post"
                                              style="display:inline;">
                                            <input type="hidden" name="action" value="delete"/>
                                            <input type="hidden" name="id" value="${n.notificationId}"/>

                                            <button type="submit"
                                                    class="btn-action btn-delete" title="Delete"
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

        <c:set var="queryString" value="&keyword=${param.keyword}" />
        <c:set var="startPage" value="${currentPage - 1}" />
        <c:set var="endPage" value="${currentPage + 1}" />

        <c:if test="${startPage < 2}">
            <c:set var="startPage" value="2"/>
            <c:set var="endPage" value="4"/>
        </c:if>

        <c:if test="${endPage > totalPages - 1}">
            <c:set var="endPage" value="${totalPages - 1}"/>
            <c:set var="startPage" value="${totalPages - 3}"/>
        </c:if>

        <c:if test="${startPage < 2}">
            <c:set var="startPage" value="2"/>
        </c:if>


        <c:if test="${totalPages > 1}">
            <div class="pagination">

                <!-- Prev -->
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/notification?action=${param.action}&page=${currentPage - 1}${queryString}"
                       class="page-btn">«</a>
                </c:if>

                <!-- Nếu <=5 trang -->
                <c:if test="${totalPages <= 5}">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="${pageContext.request.contextPath}/dashboard/notification?action=${param.action}&page=${i}${queryString}"
                           class="page-btn ${i == currentPage ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>
                </c:if>

                <!-- Nếu >5 trang -->
                <c:if test="${totalPages > 5}">

                    <!-- Page 1 -->
                    <a href="${pageContext.request.contextPath}/dashboard/notification?action=${param.action}&page=1${queryString}"
                       class="page-btn ${currentPage == 1 ? 'active' : ''}">
                        1
                    </a>

                    <!-- ... -->
                    <c:if test="${startPage > 2}">
                        <span class="page-btn disabled">...</span>
                    </c:if>

                    <!-- Middle pages -->
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="${pageContext.request.contextPath}/dashboard/notification?action=${param.action}&page=${i}${queryString}"
                           class="page-btn ${i == currentPage ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                    <!-- ... -->
                    <c:if test="${endPage < totalPages - 1}">
                        <span class="page-btn disabled">...</span>
                    </c:if>

                    <!-- Last page -->
                    <a href="${pageContext.request.contextPath}/dashboard/notification?action=${param.action}&page=${totalPages}${queryString}"
                       class="page-btn ${currentPage == totalPages ? 'active' : ''}">
                        ${totalPages}
                    </a>

                </c:if>

                <!-- Next -->
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/notification?action=${param.action}&page=${currentPage + 1}${queryString}"
                       class="page-btn">»</a>
                </c:if>

            </div>
        </c:if>


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

                <form action="${pageContext.request.contextPath}/dashboard/notification"
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
                    <tr><th>Total Sent:</th><td id="detailTotalSent"></td></tr>
                    <tr><th>Total Read:</th><td id="detailTotalRead"></td></tr>
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
        </script>

        <c:if test="${openCreate}">
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    openPopup();
                });
            </script>
        </c:if>

        <script>
            function openDetailModal(id, title, content, image, created, totalSent, totalRead) {

                document.getElementById("detailId").innerText = id;
                document.getElementById("detailTitle").innerText = title;
                document.getElementById("detailContent").innerText = content;
                document.getElementById("detailCreated").innerText = created;
                document.getElementById("detailTotalSent").innerText = totalSent;
                document.getElementById("detailTotalRead").innerText = totalRead;

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

