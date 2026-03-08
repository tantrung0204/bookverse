<%-- 
    Document   : product-list.jsp
    Created on : 24 Feb 2026, 16:55:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-list.css">
<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Products</p>
        <p class="subtitle">Create and manage books and stationery for your library</p>
    </div>

    <div class="content-card">
        <div class="tab-container">
            <a href="${pageContext.request.contextPath}/product?tab=book" 
               class="tab-item ${currentTab == 'book' ? 'active' : ''}">
                <i class="bi bi-book me-1"></i> Books
            </a>
            <a href="${pageContext.request.contextPath}/product?tab=stationery" 
               class="tab-item ${currentTab == 'stationery' ? 'active' : ''}">
                <i class="bi bi-pencil-square me-1"></i> Stationery
            </a>
        </div>

        <div class="toolbar">
            <button type="button" class="btn-add" onclick="openCreateProductPopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Product
            </button>

            <form action="${pageContext.request.contextPath}/product" method="GET">
                <input type="hidden" name="tab" value="${currentTab}">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search products..." value="${currentKeyword}">
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
                                <td><fmt:formatNumber value="${product.price}" pattern="#,###"/> đ</td>
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
                                        <a href="${pageContext.request.contextPath}/product?action=detail&id=${product.productId}&tab=${currentTab}&keyword=${currentKeyword}&page=${currentPage}" 
                                           class="btn-action btn-detail" title="View Detail" 
                                           style="display: inline-flex; align-items: center; justify-content: center; text-decoration: none;">
                                            <i class="bi bi-eye"></i>
                                        </a>


                                        <a href="${pageContext.request.contextPath}/product?action=edit&id=${product.productId}&tab=${currentTab}&keyword=${currentKeyword}&page=${currentPage}" 
                                           class="btn-action btn-edit" title="Edit" 
                                           style="display: inline-flex; align-items: center; justify-content: center; text-decoration: none;">
                                            <i class="bi bi-pencil"></i>
                                        </a>

                                        <form action="${pageContext.request.contextPath}/product" method="post" style="display:inline;" 
                                              onsubmit="return confirm('Are you sure you want to delete product:\n${product.name} (ID: ${product.productId})')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${product.productId}">
                                            <input type="hidden" name="tab" value="${currentTab}">
                                            <button type="submit" class="btn-action btn-delete" title="Delete">
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
                <span style="color: #888; font-size: 14px;">Page:</span>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="page-link active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link" href="${pageContext.request.contextPath}/product?tab=${currentTab}&keyword=${currentKeyword}&page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
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
                            <img src="${productDetail.imageUrl}" style="width: 100%; border-radius: 5px; border: 1px solid #ccc;"
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';">
                        </c:when>
                    </c:choose>
                </div>

                <div style="flex: 1;">
                    <table class="detail-table" style="width: 100%;">
                        <tr><th width="30%">ID:</th><td>${productDetail.productId}</td></tr>
                        <tr><th>Name:</th><td style="font-weight: bold;">${productDetail.name}</td></tr>
                        <tr><th>Type:</th><td>${productDetail.type}</td></tr>
                        <tr><th>Category:</th><td>${productDetail.categoryId != null ? productDetail.categoryId.categoryName : 'N/A'}</td></tr>
                        <tr><th>Price:</th><td style="color: #d9534f; font-weight: bold;"><fmt:formatNumber value="${productDetail.price}" pattern="#,###"/> đ</td></tr>
                        <tr><th>Quantity:</th><td>${productDetail.stockQuantity}</td></tr>
                        <tr><th>Rating:</th><td><fmt:formatNumber value="${productDetail.averageRating}" maxFractionDigits="1"/> <i style="color: #ffc107;" class="bi bi-star-fill"></i> (${productDetail.reviewCount} reviews)</td></tr>
                        <tr><th>Sold:</th><td>${productDetail.soldQuantity} items</td></tr>
                        <tr>
                            <th>Status:</th>
                            <td>
                                <c:choose>
                                    <c:when test="${productDetail.status == 1}"><span class="badge-status badge-active">Active</span></c:when>
                                    <c:otherwise><span class="badge-status badge-inactive">Inactive</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </table>

                    <c:if test="${productDetail.type == 'Stationery'}">
                        <table class="detail-table" style="width: 100%; margin-top: 5px;">
                            <tr><th width="30%">Color:</th><td>${not empty productDetail.color ? productDetail.color : 'N/A'}</td></tr>
                            <tr><th>Material:</th><td>${not empty productDetail.material ? productDetail.material : 'N/A'}</td></tr>
                        </table>
                    </c:if>

                    <c:if test="${productDetail.type == 'Book'}">
                        <table class="detail-table" style="width: 100%; margin-top: 5px;">
                            <tr><th width="30%">Genre:</th><td>${productDetail.genreId != null ? productDetail.genreId.genreName : 'N/A'}</td></tr>
                            <tr>
                                <th>Author(s):</th>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty productDetail.authorCollection}">
                                            <c:forEach var="author" items="${productDetail.authorCollection}" varStatus="st">
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

            <div style="text-align: left; margin-top: 10px; border-top: 1px solid #eee; padding-top: 10px;">
                <strong>Description:</strong>
                <p style="font-size: 14px; color: #555; margin-top: 5px;">${not empty productDetail.descriptionText ? productDetail.descriptionText : 'No description available.'}</p>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="document.getElementById('detailPopup').style.display = 'none';">Close</button>
            </div>
        </div>
    </div>
