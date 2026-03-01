<%-- 
    Document   : genreDetail-view
    Created on : Feb 11, 2026, 9:57:55 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Genre Detail</title>

    </head>
    <body>
        <c:if test="${not empty sessionScope.message}">
            <div style="padding:10px;margin:10px 0;
                 background:#d4edda;color:#155724;
                 border:1px solid #c3e6cb;border-radius:5px;">
                ${sessionScope.message}
            </div>
            <c:remove var="message" scope="session"/>
        </c:if>

        <c:if test="${not empty message}">
            <div style="padding:10px;margin:10px 0;
                 background:#f8d7da;color:#721c24;
                 border:1px solid #f5c6cb;border-radius:5px;">
                ${message}
            </div>
        </c:if>

        <c:if test="${not empty genre}">
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Genre name</th>
                        <th>Description</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${genre.genreId}</td>
                        <td>
                            ${genre.genreName}

                        </td>
                        <td>${genre.descriptionText}</td>

                        <c:if test="${genre.status==1}"><td>active</td></c:if>
                        <c:if test="${genre.status==0}"><td>inactive</td></c:if>
                            <td>
                                <button onclick="openEdit()">Edit</button>                           
                            </td>
                        </tr>
                    </tbody>
                </table>
        </c:if>
        <br>
        <a href="${pageContext.request.contextPath}/genre">
            ← Back to Genre list
        </a>
        <%-- Edit --%>
        <div id="editModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeEdit()">&times;</span>

                <h3>Edit Genre</h3>

                <form action="${pageContext.request.contextPath}/genre" method="post">
                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" name="id" value="${genre.genreId}"/>

                    Name:
                    <input type="text" name="name" value="${genre.genreName}" required/><br/>

                    Description:
                    <input type="text" name="description" value="${genre.descriptionText}" required/><br/>
                    Status:
                    <select name="status">
                        <option value="1" ${genre.status==1?'selected':''}>Active</option>
                        <option value="0" ${genre.status==0?'selected':''}>Inactive</option>
                    </select><br/>
                    <button type="submit">Save</button>
                </form>
            </div>
        </div>
        <script>
            function openEdit() {
                document.getElementById("editModal").style.display = "block";
            }
            function closeEdit() {
                document.getElementById("editModal").style.display = "none";
            }
        </script>

        <c:if test="${openEdit}">
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    openEdit();
                });
            </script>
        </c:if>

        <style>
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
            }

            .modal-content {
                background: #fff;
                width: 400px;
                margin: 10% auto;
                padding: 20px;
                border-radius: 8px;
            }

            .close {
                float: right;
                font-size: 22px;
                cursor: pointer;
            }
        </style>
        <%-- Delete --%>
        <form action="${pageContext.request.contextPath}/genre" method="post">
            <input type="hidden" name="action" value="delete"/>
            <input type="hidden" name="id" value="${genre.genreId}"/>
            <button type="submit" onclick="return confirm('Delete this genre?')">
                Delete
            </button>
        </form>
    </body>
</html>
