<%-- Document : product-list.jsp Created on : 24 Feb 2026, 16:55:18 Author : NganTTK-CE190411 --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <fmt:setLocale value="vi_VN" />

                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-list.css">
                <div class="container-fluid">

                    <div class="page-header">
                        <p class="title">Manage Products</p>
                        <p class="subtitle">Create and manage books and stationery for your library</p>
                    </div>

                    <div class="content-card">
                        <div class="tab-container">
                            <a href="${pageContext.request.contextPath}/dashboard/product?tab=book"
                                class="tab-item ${currentTab == 'book' ? 'active' : ''}">
                                <i class="bi bi-book me-1"></i> Books
                            </a>
                            <a href="${pageContext.request.contextPath}/dashboard/product?tab=stationery"
                                class="tab-item ${currentTab == 'stationery' ? 'active' : ''}">
                                <i class="bi bi-pencil-square me-1"></i> Stationery
                            </a>
                        </div>

                        <div class="toolbar">
                            <button type="button" class="btn-add">
                                <i class="bi bi-plus-lg me-1"></i> Add New Product
                            </button>

                            <form action="${pageContext.request.contextPath}/dashboard/product" method="GET">
                                <input type="hidden" name="tab" value="${currentTab}">
                                <div class="search-box">
                                    <i class="bi bi-search"></i>
                                    <input type="text" name="keyword" placeholder="Search products..."
                                        value="${currentKeyword}">
                                </div>
                                <button type="submit" hidden></button>
                            </form>
                        </div>
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
                                    <th width="25%">Name</th>
                                    <th width="15%">Price</th>
                                    <th width="10%">Quantity</th>
                                    <th width="10%">Type</th>
                                    <th width="15%">Status</th>
                                    <th width="15%">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty productList}">
                                        <c:forEach var="product" items="${productList}">
                                            <tr>
                                                <td><strong>${product.productId}</strong></td>
                                                <td>${product.name}</td>
                                                <td>
                                                    <fmt:formatNumber value="${product.price}" pattern="#,###" /> đ
                                                </td>
                                                <td>${product.stockQuantity}</td>
                                                <td>${product.type}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${product.status == 1}">
                                                            <span class="badge-status badge-active">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge-status badge-inactive">Inactive</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="${pageContext.request.contextPath}/dashboard/product?action=detail&id=${product.productId}&tab=${currentTab}&keyword=${currentKeyword}&page=${currentPage}"
                                                            class="btn-action btn-detail" title="View Detail"
                                                            style="display: inline-flex; align-items: center; justify-content: center; text-decoration: none;">
                                                            <i class="bi bi-eye"></i>
                                                        </a>


                                                        <a href="${pageContext.request.contextPath}/dashboard/product?action=edit&id=${product.productId}&tab=${currentTab}&keyword=${currentKeyword}&page=${currentPage}"
                                                            class="btn-action btn-edit" title="Edit"
                                                            style="display: inline-flex; align-items: center; justify-content: center; text-decoration: none;">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>

                                                        <form
                                                            action="${pageContext.request.contextPath}/dashboard/product"
                                                            method="post" style="display:inline;"
                                                            onsubmit="return confirm('Are you sure you want to delete product:\n${product.name} (ID: ${product.productId})')">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="id" value="${product.productId}">
                                                            <input type="hidden" name="tab" value="${currentTab}">
                                                            <button type="submit" class="btn-action btn-delete"
                                                                title="Delete">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7">
                                                <div style="text-align:center; padding: 40px; color: #999;">
                                                    <i class="bi bi-inbox" style="font-size: 40px;"></i>
                                                    <p class="mt-2">No products found matching your search.</p>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>

                        <c:if test="${totalPages > 1}">
                            <div class="pagination-container">

                                <%-- Tính toán Start Page và End Page --%>
                                    <c:set var="startPage" value="${currentPage - 1}" />
                                    <c:set var="endPage" value="${currentPage + 1}" />

                                    <c:if test="${startPage < 2}">
                                        <c:set var="startPage" value="2" />
                                        <c:set var="endPage" value="4" />
                                    </c:if>

                                    <c:if test="${endPage > totalPages - 1}">
                                        <c:set var="endPage" value="${totalPages - 1}" />
                                        <c:set var="startPage" value="${totalPages - 3}" />
                                    </c:if>

                                    <c:if test="${startPage < 2}">
                                        <c:set var="startPage" value="2" />
                                    </c:if>

                                    <nav>
                                        <ul class="pagination">
                                            <%-- Nút Previous --%>
                                                <c:if test="${currentPage > 1}">
                                                    <a class="page-btn"
                                                        href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}&keyword=${currentKeyword}&page=${currentPage - 1}">&laquo;</a>
                                                </c:if>

                                                <%-- Nếu tổng số trang <=maxNode, hiển thị tất cả --%>
                                                    <c:choose>
                                                        <c:when test="${totalPages <= maxNode}">
                                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                                <a class="page-btn ${currentPage == i ? 'active' : ''}"
                                                                    href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}&keyword=${currentKeyword}&page=${i}">${i}</a>
                                                            </c:forEach>
                                                        </c:when>

                                                        <%-- Nếu tổng số trang> maxNode, dùng dấu ... --%>
                                                            <c:otherwise>
                                                                <%-- Luôn hiện trang 1 --%>
                                                                    <a class="page-btn ${currentPage == 1 ? 'active' : ''}"
                                                                        href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}&keyword=${currentKeyword}&page=1">1</a>

                                                                    <%-- Dấu ... đầu tiên --%>
                                                                        <c:if test="${startPage > 2}">
                                                                            <span class="page-btn disabled">...</span>
                                                                        </c:if>

                                                                        <%-- Các trang ở giữa --%>
                                                                            <c:forEach begin="${startPage}"
                                                                                end="${endPage}" var="i">
                                                                                <c:if
                                                                                    test="${i > 1 and i < totalPages}">
                                                                                    <a class="page-btn ${currentPage == i ? 'active' : ''}"
                                                                                        href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}&keyword=${currentKeyword}&page=${i}">${i}</a>
                                                                                </c:if>
                                                                            </c:forEach>

                                                                            <%-- Dấu ... cuối cùng --%>
                                                                                <c:if
                                                                                    test="${endPage < totalPages - 1}">
                                                                                    <span
                                                                                        class="page-btn disabled">...</span>
                                                                                </c:if>

                                                                                <%-- Luôn hiện trang cuối --%>
                                                                                    <a class="page-btn ${currentPage == totalPages ? 'active' : ''}"
                                                                                        href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}&keyword=${currentKeyword}&page=${totalPages}">${totalPages}</a>
                                                            </c:otherwise>
                                                    </c:choose>

                                                    <%-- Nút Next --%>
                                                        <c:if test="${currentPage < totalPages}">
                                                            <a class="page-btn"
                                                                href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}&keyword=${currentKeyword}&page=${currentPage + 1}">&raquo;</a>
                                                        </c:if>
                                        </ul>
                                    </nav>
                            </div>
                        </c:if>

                    </div>
                </div>

                <!-- ================= DETAIL POPUP ================= -->
                <c:if test="${not empty productDetail}">
                    <div id="detailPopup" class="modal-overlay" style="display: flex;">
                        <div class="modal-content" style="width: 700px; max-width: 90%;">
                            <div class="modal-header">
                                <h3>Product Detail</h3>
                            </div>

                            <div style="display: flex; gap: 20px; text-align: left; padding: 10px 0;">
                                <div style="flex: 0 0 200px;">
                                    <c:choose>
                                        <c:when test="${not empty productDetail.imageUrl}">
                                            <img src="${productDetail.imageUrl}"
                                                style="width: 100%; border-radius: 5px; border: 1px solid #ccc;"
                                                onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';">
                                        </c:when>
                                    </c:choose>
                                </div>

                                <div style="flex: 1;">
                                    <table class="detail-table" style="width: 100%;">
                                        <tr>
                                            <th width="30%">ID:</th>
                                            <td>${productDetail.productId}</td>
                                        </tr>
                                        <tr>
                                            <th>Name:</th>
                                            <td style="font-weight: bold;">${productDetail.name}</td>
                                        </tr>
                                        <tr>
                                            <th>Type:</th>
                                            <td>${productDetail.type}</td>
                                        </tr>
                                        <tr>
                                            <th>Category:</th>
                                            <td>${productDetail.categoryId != null ?
                                                productDetail.categoryId.categoryName : 'N/A'}</td>
                                        </tr>
                                        <tr>
                                            <th>Price:</th>
                                            <td style="color: #d9534f; font-weight: bold;">
                                                <fmt:formatNumber value="${productDetail.price}" pattern="#,###" /> đ
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Quantity:</th>
                                            <td>${productDetail.stockQuantity}</td>
                                        </tr>
                                        <tr>
                                            <th>Rating:</th>
                                            <td>
                                                <fmt:formatNumber value="${productDetail.averageRating}"
                                                    maxFractionDigits="1" /> <i style="color: #ffc107;"
                                                    class="bi bi-star-fill"></i> (${productDetail.reviewCount} reviews)
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Sold:</th>
                                            <td>${productDetail.soldQuantity} items</td>
                                        </tr>
                                        <tr>
                                            <th>Status:</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${productDetail.status == 1}"><span
                                                            class="badge-status badge-active">Active</span></c:when>
                                                    <c:otherwise><span
                                                            class="badge-status badge-inactive">Inactive</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </table>

                                    <c:if test="${productDetail.type == 'Stationery'}">
                                        <table class="detail-table" style="width: 100%; margin-top: 5px;">
                                            <tr>
                                                <th width="30%">Color:</th>
                                                <td>${not empty productDetail.color ? productDetail.color : 'N/A'}</td>
                                            </tr>
                                            <tr>
                                                <th>Material:</th>
                                                <td>${not empty productDetail.material ? productDetail.material : 'N/A'}
                                                </td>
                                            </tr>
                                        </table>
                                    </c:if>

                                    <c:if test="${productDetail.type == 'Book'}">
                                        <table class="detail-table" style="width: 100%; margin-top: 5px;">
                                            <tr>
                                                <th width="30%">Genre:</th>
                                                <td>${productDetail.genreId != null ? productDetail.genreId.genreName :
                                                    'N/A'}</td>
                                            </tr>
                                            <tr>
                                                <th>Author(s):</th>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty productDetail.authorCollection}">
                                                            <c:forEach var="author"
                                                                items="${productDetail.authorCollection}"
                                                                varStatus="st">
                                                                ${author.authorName}${!st.last ? ', ' : ''}
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>N/A</c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </table>
                                    </c:if>
                                </div>
                            </div>

                            <div
                                style="text-align: left; margin-top: 10px; border-top: 1px solid #eee; padding-top: 10px;">
                                <strong>Description:</strong>
                                <p style="font-size: 14px; color: #555; margin-top: 5px;">${not empty
                                    productDetail.descriptionText ? productDetail.descriptionText : 'No description
                                    available.'}</p>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn-cancel"
                                    onclick="document.getElementById('detailPopup').style.display = 'none';">Close</button>
                            </div>
                        </div>
                    </div>
                </c:if>


                <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        // Add New Product Popup
                        var addModal = document.getElementById("addPopup");
                        var btnAdd = document.querySelector(".btn-add");

                        if (btnAdd && addModal) {
                            btnAdd.onclick = function () {
                                addModal.style.display = "flex";
                            }
                        }

                        // Run on load for Create Modal
                        var addTypeRads = document.querySelectorAll('input[name="productType"][form="createForm"]');
                        if (addTypeRads) {
                            addTypeRads.forEach(r => r.addEventListener('change', (e) => toggleProductType('addPopup', e.target.value)));
                        }

                        // Close Modals when clicking outside
                        window.onclick = function (event) {
                            var detailModal = document.getElementById("detailPopup");
                            if (detailModal && event.target == detailModal) {
                                detailModal.style.display = "none";
                            }
                            var addM = document.getElementById("addPopup");
                            if (addM && event.target == addM) {
                                addM.style.display = "none";
                            }
                            var editM = document.getElementById("editPopup");
                            if (editM && event.target == editM) {
                                editM.style.display = "none";
                            }
                        };
                    });

                    function toggleProductType(modalId, type) {
                        var modal = document.getElementById(modalId);
                        if (!modal)
                            return;

                        var bookFields = modal.querySelector('.book-fields');
                        var statFields = modal.querySelector('.stationery-fields');
                        var bookCat = modal.querySelector('.book-category');
                        var statCat = modal.querySelector('.stationery-category');

                        if (type === 'Book') {
                            if (bookFields)
                                bookFields.style.display = 'block';
                            if (statFields)
                                statFields.style.display = 'none';
                            if (bookCat)
                                bookCat.style.display = 'block';
                            if (statCat)
                                statCat.style.display = 'none';
                        } else {
                            if (bookFields)
                                bookFields.style.display = 'none';
                            if (statFields)
                                statFields.style.display = 'block';
                            if (bookCat)
                                bookCat.style.display = 'none';
                            if (statCat)
                                statCat.style.display = 'block';
                        }
                    }
                </script>

                <!-- ================= ADD PRODUCT POPUP ================= -->
                <div id="addPopup" class="modal-overlay" style="display: ${openCreateModal ? 'flex' : 'none'};">
                    <div class="modal-content"
                        style="width: 700px; max-width: 90%; max-height: 90vh; overflow-y: auto;">
                        <div class="modal-header">
                            <h3>Add New Product</h3>
                        </div>
                        <c:if test="${not empty createError}">
                            <div class="alert alert-error" style="color:red; margin-bottom:10px;">${createError}</div>
                        </c:if>
                        <form id="createForm" action="${pageContext.request.contextPath}/dashboard/product"
                            method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="create">
                            <input type="hidden" name="tab" value="${currentTab}">

                            <div class="form-group">
                                <label style="font-weight:bold;">Product Type:</label><br>
                                <label><input type="radio" name="productType" value="Book" ${productCreate.type
                                        !='Stationery' ? 'checked' : '' }
                                        onclick="toggleProductType('addPopup', 'Book')"> Book</label>
                                <label><input type="radio" name="productType" value="Stationery"
                                        ${productCreate.type=='Stationery' ? 'checked' : '' }
                                        onclick="toggleProductType('addPopup', 'Stationery')"> Stationery</label>
                            </div>

                            <div class="form-group">
                                <label>Name: *</label>
                                <input type="text" name="name" class="form-control" value="${productCreate.name}"
                                    required>
                            </div>

                            <div style="display:flex; gap:15px;">
                                <div class="form-group" style="flex:1;">
                                    <label>Price (đ): *</label>
                                    <input type="number" name="price" min="1" step="0.01" class="form-control"
                                        value="${productCreate.price}" required>
                                </div>
                                <div class="form-group" style="flex:1;">
                                    <label>Status:</label>
                                    <select name="status" class="form-control">
                                        <option value="1" ${productCreate.status==1 ? 'selected' : '' }>Active</option>
                                        <option value="0" ${productCreate !=null && productCreate.status==0 ? 'selected'
                                            : '' }>Inactive</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Image:</label>
                                <input type="file" name="imageFile" accept="image/*" class="form-control"
                                    style="padding: 7px;">
                            </div>

                            <div class="form-group book-category"
                                style="display: ${productCreate == null || productCreate.type != 'Stationery' ? 'block' : 'none'};">
                                <label>Category (Book): *</label>
                                <select name="bookCategoryId" class="form-control">
                                    <option value="">-- Select Category --</option>
                                    <c:forEach var="cat" items="${bookCategories}">
                                        <option value="${cat.categoryId}"
                                            ${productCreate.categoryId.categoryId==cat.categoryId ? 'selected' : '' }>
                                            ${cat.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group stationery-category"
                                style="display: ${productCreate.type == 'Stationery' ? 'block' : 'none'};">
                                <label>Category (Stationery): *</label>
                                <select name="stationeryCategoryId" class="form-control">
                                    <option value="">-- Select Category --</option>
                                    <c:forEach var="cat" items="${stationeryCategories}">
                                        <option value="${cat.categoryId}"
                                            ${productCreate.categoryId.categoryId==cat.categoryId ? 'selected' : '' }>
                                            ${cat.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="book-fields"
                                style="display: ${productCreate == null || productCreate.type != 'Stationery' ? 'block' : 'none'}; background:#f9f9f9; padding:15px; margin-bottom:15px; border-radius:5px;">
                                <h4 style="margin-top:0;">Book Details</h4>
                                <div style="display:flex; gap:15px;">
                                    <div class="form-group" style="flex:1;">
                                        <label>ISBN: *</label>
                                        <input type="text" name="isbn" class="form-control"
                                            value="${productCreate.isbn}">
                                    </div>
                                    <div class="form-group" style="flex:1;">
                                        <label>Published Year:</label>
                                        <input type="number" name="publishedYear" min="0" max="2026"
                                            class="form-control" value="${productCreate.publishedYear}">
                                    </div>
                                </div>
                                <div style="display:flex; gap:15px;">
                                    <div class="form-group" style="flex:1;">
                                        <label>Publisher:</label>
                                        <input type="text" name="publisher" class="form-control"
                                            value="${productCreate.publisher}">
                                    </div>
                                    <div class="form-group" style="flex:1;">
                                        <label>Translator:</label>
                                        <input type="text" name="translator" class="form-control"
                                            value="${productCreate.translator}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>Genre: *</label>
                                    <select name="genreId" class="form-control">
                                        <option value="">-- Select Genre --</option>
                                        <c:forEach var="g" items="${genres}">
                                            <option value="${g.genreId}" ${productCreate.genreId.genreId==g.genreId
                                                ? 'selected' : '' }>${g.genreName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Authors (Multi-select): *</label>
                                    <select name="authorIds" class="form-control" multiple style="height:80px;">
                                        <c:forEach var="a" items="${authors}">
                                            <c:set var="isSelected" value="false" />
                                            <c:if test="${not empty productCreate.authorCollection}">
                                                <c:forEach var="pa" items="${productCreate.authorCollection}">
                                                    <c:if test="${pa.authorId == a.authorId}">
                                                        <c:set var="isSelected" value="true" />
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                            <option value="${a.authorId}" ${isSelected ? 'selected' : '' }>
                                                ${a.authorName}</option>
                                        </c:forEach>
                                    </select>
                                    <small style="color:#666;">Hold Ctrl (Windows) or Cmd (Mac) to select
                                        multiple</small>
                                </div>
                            </div>

                            <div class="stationery-fields"
                                style="display: ${productCreate.type == 'Stationery' ? 'block' : 'none'}; background:#f9f9f9; padding:15px; margin-bottom:15px; border-radius:5px;">
                                <h4 style="margin-top:0;">Stationery Details</h4>
                                <div style="display:flex; gap:15px;">
                                    <div class="form-group" style="flex:1;">
                                        <label>Color:</label>
                                        <input type="text" name="color" class="form-control"
                                            value="${productCreate.color}">
                                    </div>
                                    <div class="form-group" style="flex:1;">
                                        <label>Material:</label>
                                        <input type="text" name="material" class="form-control"
                                            value="${productCreate.material}">
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Description:</label>
                                <textarea name="description" rows="3"
                                    class="form-control">${productCreate.descriptionText}</textarea>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn-cancel"
                                    onclick="document.getElementById('addPopup').style.display = 'none'">Cancel</button>
                                <button type="submit" class="btn-save">Create</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- ================= EDIT PRODUCT POPUP ================= -->
                <c:if test="${not empty productEdit}">
                    <div id="editPopup" class="modal-overlay" style="display: flex;">
                        <div class="modal-content"
                            style="width: 700px; max-width: 90%; max-height: 90vh; overflow-y: auto;">
                            <div class="modal-header">
                                <h3>Edit Product</h3>
                            </div>
                            <c:if test="${not empty editError}">
                                <div class="alert alert-error" style="color:red; margin-bottom:10px;">${editError}</div>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/dashboard/product" method="post"
                                enctype="multipart/form-data">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id" value="${productEdit.productId}">
                                <input type="hidden" name="tab" value="${currentTab}">
                                <input type="hidden" name="productType" value="${productEdit.type}">
                                <input type="hidden" name="oldImageUrl" value="${productEdit.imageUrl}">

                                <div class="form-group">
                                    <label style="font-weight:bold;">Product Type: ${productEdit.type}</label>
                                </div>

                                <div class="form-group">
                                    <label>Name: *</label>
                                    <input type="text" name="name" class="form-control" value="${productEdit.name}"
                                        required>
                                </div>

                                <div style="display:flex; gap:15px;">
                                    <div class="form-group" style="flex:1;">
                                        <label>Price (đ): *</label>
                                        <input type="number" name="price" min="1" step="0.01" class="form-control"
                                            value="${productEdit.price}" required>
                                    </div>
                                    <div class="form-group" style="flex:1;">
                                        <label>Status:</label>
                                        <select name="status" class="form-control">
                                            <option value="1" ${productEdit.status==1 ? 'selected' : '' }>Active
                                            </option>
                                            <option value="0" ${productEdit.status==0 ? 'selected' : '' }>Inactive
                                            </option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Category: *</label>
                                    <select name="categoryId" class="form-control" required>
                                        <option value="">-- Select Category --</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.categoryId}"
                                                ${productEdit.categoryId.categoryId==cat.categoryId ? 'selected' : '' }>
                                                ${cat.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Current Image:</label><br>
                                    <c:if test="${not empty productEdit.imageUrl}">
                                        <img src="${productEdit.imageUrl}"
                                            style="max-height: 100px; margin-bottom: 10px;"><br>
                                    </c:if>
                                    <label>Upload New Image (leaves current if empty):</label>
                                    <input type="file" name="imageFile" accept="image/*" class="form-control"
                                        style="padding: 7px;">
                                </div>

                                <c:if test="${productEdit.type == 'Book'}">
                                    <div
                                        style="background:#f9f9f9; padding:15px; margin-bottom:15px; border-radius:5px;">
                                        <h4 style="margin-top:0;">Book Details</h4>
                                        <div style="display:flex; gap:15px;">
                                            <div class="form-group" style="flex:1;">
                                                <label>ISBN: *</label>
                                                <input type="text" name="isbn" class="form-control"
                                                    value="${productEdit.isbn}">
                                            </div>
                                            <div class="form-group" style="flex:1;">
                                                <label>Published Year:</label>
                                                <input type="number" name="publishedYear" min="0" max="2026"
                                                    class="form-control" value="${productEdit.publishedYear}">
                                            </div>
                                        </div>
                                        <div style="display:flex; gap:15px;">
                                            <div class="form-group" style="flex:1;">
                                                <label>Publisher:</label>
                                                <input type="text" name="publisher" class="form-control"
                                                    value="${productEdit.publisher}">
                                            </div>
                                            <div class="form-group" style="flex:1;">
                                                <label>Translator:</label>
                                                <input type="text" name="translator" class="form-control"
                                                    value="${productEdit.translator}">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label>Genre: *</label>
                                            <select name="genreId" class="form-control">
                                                <option value="">-- Select Genre --</option>
                                                <c:forEach var="g" items="${genres}">
                                                    <option value="${g.genreId}"
                                                        ${productEdit.genreId.genreId==g.genreId ? 'selected' : '' }>
                                                        ${g.genreName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Authors (Multi-select): *</label>
                                            <select name="authorIds" class="form-control" multiple style="height:80px;">
                                                <c:forEach var="a" items="${authors}">
                                                    <c:set var="isSelected" value="false" />
                                                    <c:if test="${not empty productEdit.authorCollection}">
                                                        <c:forEach var="pa" items="${productEdit.authorCollection}">
                                                            <c:if test="${pa.authorId == a.authorId}">
                                                                <c:set var="isSelected" value="true" />
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:if>
                                                    <option value="${a.authorId}" ${isSelected ? 'selected' : '' }>
                                                        ${a.authorName}</option>
                                                </c:forEach>
                                            </select>
                                            <small style="color:#666;">Hold Ctrl (Windows) or Cmd (Mac) to select
                                                multiple</small>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${productEdit.type == 'Stationery'}">
                                    <div
                                        style="background:#f9f9f9; padding:15px; margin-bottom:15px; border-radius:5px;">
                                        <h4 style="margin-top:0;">Stationery Details</h4>
                                        <div style="display:flex; gap:15px;">
                                            <div class="form-group" style="flex:1;">
                                                <label>Color:</label>
                                                <input type="text" name="color" class="form-control"
                                                    value="${productEdit.color}">
                                            </div>
                                            <div class="form-group" style="flex:1;">
                                                <label>Material:</label>
                                                <input type="text" name="material" class="form-control"
                                                    value="${productEdit.material}">
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="form-group">
                                    <label>Description:</label>
                                    <textarea name="description" rows="3"
                                        class="form-control">${productEdit.descriptionText}</textarea>
                                </div>

                                <div class="modal-footer">
                                    <a href="${pageContext.request.contextPath}/dashboard/product?tab=${currentTab}"
                                        class="btn-cancel" style="text-decoration:none;">Cancel</a>
                                    <button type="submit" class="btn-save">Save Changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:if>