</c:if>
<!-- ================= CREATE POPUP ================= -->
<div id="createPopup" class="modal-overlay" style="display: none;">
    <div class="modal-content" style="width: 700px; max-width: 90%; max-height: 90vh; overflow-y: auto;">
        <div class="modal-header">
            <h3>Add New Product</h3>
        </div>

        <c:if test="${not empty createError}">
            <div class="alert alert-danger" style="margin-bottom: 15px; color: red; background: #fdd; padding: 10px; border-radius: 5px;">
                ${createError}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/product" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="tab" value="${currentTab}">

            <div class="form-group mb-3" style="background: #f9f9f9; padding: 10px; border-radius: 5px;">
                <label style="font-weight: bold; margin-right: 15px;">Product Type <span style="color:red;">*</span></label>

                <input type="radio" name="productType" id="typeBook" value="Book" 
                       ${empty productCreate or productCreate.type == 'Book' ? 'checked' : ''} 
                       onchange="toggleCreateFields()"> <label for="typeBook" style="margin-right: 15px;">Book</label>

                <input type="radio" name="productType" id="typeStat" value="Stationery" 
                       ${productCreate.type == 'Stationery' ? 'checked' : ''} 
                       onchange="toggleCreateFields()"> <label for="typeStat">Stationery</label>
            </div>

            <div style="display: flex; gap: 20px;">
                <div style="flex: 1;">
                    <div class="form-group mb-3">
                        <label style="font-weight: bold; display: block;">Name <span style="color:red;">*</span></label>
                        <input type="text" name="name" class="form-control" value="${productCreate.name}" required style="width: 100%;">
                    </div>

                    <div class="form-group mb-3">
                        <label style="font-weight: bold; display: block;">Price (VND) <span style="color:red;">*</span></label>
                        <input type="number" name="price" class="form-control" value="${productCreate.price}" min="1" step="0.01" required style="width: 100%;">
                    </div>

                    <div class="form-group mb-3">
                        <label style="font-weight: bold; display: block;">Image Upload</label>
                        <input type="file" name="imageFile" class="form-control" accept="image/*" style="width: 100%;">
                    </div>
                </div>

                <div style="flex: 1;">
                    <div class="form-group mb-3">
                        <label style="font-weight: bold; display: block;">Status</label>
                        <select name="status" class="form-control" style="width: 100%;">
                            <option value="1" ${productCreate.status == 1 ? 'selected' : ''}>Active</option>
                            <option value="0" ${productCreate.status == 0 ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                    <div class="form-group mb-3" id="bookCategoryGroup">
                        <label style="font-weight: bold; display: block;">Book Category <span style="color:red;">*</span></label>
                        <select name="bookCategoryId" class="form-control" style="width: 100%;">
                            <option value="">-- Select Book Category --</option>
                            <c:forEach var="c" items="${bookCategories}">
                                <option value="${c.categoryId}" ${(not empty productCreate.categoryId) and (productCreate.categoryId.categoryId == c.categoryId) ? 'selected' : ''}>${c.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group mb-3" id="statCategoryGroup" style="display: none;">
                        <label style="font-weight: bold; display: block;">Stationery Category <span style="color:red;">*</span></label>
                        <select name="stationeryCategoryId" class="form-control" style="width: 100%;">
                            <option value="">-- Select Stationery Category --</option>
                            <c:forEach var="c" items="${stationeryCategories}">
                                <option value="${c.categoryId}" ${(not empty productCreate.categoryId) and (productCreate.categoryId.categoryId == c.categoryId) ? 'selected' : ''}>${c.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div id="createBookExtra">
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Genre <span style="color:red;">*</span></label>
                            <select name="genreId" class="form-control" style="width: 100%;">
                                <option value="">-- Select Genre --</option>
                                <c:forEach var="g" items="${genres}">
                                    <option value="${g.genreId}" ${(not empty productCreate.genreId) and (productCreate.genreId.genreId == g.genreId) ? 'selected' : ''}>${g.genreName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Authors <span style="color:red;">*</span></label>
                            <select name="authorIds" class="form-control" multiple size="3" style="width: 100%;">
                                <c:forEach var="a" items="${authors}">
                                    <c:set var="isSel" value="false" />
                                    <c:forEach var="pa" items="${productCreate.authorCollection}">
                                        <c:if test="${pa.authorId == a.authorId}"><c:set var="isSel" value="true" /></c:if>
                                    </c:forEach>
                                    <option value="${a.authorId}" ${isSel ? 'selected' : ''}>${a.authorName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">ISBN</label>
                            <input type="text" name="isbn" class="form-control" value="${productCreate.isbn}" style="width: 100%;">
                        </div>
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Publisher</label>
                            <input type="text" name="publisher" class="form-control" value="${productCreate.publisher}" style="width: 100%;">
                        </div>
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Translator</label>
                            <input type="text" name="translator" class="form-control" value="${productCreate.translator}" style="width: 100%;">
                        </div>
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Published Year</label>
                            <input type="number" name="publishedYear" class="form-control" value="${productCreate.publishedYear}" style="width: 100%;">
                        </div>
                    </div>

                    <div id="createStatExtra" style="display: none;">
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Color</label>
                            <input type="text" name="color" class="form-control" value="${productCreate.color}" style="width: 100%;">
                        </div>
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Material</label>
                            <input type="text" name="material" class="form-control" value="${productCreate.material}" style="width: 100%;">
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group mb-3">
                <label style="font-weight: bold; display: block;">Description</label>
                <textarea name="description" class="form-control" rows="3" style="width: 100%; resize: vertical;">${productCreate.descriptionText}</textarea>
            </div>

            <div class="modal-footer" style="text-align: right; border-top: 1px solid #eee; padding-top: 15px;">
                <button type="button" class="btn-cancel" onclick="document.getElementById('createPopup').style.display = 'none';">Cancel</button>
                <button type="submit" class="btn-save">Add Product</button>
            </div>
        </form>
    </div>
</div>
<!-- ================= EDIT POPUP ================= -->
<c:if test="${not empty productEdit}">
    <div id="editPopup" class="modal-overlay" style="display: flex;">
        <div class="modal-content" style="width: 700px; max-width: 90%; max-height: 90vh; overflow-y: auto;">
            <div class="modal-header">
                <h3>Edit Product (#${productEdit.productId})</h3>
            </div>

            <c:if test="${not empty editError}">
                <div class="alert alert-danger" style="margin-bottom: 15px; color: red; background: #fdd; padding: 10px; border-radius: 5px;">
                    ${editError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/product" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${productEdit.productId}">
                <input type="hidden" name="tab" value="${currentTab}">
                <input type="hidden" name="productType" value="${productEdit.type}">
                <input type="hidden" name="oldImageUrl" value="${productEdit.imageUrl}">

                <div style="display: flex; gap: 20px;">
                    <div style="flex: 1;">
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Name <span style="color:red;">*</span></label>
                            <input type="text" name="name" class="form-control" value="${productEdit.name}" required style="width: 100%;">
                        </div>

                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Price (VND) <span style="color:red;">*</span></label>
                            <input type="number" name="price" class="form-control" value="${productEdit.price}" min="1" step="0.01" required style="width: 100%;">
                        </div>

                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Category <span style="color:red;">*</span></label>
                            <select name="categoryId" class="form-control" required style="width: 100%;">
                                <option value="">-- Select Category --</option>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}" ${productEdit.categoryId.categoryId == c.categoryId ? 'selected' : ''}>
                                        ${c.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Product Image</label>
                            <input type="file" name="imageFile" class="form-control" accept="image/png, image/jpeg, image/webp" style="width: 100%;">
                        </div>
                    </div>

                    <div style="flex: 1;">
                        <div class="form-group mb-3">
                            <label style="font-weight: bold; display: block;">Status <span style="color:red;">*</span></label>
                            <select name="status" class="form-control" style="width: 100%;">
                                <option value="1" ${productEdit.status == 1 ? 'selected' : ''}>Active</option>
                                <option value="0" ${productEdit.status == 0 ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>

                        <c:if test="${productEdit.type == 'Book'}">
                            <div class="form-group mb-3">
                                <label style="font-weight: bold; display: block;">Genre</label>
                                <select name="genreId" class="form-control" style="width: 100%;">
                                    <option value="">-- Select Genre --</option>
                                    <c:forEach var="g" items="${genres}">
                                        <option value="${g.genreId}" ${productEdit.genreId.genreId == g.genreId ? 'selected' : ''}>
                                            ${g.genreName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group mb-3">
                                <label style="font-weight: bold; display: block;">Authors (Hold Ctrl to select multiple)</label>
                                <select name="authorIds" class="form-control" multiple size="4" style="width: 100%;">
                                    <c:forEach var="a" items="${authors}">
                                        <c:set var="isSelected" value="false" />
                                        <c:forEach var="pa" items="${productEdit.authorCollection}">
                                            <c:if test="${pa.authorId == a.authorId}">
                                                <c:set var="isSelected" value="true" />
                                            </c:if>
                                        </c:forEach>
                                        <option value="${a.authorId}" ${isSelected ? 'selected' : ''}>${a.authorName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>

                        <c:if test="${productEdit.type == 'Stationery'}">
                            <div class="form-group mb-3">
                                <label style="font-weight: bold; display: block;">Color</label>
                                <input type="text" name="color" class="form-control" value="${productEdit.color}" style="width: 100%;">
                            </div>
                            <div class="form-group mb-3">
                                <label style="font-weight: bold; display: block;">Material</label>
                                <input type="text" name="material" class="form-control" value="${productEdit.material}" style="width: 100%;">
                            </div>
                        </c:if>
                    </div>
                </div>

                <div class="form-group mb-3">
                    <label style="font-weight: bold; display: block;">Description</label>
                    <textarea name="description" class="form-control" rows="4" style="width: 100%; resize: vertical;">${productEdit.descriptionText}</textarea>
                </div>

                <div class="modal-footer" style="text-align: right; border-top: 1px solid #eee; padding-top: 15px;">
                    <button type="button" class="btn-cancel" onclick="document.getElementById('editPopup').style.display = 'none';">Cancel</button>
                    <button type="submit" class="btn-save">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</c:if>

<script>
// 1. Hàm bật modal Create
    function openCreateProductPopup() {
        document.getElementById("createPopup").style.display = "flex";
        toggleCreateFields(); // Chạy ngay lần đầu để set đúng layout
    }

    // 2. Hàm Toggle Ẩn Hiện các trường theo Product Type
    function toggleCreateFields() {
        var isBook = document.getElementById("typeBook").checked;

        if (isBook) {
            document.getElementById("bookCategoryGroup").style.display = "block";
            document.getElementById("statCategoryGroup").style.display = "none";
            document.getElementById("createBookExtra").style.display = "block";
            document.getElementById("createStatExtra").style.display = "none";
        } else {
            document.getElementById("bookCategoryGroup").style.display = "none";
            document.getElementById("statCategoryGroup").style.display = "block";
            document.getElementById("createBookExtra").style.display = "none";
            document.getElementById("createStatExtra").style.display = "block";
        }
    }

    // 3. Tự động bật lại Modal nếu Controller báo có lỗi Validate
    <c:if test="${openCreateModal == true}">
    window.onload = function () {
        setTimeout(function () {
            openCreateProductPopup();
        }, 100);
    };
    </c:if>

// Đóng Popup khi click ra ngoài vùng xám (overlay)
    window.onclick = function (event) {
        var detailModal = document.getElementById("detailPopup");
        if (detailModal && event.target == detailModal) {
            detailModal.style.display = "none";
        }
    }
</script>