<%-- 
    Document   : product-list.jsp
    Created on : 24 Feb 2026, 16:55:18
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-list.css">

<div class="container-fluid">

    <div class="page-header">
        <p class="title">Manage Products</p>
        <p class="subtitle">Create and manage product for your library</p>
    </div>


    <div class="content-card">

        <div class="toolbar">
            <div class="filter-buttons">
                <a href="${pageContext.request.contextPath}/product?action=list&type=book"
                   class="btn-filter ${type == null || type == 'book' ? 'active' : ''}">
                    📖 Books
                </a>
                <a href="${pageContext.request.contextPath}/product?action=list&type=stationery"
                   class="btn-filter ${type == 'stationery' ? 'active' : ''}">
                    ✏️ Stationery
                </a>
            </div>

            <button class="btn btn-primary"
                    onclick="openCreateModal('${type == null ? "book" : type}')">
                ➕ Add Product
            </button>

            <form action="${pageContext.request.contextPath}/product" method="get">
                <input type="hidden" name="action" value="search" />
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search categories..." value="${keyword}">
                </div>
                <button type="submit" hidden></button>
            </form>
        </div>

        <c:choose>
            <c:when test="${not empty products}">
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
                            <th width="10%">Stock</th>
                            <th width="15%">Type</th>
                            <th width="15%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td><strong>${p.productId}</strong></td>
                                <td>${p.name}</td>
                                <td>${p.price}</td>
                                <td>${p.stockQuantity}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.type == 'Book'}">
                                            <span class="badge-status badge-book">Book</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge-status badge-stationery">Stationery</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <div class="action-buttons">
                                        <button type="button" class="btn-action btn-detail"
                                                title="View Detail"  onclick="openDetailPopup('${p.productId}')">
                                            <i class="bi bi-eye"></i>
                                        </button>

                                        <button type="button" class="btn-action btn-edit" title="Edit"
                                                onclick="openEditPopup(
                                                                '${c.categoryId}',
                                                                '${c.categoryName}',
                                                                '${c.descriptionText}',
                                                                '${c.status}'
                                                                )">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/category"
                                              method="post" style="display:inline;"
                                              onsubmit="return confirmDelete('${c.categoryId}', '${c.categoryName}')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${c.categoryId}">
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
                    <p class="mt-2">No categories found.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty deleteError}">
            <div class="alert alert-danger mt-3 ">${deleteError}</div>
            <c:remove var="deleteError" scope="session" />
        </c:if>
    </div>
</div>

<div id="createModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>

        <h3 id="modalTitle"></h3>

        <!-- FORM BOOK -->
        <form id="bookForm" method="post"
              action="${pageContext.request.contextPath}/product">
            <input type="hidden" name="action" value="createBook"/>

            <label>Book Name</label>
            <input type="text" name="name" required/>

            <label>Price</label>
            <input type="number" name="price" required/>

            <label>Stock</label>
            <input type="number" name="stock"/>

            <label>ISBN</label>
            <input type="text" name="isbn"/>

            <label>Publisher</label>
            <input type="text" name="publisher"/>

            <label>Published Year</label>
            <input type="number" name="publishedYear"/>

            <button type="submit">Save Book</button>
        </form>

        <!-- FORM STATIONERY -->
        <form id="stationeryForm" method="post"
              action="${pageContext.request.contextPath}/product">
            <input type="hidden" name="action" value="createStationery"/>

            <label>Stationery Name</label>
            <input type="text" name="name" required/>

            <label>Price</label>
            <input type="number" name="price" required/>

            <label>Stock</label>
            <input type="number" name="stock"/>

            <label>Color</label>
            <input type="text" name="color"/>

            <label>Material</label>
            <input type="text" name="material"/>

            <button type="submit">Save Stationery</button>
        </form>
    </div>
</div>

