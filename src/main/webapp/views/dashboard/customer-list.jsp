<%-- 
    Document   : customer-list
    Created on : Feb 9, 2026, 4:23:54 PM
    Author     : TrungNT - CE200064
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="mb-4">
    <h3 class="fw-bold">Manager Customer</h3>
    <p class="text-muted">Manage and monitor all user accounts</p>
</div>

<div class="d-flex justify-content-between align-items-center mb-4 bg-white p-3 rounded shadow-sm">
    <div class="d-flex gap-3 w-50">
        <form action="${pageContext.request.contextPath}/customer" method="GET" class="d-flex w-100">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" class="form-control" placeholder="Search users...">
            <button type="submit" class="btn btn-outline-secondary ms-2"><i class="bi bi-search"></i></button>
        </form>
        
        <select class="form-select w-50">
            <option value="">All Status</option>
            <option value="1">Active</option>
            <option value="0">Blocked</option>
        </select>
    </div>
    
    <button class="btn btn-success px-4" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
        Add User
    </button>
</div>

<p class="text-muted mb-3">${customers.size()} Customer found</p>

<div class="bg-white rounded shadow-sm overflow-hidden">
    <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${customers}" var="c" varStatus="loop">
                <tr>
                    <td class="text-muted">${loop.index + 1 < 10 ? '0' : ''}${loop.index + 1}</td>
                    <td class="fw-semibold">${c.fullName}</td>
                    <td>${c.email}</td>
                    <td>
                        <c:choose>
                            <c:when test="${c.status == 1}">
                                <span class="badge rounded-pill bg-success px-3 py-2 bg-opacity-25 text-success">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge rounded-pill bg-danger px-3 py-2 bg-opacity-25 text-danger">Blocked</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="btn btn-primary btn-sm px-3" data-bs-toggle="modal" data-bs-target="#editCustomerModal${c.customerId}">Edit</button>
                        <button class="btn btn-danger btn-sm px-3" data-bs-toggle="modal" data-bs-target="#deleteCustomerModal${c.customerId}">Delete</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<div class="modal fade" id="addCustomerModal" tabindex="-1" aria-labelledby="addCustomerLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/customer" method="POST">
                <input type="hidden" name="action" value="create">
                
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold fs-4" id="addCustomerLabel">Add Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body pt-0">
                    <p class="text-muted mb-4">Update Customer information and details</p>
                    
                    <div class="row">
                        <div class="col-md-7">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Username</label>
                                <input type="text" class="form-control" name="username" required placeholder="Nhập username...">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Full Name</label>
                                <input type="text" class="form-control" name="fullName" required placeholder="Nhập họ tên...">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Email</label>
                                <input type="email" class="form-control" name="email" required placeholder="Nhập email...">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Password</label>
                                <input type="password" class="form-control" name="password" required placeholder="Nhập mật khẩu...">
                            </div>
                        </div>
                        
                        <div class="col-md-5 d-flex flex-column align-items-center justify-content-center">
                            <label class="form-label fw-semibold align-self-start">Avatar</label>
                            <div class="border border-dashed rounded p-4 text-center w-100" style="cursor: pointer;">
                                <i class="bi bi-image fs-1 text-muted"></i>
                                <p class="mb-0 mt-2">Upload new image</p>
                                <small class="text-muted">PNG, JPG up to 10MB</small>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary px-4">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>



