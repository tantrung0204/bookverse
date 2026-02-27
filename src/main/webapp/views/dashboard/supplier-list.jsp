<%-- 
    Document   : suppliers
    Created on : 26 Feb 2026, 18:34:05
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Suppliers</p>
        <p class="subtitle">Create and manage suppliers for your library</p>
    </div>

    <div class="content-card">

        <div class="toolbar">
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Suppliers
            </button>

            <form action="${pageContext.request.contextPath}/supplier" method="get">
                <input type="hidden" name="action" value="search" />
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search supplier..." value="${keyword}">
                </div>
                <button type="submit" hidden></button>
            </form>
        </div>

        <c:choose>
            <c:when test="${not empty suppliers}">
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success">
                        ${successMsg}
                    </div>
                    <c:remove var="successMsg" scope="session" />
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-error">
                        ${errorMsg}
                    </div>
                    <c:remove var="errorMsg" scope="session" />
                </c:if>
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="20%">Supplier Name</th>
                            <th width="15%">Supplier Email</th>
                            <th width="15%">Supplier Phone</th>
                            <th width="35%">Supplier Address</th>
                            <th width="20%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${suppliers}">
                            <tr>
                                <td><strong>${s.supplierId}</strong></td>
                                <td>${s.supplierName}</td>
                                <td>${s.supplierEmail}</td>
                                <td>${s.supplierPhone}</td>
                                <td>${s.supplierAddress}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${s.status == 1}">
                                            <span class="badge-status badge-active">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-inactive">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail"
                                                title="View Detail" onclick="openDetailPopup(
                                                                '${s.supplierId}',
                                                                '${s.supplierName}',
                                                                '${s.supplierEmail}',
                                                                '${s.supplierPhone}',
                                                                '${s.supplierAddress}',
                                                                '${s.status}'
                                                                )">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit" title="Edit"
                                                onclick="openEditPopup(
                                                                '${s.supplierId}',
                                                                '${s.supplierName}',
                                                                '${s.supplierEmail}',
                                                                '${s.supplierPhone}'
                                                                '${s.supplierAddress}'
                                                                '${s.status}'
                                                                )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/supplier"
                                              method="post" style="display:inline;"
                                              onsubmit="return confirmDelete('${s.supplierId}', '${s.supplierName}')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${s.supplierId}">
                                            <button type="submit" class="btn-action btn-delete"
                                                    title="Delete">
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
                    <p class="mt-2">No suppliers found.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty deleteError}">
            <div class="alert alert-danger mt-3 ">${deleteError}</div>
            <c:remove var="deleteError" scope="session" />
        </c:if>
    </div>
</div>

<!--================= DETAIL POPUP =================--> 
<div id="detailPopup" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Supplier Detail</h3>
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
                <th>Email:</th>
                <td id="detailEmail"></td>
            </tr>
            <tr>
                <th>Phone:</th>
                <td id="detailPhone"></td>
            </tr>
            <tr>
                <th>Address:</th>
                <td id="detailAddress"></td>
            </tr>
            <tr>
                <th>Status:</th>
                <td id="detailStatus"></td>
            </tr>
        </table>

        <div class="modal-footer">
            <button type="button" class="btn-cancel" onclick="closeDetailPopup()">Close</button>
        </div>
    </div>
</div>

<script>
    function openDetailPopup(id, name, email, phone, address, status) {
        document.getElementById("detailId").innerText = id;
        document.getElementById("detailName").innerText = name;
        document.getElementById("detailEmail").innerText = email;
        document.getElementById("detailPhone").innerText = phone;
        document.getElementById("detailAddress").innerText = address;
        document.getElementById("detailStatus").innerText = status == 1 ? "Active" : "Inactive";



        document.getElementById("detailPopup").style.display = "flex";
    }
    function closeDetailPopup() {
        document.getElementById("detailPopup").style.display = "none";
    }
</script>
