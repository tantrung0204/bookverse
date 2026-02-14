<%-- 
    Document   : category_list
    Created on : 10 Feb 2026, 18:33:04
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Category List</title>
    </head>

    <body>

        <h2>Category List</h2>
        <a class="text-decoration-none btn btn-primary" href="${pageContext.request.contextPath}/category?view=create">Thêm thể loại
        </a>
        <form method="get" action="category">
            <input type="text"
                   name="keyword"
                   placeholder="Search category..."
                   value="${keyword}">
            <button type="submit">Tìm kiếm</button>
        </form>

        <c:choose>        
            <c:when test="${not empty category_list}">
                <table >
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Category Name</th>
                            <th>Description</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${category_list}">
                            <tr>
                                <td>${c.categoryId}</td>
                                <td>${c.categoryName}</td>
                                <td>${c.descriptionText}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${c.status == 1}">
                                            <span class="badge bg-success">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="actions" > 
                                    <div style="display: flex; justify-content: center">
                                        <a href="${pageContext.request.contextPath}/category?view=view_detail&id=${c.categoryId}" > Detail
                                            <i class="fa-solid fa-eye"></i>
                                        </a>
                                    </div>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/category?view=add&id=${c.categoryId}" >  
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                </td>
                                <td>
                                    <button type="button"
                                            onclick="openEditPopupByUser(
                                                            '${c.categoryId}',
                                                            '${c.categoryName}',
                                                            '${c.descriptionText}',
                                                            '${c.status}'
                                                            )">
                                        Edit
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p style="color:gray;">Empty List</p>
            </c:otherwise>
        </c:choose>


        <div id="editPopup"
             style="display:none;
             position:fixed;
             top:0;
             left:0;
             width:100%; height:100%;
             background:rgba(0,0,0,0.5);">

            <div style="background:#fff;
                 width:400px;
                 margin:100px auto;
                 padding:20px;">

                <h3>Edit Category</h3>

                <c:if test="${not empty editError}">
                    <p id="editErrorMsg" style="color:red; font-weight:bold;">
                        ${editError}
                    </p>
                </c:if>

                <form action="${pageContext.request.contextPath}/category?action=edit"
                      method="post"  >

                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="categoryId" id="editCategoryId">

                    <table>
                        <tr>
                            <td>Category Name</td>
                            <td>
                                <input type="text"
                                       name="categoryName"
                                       id="editCategoryName"
                                       required>
                            </td>
                        </tr>

                        <tr>
                            <td>Description</td>
                            <td>
                                <textarea name="descriptionText"
                                          id="editDescription"></textarea>
                            </td>
                        </tr>

                        <tr>
                            <td>Status</td>
                            <td>
                                <select name="status" id="editStatus">
                                    <option value="1">Active</option>
                                    <option value="0">Inactive</option>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2" style="text-align:right">
                                <button type="submit">Edit</button>
                                <button type="button" onclick="closeEditPopup()">Cancel</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <script>
            function openEditPopup(id, name, description, status) {

                document.getElementById("editCategoryId").value = id;
                document.getElementById("editCategoryName").value = name;
                document.getElementById("editDescription").value = description;
                document.getElementById("editStatus").value = status;
                document.getElementById("editPopup").style.display = "block";
            }

            function openEditPopupByUser(id, name, description, status) {
                const err = document.getElementById("editErrorMsg");
                if (err) {
                    err.innerText = "";
                    err.style.display = "none";
                }
                openEditPopup(id, name, description, status);
            }

            function closeEditPopup() {
                document.getElementById("editPopup").style.display = "none";
            }
        </script>

        <!-- auto reopen popup when update error -->
        <c:if test="${openEditPopup}">
            <script>
                window.onload = function () {
                    openEditPopup(
                            '${editId}',
                            '${editName}',
                            '${editDesc}',
                            '${editStatus}'
                            );
                };
            </script>
        </c:if>

    </body>
</html>
