<%-- 
    Document   : author-list
    Created on : Feb 25, 2026, 7:47:00 PM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/voucher-list.css">

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Author</p>
        <p class="subtitle">Create and manage author for your library</p>
    </div>
    <div class="content-card">

        <div class="toolbar">
            <%-- Add author --%>
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Author
            </button>
            <%-- Search author --%>
            <form action="author" method="get" class="search-form">
                <input type="hidden" name="view" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>          
                    <input type="text" name="keyword" placeholder="Search authors..." value="${keyword}">
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
            <%-- Author List --%>
            <c:when test="${not empty authors}">
                <table class="custom-table">                 
                    <tr>
                        <th width="10%">ID</th>

                        <th width="20%">Author name</th>

                        <th width="20%">Nationality</th>

                        <th width="20%">Biography</th>   

                        <th width="20%">Action</th>
                    </tr>
                    <c:forEach var="a" items="${authors}"> 
                        <tr>
                            <td><Strong>${a.authorId}</strong></td>
                            <td>${a.authorName}</td>
                            <td>${a.nationality}</td>
                            <td>${a.biographyText}</td>
                            <td>
                                <%-- Detail author --%>
                                <div class="action-buttons">
                                    <button type="button" class="btn-action btn-detail"
                                            title="Detail" onclick="openDetailPopup(
                                                            '${a.authorId}',
                                                            '${a.authorName}',
                                                            '${a.birthYear}',
                                                            '${a.nationality}',
                                                            '${a.biographyText}'
                                                            )">
                                        <i class="bi bi-eye"></i>
                                    </button>
                                    <%-- Edit author --%>
                                    <button type="button" class="btn-action btn-edit" title="Edit"
                                            onclick="openEditPopup(
                                                            '${a.authorId}',
                                                            '${a.authorName}',
                                                            '${a.birthYear}',
                                                            '${a.nationality}',
                                                            '${a.biographyText}'
                                                            )">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <%-- Delete author --%>
                                    <form action="${pageContext.request.contextPath}/author" method="post" style="display:inline;"
                                          onsubmit="return confirmDelete('${a.authorId}', '${a.authorName}')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${a.authorId}">                                       
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
                    <a class="page-btn" href="author?page=${currentPage - 1}">&laquo;</a>
                </c:if>
                <%-- If the total <= 5, display all pages. --%>
                <c:if test="${totalPages <= maxNote}">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a class="page-btn ${currentPage == i ? 'active' : ''}" href="author?page=${i}">${i}</a>
                    </c:forEach>
                </c:if>
                <%-- If the total > 5 --%>
                <c:if test="${totalPages > maxNote}">
                    <%-- Page 1 always appears --%>                   
                    <a class="page-btn ${currentPage == 1 ? 'active' : ''}"href="author?page=1">1</a>
                    <%-- The ... mark at the beginning --%>
                    <c:if test="${startPage > 2}">
                        <span class="page-btn disabled">...</span>
                    </c:if>
                    <%-- Middle page --%>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a class="page-btn ${currentPage == i ? 'active' : ''}" href="author?page=${i}">${i}</a>
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
                    <a class="page-btn" href="author?page=${currentPage + 1}">&raquo;</a>
                </c:if>
            </ul>
        </nav>
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
        function openDetailPopup(id, name, birth, nat, bio) {
            document.getElementById("detailId").innerText = id;
            document.getElementById("detailName").innerText = name;
            document.getElementById("detailBirth").innerText = birth;
            document.getElementById("detailNat").innerText = nat;
            document.getElementById("detailBio").innerText = bio;
            document.getElementById("detailQuantity").innerText = "Loading...";

            fetch('${pageContext.request.contextPath}/author?view=detail&id=' + id)
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById("detailQuantity").innerText = data.quantity + " products";
                    })
                    .catch(error => {
                        document.getElementById("detailQuantity").innerText = "Error";
                    });
            document.getElementById("detailPopup").style.display = "flex";
        }
        function openEditPopup(id, name, birth, nat, bio) {
            document.getElementById("editAuthorId").value = id;
            document.getElementById("editAuthorName").value = name;
            document.getElementById("editAuthorBirth").value = birth;
            document.getElementById("editNat").value = nat;
            document.getElementById("editBio").value = bio;

            const err = document.getElementById("editErrorMsg");
            if (err)
                err.style.display = 'none';

            document.getElementById("editPopup").style.display = "flex";
        }
        function closeEditPopup() {
            document.getElementById("editPopup").style.display = "none";
        }
        function confirmDelete(id, name) {
            return confirm("Are you sure you want to delete author:\n" + name + " (ID: " + id + ")");
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
                            '${birth}',
                            '${nationality}',
                            '${biography}'
                            );
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
                setTimeout(function () {
                    openEditPopup(
                            '${editId}',
                            '${editName}',
                            '${editBirth}',
                            '${editNat}',
                            '${editBio}'
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
                <h3>Add new author</h3>
            </div>

            <c:if test="${not empty createError}">
                <div class="alert alert-danger" id="createErrorMsg">
                    ${createError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/author" method="post">
                <input type="hidden" name="action" value="create">

                <div class="form-group">
                    <label>Author Name</label>
                    <input type="text" name="name" class="form-control" value="${createName}">
                </div>

                <div class="form-group">
                    <label>Year of birth</label>
                    <input type="text" name="birth" class="form-control" value="${birth}">
                </div>

                <div class="form-group">
                    <label>Nationality</label>
                    <input type="text" name="nationality" class="form-control" value="${nationality}">
                </div>

                <div class="form-group">
                    <label>Biography</label>
                    <input type="text" name="biography" class="form-control" value="${biography}">
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
                <h3>Author Detail</h3>
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
                    <th>Year of birth:</th>
                    <td id="detailBirth"></td>
                </tr>
                <tr>
                    <th>Nationality:</th>
                    <td id="detailNat"></td>
                </tr>
                <tr>
                    <th>Biography:</th>
                    <td id="detailBio"></td>
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
    <!-- ================= EDIT POPUP ================= -->
    <div id="editPopup" class="modal-overlay">
        <div class="modal-content ">
            <div class="modal-header">
                <h3>Edit Author</h3>
            </div>

            <c:if test="${not empty editError}">
                <div class="alert alert-danger p-2 mb-3 alert-error" id="editErrorMsg" style="font-size: 13px;">
                    ${editError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/author" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="authorId" id="editAuthorId">

                <div class="form-group">
                    <label>Author Name</label>
                    <input type="text" name="authorName" id="editAuthorName" class="form-control">
                </div>
                <div class="form-group">
                    <label>Year of birth</label>
                    <input type="text" name="birth" id="editAuthorBirth" class="form-control">
                </div>
                <div class="form-group">
                    <label>Nationality</label>
                    <input type="text" name="nationality" id="editNat" class="form-control">
                </div>

                <div class="form-group">
                    <label>Biography</label>
                    <input type="text" name="biography" id="editBio" class="form-control">

                </div>

                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeEditPopup()">Cancel</button>
                    <button type="submit" class="btn-save">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>