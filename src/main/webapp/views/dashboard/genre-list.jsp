<%-- 
    Document   : genre-view
    Created on : Feb 11, 2026, 8:48:53 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Genre</p>
        <p class="subtitle">Create and manage book Genre for your library</p>
    </div>
    <div class="content-card">

        <div class="toolbar">
            <%-- Add Genre --%>
            <a class="btn-add" href="${pageContext.request.contextPath}/genre?view=create">
                <i class="bi bi-plus-lg me-1"></i> Add New Genre
            </a>
            <%-- Search Genre --%>
            <form action="genre" method="get" class="search-form">
                <input type="hidden" name="view" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>          
                    <input type="text" name="keyword" placeholder="Search categories..." value="${keyword}">
                </div>
            </form>
        </div>
        <c:choose>
            <%-- Genre List --%>
            <c:when test="${not empty genres}">
                <table class="custom-table">                 
                    <tr>
                        <th width="10%">ID</th>

                        <th width="20%">Genre name</th>

                        <th width="35%">Description</th>

                        <th width="15%">Status</th>   

                        <th width="20%">Actions</th>
                    </tr>
                    <c:forEach var="g" items="${genres}"> 
                        <tr>
                            <td><Strong>${g.genreId}</strong></td>
                            <td>${g.genreName}</td>
                            <td>${g.descriptionText}</td>
                            <td>
                                <c:if test="${g.status==1}">
                                    <span class="badge-status badge-active">Active</span>
                                </c:if>
                                <c:if test="${g.status==0}">
                                    <span class="badge-status badge-inactive">Inactive</span>
                                </c:if>
                            </td>
                            <td>
                                <%-- Detail Genre --%>
                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/genre?view=detail&id=${g.genreId}"
                                       class="btn-action btn-detail" title="View Detail">                                   
                                        <i class="bi bi-eye"></i>
                                    </a>

                                    <%-- Edit Genre --%>
                                    <button type="button" class="btn-action btn-edit" title="Edit"
                                            onclick="openEditPopup(
                                                        '${g.genreId}',
                                                        '${g.genreName}',
                                                        '${g.descriptionText}',
                                                        '${g.status}'
                                                        )">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <%-- Delete Genre --%>
                                    <form action="${pageContext.request.contextPath}/genre" method="post" style="display:inline;"
                                          onsubmit="return confirmDelete('${g.genreId}', '${g.genreName}')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${g.genreId}">
                                        <button type="submit" class="btn-action btn-delete" title="Delete">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </td>   
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <c:if test="${not empty message}">
                    <div style="padding:10px;margin:10px 0;
                         background:#f8d7da;color:#721c24;
                         border:1px solid #f5c6cb;border-radius:5px;">
                        ${message}
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty deleteError}">
            <div class="alert alert-danger mt-3">${deleteError}</div>
            <c:remove var="deleteError" scope="session"/>
        </c:if>
    </div>

    <div id="editPopup" class="modal-overlay">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Edit Genre</h3>
            </div>

            <c:if test="${not empty editError}">
                <div class="alert alert-danger p-2 mb-3" id="editErrorMsg" style="font-size: 13px;">
                    ${editError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/Genre" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="genreId" id="editGenreId">

                <div class="form-group">
                    <label>Genre Name</label>
                    <input type="text" name="genreName" id="editGenreName" class="form-control" required>
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <input type="text" name="descriptionText" id="editDescription" class="form-control">
                </div>

                <div class="form-group">
                    <label>Status</label>
                    <select name="status" id="editStatus" class="form-control">
                        <option value="1">Active</option>
                        <option value="0">Inactive</option>
                    </select>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeEditPopup()">Cancel</button>
                    <button type="submit" class="btn-save">Save Changes</button>
                </div>
            </form>
        </div>
    </div> 
</div>
<%-- Edit Genre po-up--%>
<div id="editPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Edit Genre</h3>
        </div>

        <c:if test="${not empty editError}">
            <div class="alert alert-danger p-2 mb-3" id="editErrorMsg" style="font-size: 13px;">
                ${editError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/edit" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="id" value="${genre.genreId} id="editGenreId"">

            <div class="form-group">
                <label>Genre Name</label>
                <input type="text" name="name" value="${genre.genreName}" id="editGenreName" class="form-control" required>
            </div>

            <div class="form-group">
                <label>Description</label>
                <input type="text" name="description" value="${genre.descriptionText}" id="editDescription" class="form-control">
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" id="status" class="form-control" id="editStatus">
                    <option value="1">Active</option>
                    <option value="0">Inactive</option>
                </select>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeEditPopup()">Cancel</button>
                <button type="submit" class="btn-save">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<script>
    // Hàm mở Popup và điền dữ liệu
    function openEditPopup(id, name, description, status) {
        document.getElementById("editGenreId").value = id;
        document.getElementById("editGenreName").value = name;
        document.getElementById("editDescription").value = description;
        document.getElementById("editStatus").value = status;

        // Ẩn thông báo lỗi cũ nếu có
        const err = document.getElementById("editErrorMsg");
        if (err)
            err.style.display = 'none';

        // Hiển thị modal (sử dụng Flex để căn giữa)
        document.getElementById("editPopup").style.display = "flex";
    }

    // Hàm đóng Popup
    function closeEditPopup() {
        document.getElementById("editPopup").style.display = "none";
    }

    // Xử lý confirm xóa
    function confirmDelete(id, name) {
        return confirm("Are you sure you want to delete Genre:\n" + name + " (ID: " + id + ")?");
    }

    // Đóng Popup khi click ra ngoài vùng trắng
    window.onclick = function (event) {
        var modal = document.getElementById("editPopup");
        if (event.target == modal) {
            closeEditPopup();
        }
    }
</script>



<c:if test="${openEditPopup}">
    <script>
        window.onload = function () {
            // Đảm bảo DOM đã load xong
            setTimeout(function () {
                openEditPopup(
                        '${editId}',
                        '${editName}',
                        '${editDesc}',
                        '${editStatus}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("editErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>

