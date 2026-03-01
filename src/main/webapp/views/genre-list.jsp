<%-- 
    Document   : genre-view
    Created on : Feb 11, 2026, 8:48:53 AM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Genre management</title>
    </head>
    <body>
        <form action="genre" method="get">
            <input type="hidden" name="action" value="search"/>
            <input type="text" name="keyword" placeholder="Search genre name...">
            <button type="submit">Search</button>
        </form>
        <button onclick="openPopup()">+ Create Genre</button> <%-- button của create --%>
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

        <c:if test="${not empty genres}">
            <table border="1">                 
                <tr>
                    <th>ID</th>
                    
                    <th>Genre name</th>
                    
                    <th>Description</th>
                    
                    <th>Status</th>   
                       
                </tr>
                <c:forEach var="g" items="${genres}"> 
                    <tr>
                        <td>${g.genreId}</td>
                        <td>${g.genreName}</td>
                        <td>${g.descriptionText}</td>
                        
                        <c:if test="${g.status==1}"><td>active</td></c:if>
                        <c:if test="${g.status==0}"><td>inactive</td></c:if>
                            
                            <td>
                                <a href="${pageContext.request.contextPath}/genre?action=detail&id=${g.genreId}">
                                View Detail
                            </a>
                        </td>   
                            
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        
        <%-- Create --%>
        <div id="createModal" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closePopup()">&times;</span>

                <h3>Create Genre</h3>

                <c:if test="${not empty message}">
                    <p style="color:red">${message}</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/genre" method="post">
                    <input type="hidden" name="action" value="create"/>

                    Name:
                    <input type="text" name="name" required/><br/>

                    Description:
                    <input type="text" name="description">
<!--                    Status:
                    <select name="status">
                        <option value="1">Active</option>
                        <option value="0">Inactive</option>
                    </select><br/>-->

                    <button type="submit">Create</button>
                </form>
            </div>
        </div>
        <script>
            function openPopup() {
                document.getElementById("createModal").style.display = "block";
            }

            function closePopup() {
                document.getElementById("createModal").style.display = "none";
            }
        </script>

        <c:if test="${openCreate}">
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    openPopup();
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
</html>
