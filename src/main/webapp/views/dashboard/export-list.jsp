<%-- 
    Document   : export-list
    Created on : Mar 3, 2026, 5:05:34 PM
    Author     : LECOO
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

        <div class="toolbar">
            <%-- Add import --%>
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Import
            </button>
            <%-- Search author --%>
            <form action="author" method="get" class="search-form">
                <input type="hidden" name="view" value="search">
                <div class="search-box">
                    <i class="bi bi-search"></i>          
                    <input type="text" name="keyword" placeholder="Search categories..." value="${keyword}">
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
        <c:choose>
            <%-- Author List --%>
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
         function openDetailPopup(importId, page) {
            fetch("inventory?view=importDetail&importId=" + importId + "&page=" + page)
                    .then(response => response.json())
                    .then(data => {
                        console.log("DATA:", data);
                        //ép data thành array để dùng forEach.
                        if (!Array.isArray(data)) {
                            data = [data];
                        }
                        //xem data ở console chơi.
                        console.log("DATA:", data);
                        let currentPage = data[0].currentPage;
                        let totalPages = data[0].totalPages;
                        var html = `
        <table class="detail-table">
            <tr>
                <th style="width: 10%">ID</th>
                <th style="width: 20%">Product Name</th>
                <th style="width: 20%">Quantity</th>
                <th style="width: 20%">Unit Price</th>
                <th style="width: 10%">Note</th>
            </tr>
    `;
                        if (data.length === 0) {
                            html += `<tr><td colspan="5">No import details found</td></tr>`;
                        } else {
                            data.forEach(iteam => {
                                let id = iteam.importDetailId;
                                let name = iteam.product ? iteam.product.name : "";
                                let quan = iteam.importedQuantity;
                                let unitPri = iteam.unitPrice;
                                let note = iteam.note || "empty";
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
                        //Phân trang
                        let startPage = currentPage - 1;//là page số 2 trở về sau
                        let endPage = currentPage + 1;//là page kề page cuối
                        //Nếu currentPage = 1 thì hiển thị từ trang 2 trở đi
                        if (startPage < 2) {
                            startPage = 2;
                            endPage = 4;
                        }
                        //Nếu currentPage = page cuối
                        if (endPage > totalPages - 1) {
                            endPage = totalPages - 1;
                            startPage = totalPages - 3;
                        }
                        //Luôn luôn đặt startPage=2.
                        if (startPage < 2) {
                            startPage = 2;
                        }

                        let pagination = `<nav class="d-flex justify-content-center">
                <ul class="pagination">`;

                        // Previous
                        pagination += `
            <li class="page-item ` + (currentPage === 1 ? 'disabled' : '') + `">
                <button class="page-link" onclick="openDetailPopup(` + importId + `,` + (currentPage - 1 < 1 ? 1 : currentPage - 1) + `)">&laquo;</button>
            </li>`;

                        // Nếu total <= 5
                        if (totalPages <= 5) {
                            for (let i = 1; i <= totalPages; i++) {
                                pagination += `
                    <li class="page-item ` + (currentPage === i ? 'active' : '') + `">
                        <button class="page-link" onclick="openDetailPopup(` + importId + `,` + i + `)">` + i + `</button>
                    </li>`;
                            }
                        } else {

                            // page 1
                            pagination += `
                <li class="page-item ` + (currentPage === 1 ? 'active' : '') + `">
                    <button class="page-link" onclick="openDetailPopup(` + importId + `,` + 1 + `)">1</button>
                </li>`;

                            if (startPage > 2) {
                                pagination += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
                            }

                            for (let i = startPage; i <= endPage; i++) {
                                pagination += `
                    <li class="page-item ` + (currentPage === i ? 'active' : '') + `">
                        <button class="page-link" onclick="openDetailPopup(` + importId + `,` + i + `)">` + i + `</button>
                    </li>`;
                            }

                            if (endPage < totalPages - 1) {
                                pagination += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
                            }

                            pagination += `
                <li class="page-item ` + (currentPage === totalPages ? 'active' : '') + `">
                    <button class="page-link" onclick="openDetailPopup(` + importId + `,` + totalPages + `)">` + totalPages + `</button>
                </li>`;
                        }

                        // Next
                        pagination += `
            <li class="page-item ` + (currentPage === totalPages ? 'disabled' : '') + `">
                <button class="page-link" onclick="openDetailPopup(` + importId + `,` + (currentPage + 1) + `)">&raquo;</button>
            </li>`;

                        pagination += `</ul></nav>`;

                        html += pagination;
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
            return confirm("Are you sure you want to delete author:\n" + name + " (ID: " + id + ")");
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
                <h3>Author Detail</h3>
            </div>
            <div id="popupContent"></div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeDetailPopup()">Close</button>
            </div>

        </div>

    </div>
</div>