<%-- Document : import-list Created on : Mar 3, 2026, 2:24:50 PM Author : LECOO --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/category-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/voucher-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/product-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/inventory.css">

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Inventory</p>
        <p class="subtitle">Create and manage inventory for your library</p>
    </div>
    <div class="content-card">
        <div class="tab-container">
            <a href="${pageContext.request.contextPath}/dashboard/inventory"
               class="tab-item ${currentTab == 'import-list' ? 'active' : ''}">
                <i class="bi bi-box-arrow-in-down"></i> Imports
            </a>
            <a href="${pageContext.request.contextPath}/dashboard/inventory?view=export-list"
               class="tab-item ${currentTab == 'export-list' ? 'active' : ''}">
                <i class="bi bi-box-arrow-up"></i> Exports
            </a>
        </div>

        <div class="toolbar">
            <%-- Add import --%>
            <button type="button" class="btn-add" onclick="openCreatePopup()">
                <i class="bi bi-plus-lg me-1"></i> Add New Import
            </button>
            <%-- Filter by date range --%>
            <form action="inventory" method="get" class="date-filter-form">
                <input type="hidden" name="view" value="import-list">

                <div class="date-filter">
                    <span>From</span>
                    <input type="date" name="fromDate" value="${fromDate}">
                </div>

                <div class="date-filter">
                    <span>To</span>
                    <input type="date" name="toDate" value="${toDate}">
                </div>

                <button type="submit" class="btn-filter p-2"
                        style="margin-top:18px; background-color:#a68a6d; border-radius: 5px; border: none; color: white;">
                    <i class="bi bi-funnel"></i> Filter
                </button>
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
            <%-- Import List --%>
            <c:when test="${not empty imports}">
                <table class="custom-table">
                    <tr>
                        <th width="10%">ID</th>

                        <th width="15%">Supplier</th>

                        <th width="15%">Staff</th>

                        <th width="15%">Total cost</th>

                        <th width="20%">Creation date</th>

                        <th width="10%">Action</th>

                    </tr>
                    <c:forEach var="i" items="${imports}">
                        <tr>
                            <td><Strong>${i.importId}</strong></td>
                            <td>${i.supplierId.supplierName}</td>
                            <td>${i.staffId.fullName}</td>
                            <td>${i.totalCost}</td>
                            <td>${i.createdAt}</td>
                            <td>
                                <%-- Detail Import --%>
                                <div class="action-buttons">
                                    <button type="button" class="btn-action btn-detail"
                                            onclick="openDetailPopup(${i.importId}, 1)" title="Detail">
                                        <i class="bi bi-eye"></i>

                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>

        </c:choose>
        <%-- pagination --%>
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
        <nav class="d-flex justify-content-center">
            <ul class="pagination">
                <%-- Previous button --%>
                <c:if test="${currentPage > 1}">
                    <a class="page-btn"
                       href="inventory?page=${currentPage - 1}&fromDate=${fromDate}&toDate=${toDate}">&laquo;</a>
                </c:if>
                <%-- If the total <=5, display all pages. --%>
                <c:if test="${totalPages <= maxNote}">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a class="page-btn ${currentPage == i ? 'active' : ''}"
                           href="inventory?page=${i}&fromDate=${fromDate}&toDate=${toDate}">${i}</a>
                    </c:forEach>
                </c:if>
                <%-- If the total> 5 --%>
                <c:if test="${totalPages > maxNote}">
                    <%-- Page 1 always appears --%>
                    <a class="page-btn ${currentPage == 1 ? 'active' : ''}"
                       href="inventory?page=1&fromDate=${fromDate}&toDate=${toDate}">1</a>
                    <%-- The ... mark at the beginning --%>
                    <c:if test="${startPage > 2}">
                        <span class="page-btn disabled">...</span>
                    </c:if>
                    <%-- Middle page --%>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a class="page-btn ${currentPage == i ? 'active' : ''}"
                           href="inventory?page=${i}&fromDate=${fromDate}&toDate=${toDate}">${i}</a>
                    </c:forEach>
                    <%-- The final ellipsis --%>
                    <c:if test="${endPage < totalPages - 1}">
                        <span class="page-btn disabled">...</span>
                    </c:if>
                    <%-- The last page always appears --%>
                    <a class="page-btn ${currentPage == totalPages ? 'active' : ''}"
                       href="inventory?page=${totalPages}&fromDate=${fromDate}&toDate=${toDate}">
                        ${totalPages}
                    </a>
                </c:if>
                <%-- Next button --%>
                <c:if test="${currentPage < totalPages}">
                    <a class="page-btn"
                       href="inventory?page=${currentPage + 1}&fromDate=${fromDate}&toDate=${toDate}">&raquo;</a>
                </c:if>
            </ul>
        </nav>
    </div>
    <form action="inventory" method="post"></form>
    <script>
        let globalProducts = [];

        function openCreatePopup() {
            fetch("inventory?view=addProductList")
                    .then(response => response.json())
                    .then(data => {
                        globalProducts = data.products;

                        let html = `
                <div style="display: flex; gap: 20px; margin-bottom: 15px;">
                    <div style="flex: 1;">
                        <label style="display: block; margin-bottom: 5px;">Supplier <span style="color:red">*</span></label>
                        <select name="supplierId" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                            <option value="">-- Select Supplier --</option>
            `;

                        data.suppliers.forEach(item => {
                            html += `<option value="` + item.supplierId + `">` + item.supplierName + `</option>`;
                        });

                        html += `
                        </select>
                    </div>
                    <div style="flex: 1;">
                        <label style="display: block; margin-bottom: 5px;">Import Date <span style="color:red">*</span></label>
                        <input type="date" name="importDate" id="importDate" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                </div>
                            
                <div style="margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
                    <h4 style="margin: 0; font-size: 16px;">Stock Items</h4>
                    <button type="button" class="btn-add" onclick="addStockItemRow()" style="padding: 5px 10px; font-size: 14px;">
                        <i class="bi bi-plus-lg me-1"></i> Add Item
                    </button>
                </div>
                            
                <table class="detail-table" id="stockItemsTable" style="width: 100%; border-collapse: separate; border-spacing: 0;">
                    <thead>
                        <tr>
                            <th style="width: 30%; padding: 4px 6px; text-align: left;">Product Name</th>
                            <th style="width: 15%; padding: 4px 6px; text-align: left;">Quantity</th>
                            <th style="width: 20%; padding: 4px 6px; text-align: left;">Unit Price</th> 
                            <th style="width: 25%; padding: 4px 6px; text-align: left;">Note</th>
                            <th style="width: 10%; padding: 4px 6px; text-align: center;">Action</th>
                        </tr>
                    </thead>
                    <tbody id="stockItemsBody">
                    </tbody>
                </table>
            `;

                        document.getElementById("createContent").innerHTML = html;

                        // Set max date to today
                        const today = new Date().toISOString().split('T')[0];
                        document.getElementById("importDate").setAttribute('max', today);

                        // Add first row
                        addStockItemRow();

                        document.getElementById("createPopup").style.display = "flex";
                    })
                    .catch(error => {
                        document.getElementById("createContent").innerHTML =
                                "<p style='color:red;text-align:center;'>Failed to load data</p>";
                        document.getElementById("createPopup").style.display = "flex";
                        console.error(error);
                    });
        }

        function addStockItemRow() {
            const tbody = document.getElementById("stockItemsBody");
            const tr = document.createElement("tr");

            let optionsHtml = `<option value="">-- Select Product --</option>`;
            globalProducts.forEach(p => {
                optionsHtml += `<option value="` + p.productId + `">` + p.productName + `</option>`;
            });

            const uniformStyle = "width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;";

            tr.innerHTML = `
    <td style="padding: 4px 6px;">
        <select name="productIds" class="product-select" required onchange="updateProductOptions(); updateRowNames(this);" style="${uniformStyle}">
            ` + optionsHtml + `
        </select>
    </td>
    <td style="padding: 4px 6px;">
        <input type="number" class="qty-input" min="1" step="1" required style="${uniformStyle}" oninput="if(this.value && this.value < 1) this.value=1;" placeholder="1">
    </td>
    <td style="padding: 4px 6px;">
        <input type="number" class="price-input" min="1" step="0.01" required style="${uniformStyle}" oninput="if(this.value && this.value < 1) this.value=1;" placeholder="1">
    </td>
    <td style="padding: 4px 6px;">
        <input type="text" class="note-input" placeholder="Note" style="${uniformStyle}">
    </td>
    <td style="text-align: center; padding: 4px 6px;">
        <button type="button" onclick="removeStockItemRow(this)" style="background: none; border: none; color: red; cursor: pointer; font-size: 18px;">
            <i class="bi bi-trash"></i>
        </button>
    </td>
`;
            tbody.appendChild(tr);
            updateProductOptions();
        }

        function removeStockItemRow(btn) {
            const tbody = document.getElementById("stockItemsBody");
            if (tbody.children.length <= 1) {
                alert("You must have at least one stock item.");
                return;
            }
            const row = btn.closest("tr");
            row.remove();
            updateProductOptions();
        }

        function updateProductOptions() {
            const selects = document.querySelectorAll(".product-select");
            const selectedValues = Array.from(selects).map(s => s.value).filter(v => v !== "");

            selects.forEach(select => {
                const currentValue = select.value;
                Array.from(select.options).forEach(option => {
                    if (option.value !== "" && option.value !== currentValue && selectedValues.includes(option.value)) {
                        option.disabled = true;
                    } else {
                        option.disabled = false;
                    }
                });
            });
        }

        function updateRowNames(selectElem) {
            const row = selectElem.closest("tr");
            const productId = selectElem.value;
            if (productId) {
                row.querySelector(".qty-input").name = "quantity_" + productId;
                row.querySelector(".price-input").name = "unit_price_" + productId;
                row.querySelector(".note-input").name = "note_" + productId;
            } else {
                row.querySelector(".qty-input").removeAttribute("name");
                row.querySelector(".price-input").removeAttribute("name");
                row.querySelector(".note-input").removeAttribute("name");
            }
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
        <div class="modal-content" style="width: 1000px">
            <div class="modal-header">
                <h3>Add New Import Stock</h3>
            </div>

            <c:if test="${not empty createError}">
                <div class="alert alert-danger" id="createErrorMsg">
                    ${createError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/dashboard/inventory" method="post">
                <input type="hidden" name="action" value="create">

                <div id="createContent" style="max-height: 400px;overflow-y: auto;">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeCreatePopup()">Cancel</button>
                    <button type="submit" class="btn-save">Submit</button>
                </div>
            </form>
        </div>
    </div>

    <!-- ================= DETAIL POPUP ================= -->
    <div id="detailPopup" class="modal-overlay">
        <div class="modal-content" style="width: 700px">
            <div class="modal-header">
                <h3>Import Detail</h3>
            </div>
            <div id="popupContent" style="max-height: 400px; overflow-y: auto;"></div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeDetailPopup()">Close</button>
            </div>
        </div>

    </div>
</div>