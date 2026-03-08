<%-- 
    Document   : export-list
    Created on : Mar 3, 2026, 5:05:34 PM
    Export     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Inventory</p>
        <p class="subtitle">Create and manage inventory for your library</p>
    </div>

    <a href="inventory">
        <button type="button" class="btn-add">Import</button>
    </a>
    <a href="inventory?view=export-list">
        <button type="button" class="btn-add">Export</button>
    </a>

    <div class="content-card">

        <!--        <div class="toolbar">
        <%-- Add import --%>
        <button type="button" class="btn-add" onclick="openCreatePopup()">
            <i class="bi bi-plus-lg me-1"></i> Add New Import
        </button>
        <%-- Search Export --%>
        <form action="Export" method="get" class="search-form">
            <input type="hidden" name="view" value="search">
            <div class="search-box">
                <i class="bi bi-search"></i>          
                <input type="text" name="keyword" placeholder="Search categories..." value="${keyword}">
            </div>
        </form>
    </div>-->
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
        <c:choose>
            <%-- Export List --%>
            <c:when test="${not empty exports}">
                <table class="custom-table">                 
                    <tr>
                        <th width="10%">ID</th>

                        <th width="35%">Staff</th>

                        <th width="15%">Total cost</th>   

                        <th width="15%">Creation date</th>
                    </tr>
                    <c:forEach var="e" items="${exports}"> 
                        <tr>
                            <td><Strong>${e.orderId}</strong></td>
                            <td>${e.staffId.fullName}</td>
                            <td>${e.totalAmount}</td>
                            <td>${e.createdAt}</td>
                            <!--                            <td>
                            <%-- Detail Export --%>
                            <div class="action-buttons">
                                <button type="button" class="btn-action btn-detail"
                                        title="Detail" onclick="openDetailPopup(
                                                        '${a.ExportId}',
                                                        '${a.ExportName}',
                                                        '${a.birthYear}',
                                                        '${a.nationality}',
                                                        '${a.biographyText}'
                                                        )">
                                    <i class="bi bi-eye"></i>
                                </button>                                   
                            </div> 
                        </td>   -->
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
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="inventory?view=export-list&page=${currentPage - 1}">&laquo;</a>
                </li>
                <%-- If the total <= 5, display all pages. --%>
                <c:if test="${totalPages <= 5}">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="inventory?view=export-list&page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                </c:if>
                <%-- If the total > 5 --%>
                <c:if test="${totalPages > 5}">
                    <%-- Page 1 always appears --%>
                    <li class="page-item ${currentPage == 1 ? 'active' : ''}">
                        <a class="page-link" href="inventory?view=export-list&page=1">1</a>
                    </li>
                    <%-- The ... mark at the beginning --%>
                    <c:if test="${startPage > 2}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>
                    <%-- Middle page --%>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="inventory?view=export-list&page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <%-- The final ellipsis --%>
                    <c:if test="${endPage < totalPages - 1}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>
                    <%-- The last page always appears --%>
                    <li class="page-item ${currentPage == totalPages ? 'active' : ''}">
                        <a class="page-link" href="inventory?view=export-list&page=${totalPages}">
                            ${totalPages}
                        </a>
                    </li>
                </c:if>
                <%-- Next button --%>
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="inventory?view=export-list&page=${currentPage + 1}">&raquo;</a>
                </li>
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
        function openDetailPopup(exportId, page) {
            fetch("inventory?view=exportDetail&exportId=" + exportId + "&page=" + page)
                    .then(response => response.json())
                    .then(data => {
                        console.log("DATA:", data);
                        //ép data thành array để dùng forEach.
                        if (!Array.isArray(data)) {
                            data = [data];
                        }
                        //xem data ở console chơi.
                        console.log("DATA:", data);
                        var html = `
        <table class="detail-table">
            <tr>
                <th style="width: 10%">Customer Name</th>
                <th style="width: 20%">Staff Name</th>
                <th style="width: 20%">Product Name</th>
                <th style="width: 20%">Quantity</th>
                <th style="width: 10%">Create At</th>
            </tr>
    `;
                        if (data.length === 0) {
                            html += `<tr><td colspan="5">No import details found</td></tr>`;
                        } else {
                            data.forEach(iteam => {
                                let cusName = iteam.customerName;
                                let staffName = iteam.staffName;
                                let proName = iteam.exportedQuantity;
                                let quantity = iteam.unitPrice;
                                let createAt = iteam.note || "empty";
                                html += `           
                                <tr>
                                    <td>` + id + `</td>
                                    <td>` + name + `</td>
                                    <td>` + quan + `</td>
                                    <td>` + unitPri + `</td>
                                    <td>` + note + `</td></tr>       
    `;
                            });
                        }
                        html += `</table>`;
                        document.getElementById("popupContent").innerHTML = html;
                        document.getElementById("detailPopup").style.display = "flex";
                    })
                    .catch(error => {
                        document.getElementById("popupContent").innerHTML =
                                "<p style='color:red;text-align:center;'>Failed to load data</p>";
                        document.getElementById("detailPopup").style.display = "flex";
                        console.error(error);
                    });
        }
        function confirmDelete(id, name) {
            return confirm("Are you sure you want to delete Export:\n" + name + " (ID: " + id + ")");
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

    <!-- ================= CREATE POPUP ================= -->
    <div id="createPopup" class="modal-overlay">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Add new Export</h3>
            </div>

            <c:if test="${not empty createError}">
                <div class="alert alert-danger" id="createErrorMsg">
                    ${createError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/Export" method="post">
                <input type="hidden" name="action" value="create">

                <div class="form-group">
                    <label>Export Name</label>
                    <input type="text" name="name" class="form-control" value="${createName}">
                </div>

                <div class="form-group">
                    <label>BirthDay</label>
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
    <div id="detailPopup" class="modal-overlay" >
        <div class="modal-content" style="width: 700px">
            <div class="modal-header">
                <h3>Export Detail</h3>
            </div>
            <div id="popupContent"></div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeDetailPopup()">Close</button>
            </div>

        </div>

    </div>
</div>