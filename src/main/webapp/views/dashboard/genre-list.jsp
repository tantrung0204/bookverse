<%-- 
    Document   : genre-view
    Created on : Feb 11, 2026, 8:48:53 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/voucher-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Genre</p>
        <p class="subtitle">Create and manage book Genre for your library</p>
    </div>
    <div class="content-card">
        <div class="toolbar">
            <%-- Add Genre --%>
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Genre
            </button>
            <%-- Search Genre --%>
            <form action="genre" method="get" class="search-form">
                <input type="hidden" name="view" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>          
                    <input type="text" name="keyword" placeholder="Search genres..." value="${keyword}">
                </div>
            </form>
        </div>
        <c:if test="${not empty message}">
            <div class="alert alert-error">
                ${message}
            </div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">
                ${success}
            </div>
        </c:if>
        <c:if test="${not empty deleteError}">
            <div class="alert alert-error">${deleteError}</div>
            <c:remove var="deleteError" scope="session"/>
        </c:if>
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
                                    <button type="button" class="btn-action btn-detail"
                                            title="Detail" onclick="openDetailPopup(
                                                            '${g.genreId}',
                                                            '${g.genreName}',
                                                            '${g.descriptionText}',
                                                            '${g.status}'
                                                            )">
                                        <i class="bi bi-eye"></i>
                                    </button>

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
            <%-- pagination --%>
        </c:choose>
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
        <nav class="d-flex justify-content-center">
            <ul class="pagination">
                <%-- Previous button --%>
                <c:if test="${currentPage > 1}">
                    <a class="page-btn ${currentPage == 1 ? 'disabled' : ''}" href="genre?page=${currentPage - 1}">&laquo;</a>
                </c:if>
                <%-- If the total <= 5, display all pages. --%>
                <c:if test="${totalPages <= 5}">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a class="page-btn ${currentPage == i ? 'active' : ''}" href="genre?page=${i}">${i}</a>
                    </c:forEach>
                </c:if>
                <%-- If the total > 5 --%>
                <c:if test="${totalPages > 5}">
                    <%-- Page 1 always appears --%>                   
                    <a class="page-btn ${currentPage == 1 ? 'active' : ''}"href="genre?page=1">1</a>
                    <%-- The ... mark at the beginning --%>
                    <c:if test="${startPage > 2}">
                        <span class="page-btn disabled">...</span>
                    </c:if>
                    <%-- Middle page --%>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a class="page-btn ${currentPage == i ? 'active' : ''}" href="genre?page=${i}">${i}</a>
                    </c:forEach>
                    <%-- The final ellipsis --%>
                    <c:if test="${endPage < totalPages - 1}">
                        <span class="page-btn disabled">...</span>
                    </c:if>
                    <%-- The last page always appears --%>
                    <a class="page-btn ${currentPage == totalPages ? 'active' : ''}" href="author?page=${totalPages}">
                        ${totalPages}
                    </a>
                </c:if>
                <%-- Next button --%>
                <c:if test="${currentPage < totalPages}">
                    <a class="page-btn ${currentPage == totalPages ? 'disabled' : ''}" href="genre?page=${currentPage+1}">&raquo;</a>
                </c:if>
            </ul>
        </nav>
    </div>

</div>

<script>
    function openCreatePopup() {
        document.getElementById("createPopup").style.display = "flex";
        const err = document.getElementById("createErrorMsg");
        if (err)
            err.style.display = 'none';
    }
    function closeCreatePopup() {
        document.getElementById("createPopup").style.display = "none";
    }
    function closeDetailPopup() {
        document.getElementById("detailPopup").style.display = "none";
    }
    function openDetailPopup(id, name, desc, status) {
        document.getElementById("detailId").innerText = id;
        document.getElementById("detailName").innerText = name;
        document.getElementById("detailDesc").innerText = desc;
        document.getElementById("detailStatus").innerText = status == 1 ? "Active" : "Inactive";
        document.getElementById("detailQuantity").innerText = "Loading...";

        fetch('${pageContext.request.contextPath}/genre?view=detail&id=' + id)
                .then(response => response.json())
                .then(data => {
                    document.getElementById("detailQuantity").innerText = data.quantity + " products";
                })
                .catch(error => {
                    document.getElementById("detailQuantity").innerText = "Error";
                });
        document.getElementById("detailPopup").style.display = "flex";
    }
    function openEditPopup(id, name, description, status) {
        document.getElementById("editGenreId").value = id;
        document.getElementById("editGenreName").value = name;
        document.getElementById("editDescription").value = description;
        document.getElementById("editStatus").value = status;

        const err = document.getElementById("editErrorMsg");
        if (err)
            err.style.display = 'none';

        document.getElementById("editPopup").style.display = "flex";
    }
    function closeEditPopup() {
        document.getElementById("editPopup").style.display = "none";
    }
    function confirmDelete(id, name) {
        return confirm("Are you sure you want to delete Genre:\n" + name + " (ID: " + id + ")");
    }
    let popupTimer = null;
    window.onclick = function (event) {
        var modal = document.getElementById("editPopup");
        if (event.target == modal) {
            closeEditPopup();
        }
    }
</script>
<c:if test="${openCreatePopup}">
    <script>
        window.onload = function () {
            setTimeout(function () {
                openCreatePopup(
                        '${createName}',
                        '${createDesc}',
                        '${createStatus}'
                        );
                // Hiển thị lại lỗi
                const err = document.getElementById("createErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>

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
                const err = document.getElementById("editErrorMsg");
                if (err)
                    err.style.display = 'block';
            }, 100);
        };
    </script>
</c:if>
<!-- ================= CREATE POPUP ================= -->
<div id="createPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Add new genre</h3>
        </div>

        <c:if test="${not empty createError}">
            <div class="alert alert-danger" id="createErrorMsg">
                ${createError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/genre" method="post">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
                <label>Genre Name</label>
                <input type="text" name="name" class="form-control" value="${createName}">
            </div>

            <div class="form-group">
                <label>Description</label>
                <input type="text" name="description" class="form-control" value="${createDes}">
            </div>

            <div class="form-group">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="1" ${createStatus==1 ? "selected" : "" }>Active</option>
                    <option value="0" ${createStatus==0 ? "selected" : "" }>Inactive</option>
                </select>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeCreatePopup()">Cancel</button>
                <button type="submit" class="btn-save">Create</button>
            </div>
        </form>
    </div>
</div>

<!-- ================= DETAIL POPUP ================= -->
<div id="detailPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Genre Detail</h3>
        </div>

        <table class="detail-table">
            <tr>
                <th>ID:</th>
                <td id="detailId"></td>
            </tr>
            <tr>
                <th>Name:</th>
                <td id="detailName"></td>
            </tr>
            <tr>
                <th>Description:</th>
                <td id="detailDesc"></td>
            </tr>
            <tr>
                <th>Status:</th>
                <td id="detailStatus"></td>
            </tr>
            <tr>
                <th>Quantity:</th>
                <td id="detailQuantity"></td>
            </tr>
        </table>

        <div class="modal-footer">
            <button type="button" class="btn-cancel" onclick="closeDetailPopup()">Close</button>
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

        <form action="${pageContext.request.contextPath}/genre" method="post">
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