<!-- PRODUCT DETAIL MODAL -->
<div id="detailModal" class="modal">
    <div class="modal-content modal-lg">
        <span class="close" onclick="closeDetailModal()">&times;</span>

        <h3 id="detailName"></h3>

        <div class="detail-wrapper">
            <!-- IMAGE -->
            <div class="detail-image">
                <img id="detailImage" />
            </div>

            <!-- INFO -->
            <div class="detail-info">
                <p><b>Type:</b> <span id="detailType"></span></p>
                <p><b>Category:</b> <span id="detailCategory"></span></p>
                <p><b>Price:</b> <span id="detailPrice"></span></p>
                <p><b>Stock:</b> <span id="detailStock"></span></p>
                <p><b>Rating Avg:</b> ⭐ <span id="detailRating"></span></p>
                <p><b>Sold:</b> <span id="detailSold"></span></p>

                <!-- BOOK -->
                <div id="bookDetail" style="display:none;">
                    <hr>
                    <p><b>Authors:</b> <span id="detailAuthors"></span></p>
                    <p><b>Genre:</b> <span id="detailGenre"></span></p>
                    <p><b>Publisher:</b> <span id="detailPublisher"></span></p>
                    <p><b>Published Year:</b> <span id="detailYear"></span></p>
                    <p><b>ISBN:</b> <span id="detailISBN"></span></p>
                    <p><b>Translator:</b> <span id="detailTranslator"></span></p>
                    <p><b>Description:</b></p>
                    <p id="detailDescription"></p>
                </div>

                <!-- STATIONERY -->
                <div id="stationeryDetail" style="display:none;">
                    <hr>
                    <p><b>Color:</b> <span id="detailColor"></span></p>
                    <p><b>Material:</b> <span id="detailMaterial"></span></p>
                </div>
            </div>
        </div>
    </div>
</div> 

<script>
    function openCreateModal(type) {
        document.getElementById("createModal").style.display = "block";

        document.getElementById("bookForm").style.display = "none";
        document.getElementById("stationeryForm").style.display = "none";

        if (type === "stationery") {
            document.getElementById("modalTitle").innerText = "Add Stationery";
            document.getElementById("stationeryForm").style.display = "block";
        } else {
            document.getElementById("modalTitle").innerText = "Add Book";
            document.getElementById("bookForm").style.display = "block";
        }
    }

    function closeModal() {
        document.getElementById("createModal").style.display = "none";
    }
</script>

<script>
function openDetailPopup(productId) {
    fetch('${pageContext.request.contextPath}/product?action=detail&productId=' + productId)
        .then(res => res.json())
        .then(p => {
            if (!p || !p.productId) {
                alert("Không tìm thấy sản phẩm");
                return;
            }

            document.getElementById("detailModal").style.display = "block";

            detailName.innerText = p.name;
            detailImage.src = p.imageUrl && p.imageUrl !== ""
                ? p.imageUrl
                : "${pageContext.request.contextPath}/images/no-image.png";

            detailType.innerText = p.type;
            detailCategory.innerText = p.category;
            detailPrice.innerText = p.price + " ₫";
            detailStock.innerText = p.stockQuantity;
            detailRating.innerText = p.averageRating;
            detailSold.innerText = p.soldQuantity;

            bookDetail.style.display = "none";
            stationeryDetail.style.display = "none";

            if (p.type === "Book") {
                bookDetail.style.display = "block";
                detailAuthors.innerText = p.authors ? p.authors.join(", ") : "Updating";
                detailGenre.innerText = p.genre;
                detailPublisher.innerText = p.publisher;
                detailYear.innerText = p.publishedYear;
                detailISBN.innerText = p.isbn;
            }

            if (p.type === "Stationery") {
                stationeryDetail.style.display = "block";
                detailColor.innerText = p.color;
                detailMaterial.innerText = p.material;
            }
        })
        .catch(err => {
            console.error(err);
            alert("Lỗi khi load chi tiết sản phẩm");
        });
}
</script>
<script>
    function closeDetailModal() {
        document.getElementById("detailModal").style.display = "none";
    }
</script>