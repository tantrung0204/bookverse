<%-- 
    Document   : customer-list
    Created on : Feb 9, 2026, 4:23:54 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/customer-list.css">

<div class="container-fluid">
    <div class="page-header">
        <p class="title">Manage Customers</p>
        <p class="subtitle">Manage and monitor all customer accounts</p>
    </div>

    <div class="content-card">
        <div class="toolbar">
            <a class="btn-add" href="#">
                <i class="bi bi-plus-lg me-1"></i> Add New Customer
            </a>

            <form method="get" action="" class="search-form">
                <div class="search-box">
                    <i class="bi bi-search"></i>
                    <input type="text" name="keyword" placeholder="Search customer" value="">
                </div>
            </form>
        </div>


        <c:if test="${not empty requestScope.customers}">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Address</th>
                        <th>Phone number</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${requestScope.customers}">
                        <tr>
                            <td>${c.customerId}</td>
                            <td>${c.username}</td>
                            <td>${c.email}</td>
                            <td>${c.address}</td>
                            <td>${c.phoneNumber}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

    </div>
</div>



