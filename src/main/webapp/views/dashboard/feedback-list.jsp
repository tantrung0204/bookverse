<%-- 
    Document   : feedback
    Created on : Mar 9, 2026, 8:44:22 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/styles/notification-list.css">

<div class="container-fluid">

    <!-- HEADER -->
    <div class="page-header">
        <p class="title">Feedback Management</p>
        <p class="subtitle">Manage customer feedback</p>
    </div>

    <div class="content-card">

        <!-- TOOLBAR -->
        <div class="toolbar">

            <form class="search-form"
                  action="${pageContext.request.contextPath}/dashboard/feedback"
                  method="get">

                <input type="hidden" name="action" value="search"/>

                <div style="display:flex; gap:10px">

                    <div class="search-box">
                        <i class="bi bi-search"></i>
                        <input type="text"
                               name="keyword"
                               value="${param.keyword}"
                               placeholder="Search product name...">
                    </div>

                    <select name="rating"
                            class="form-control"
                            style="width:140px"
                            onchange="this.form.submit()">

                        <option value="">All rating</option>

                        <c:forEach begin="1" end="5" var="i">
                            <option value="${i}"
                                    ${param.rating == i ? "selected" : ""}>
                                ${i} ⭐
                            </option>
                        </c:forEach>

                    </select>
                </div>

            </form>

        </div>

        <!-- SUCCESS -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert-success">
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>


        <!-- TABLE -->
        <c:choose>

            <c:when test="${not empty feedbacks}">

                <table class="custom-table">

                    <thead>
                        <tr>
                            <th width="10%">ID</th>
                            <th width="20%">Customer</th>
                            <th width="20%">Product</th>
                            <th width="20%">Rating</th>
                            <th width="15%">Date</th>
                            <th width="15%">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="f" items="${feedbacks}">
                            <tr>
                                <td><strong>${f.feedbackId}</strong></td>

                                <td>${f.customerId.fullName}</td>

                                <td>${f.productId.name}</td>

                                <td>
                                    <span style="color:#f5b301;font-size:16px">
                                        <c:forEach begin="1" end="${f.rating}">
                                            ⭐
                                        </c:forEach>
                                    </span>
                                </td>

                                <td>${f.createdAt}</td>

                                <td>

                                    <div class="action-buttons">

                                        <!-- VIEW DETAIL -->
                                        <button class="btn-action btn-detail"
                                                title="View Detail"
                                                onclick="openFeedbackDetail(
                                                                '${f.feedbackId}',
                                                                '${f.customerId.fullName}',
                                                                '${f.productId.name}',
                                                                '${f.rating}',
                                                                `${f.contentText}`,
                                                                '${f.createdAt}'
                                                                )">
                                            <i class="bi bi-eye"></i>
                                        </button>
                                        <!-- DELETE -->
                                        <form action="${pageContext.request.contextPath}/dashboard/feedback"
                                              method="post"
                                              style="display:inline;">

                                            <input type="hidden" name="action" value="delete"/>
                                            <input type="hidden" name="id" value="${f.feedbackId}"/>

                                            <button type="submit"
                                                    class="btn-action btn-delete"
                                                    title="Delete"
                                                    onclick="return confirm('Delete this feedback?')">

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

                <div class="empty-state">

                    <i class="bi bi-inbox" style="font-size:40px;"></i>

                    <p class="mt-2">No feedback found.</p>

                </div>

            </c:otherwise>

        </c:choose>


        <!-- PAGINATION -->
        <c:if test="${totalPages > 1}">

            <div class="pagination">

                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/dashboard/feedback?page=${currentPage - 1}"
                       class="page-btn">«</a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/dashboard/feedback?page=${i}"
                       class="page-btn ${i == currentPage ? 'active' : ''}">
                        ${i}
                    </a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/dashboard/feedback?page=${currentPage + 1}"
                       class="page-btn">»</a>
                </c:if>

            </div>

        </c:if>

    </div>

    <!-- FEEDBACK DETAIL POPUP -->

    <div id="feedbackDetailModal" class="modal-overlay">

        <div class="modal-content">

            <div class="modal-header">
                <h3>Feedback Detail</h3>
            </div>

            <table class="detail-table">

                <tr>
                    <th>ID:</th>
                    <td id="fbId"></td>
                </tr>

                <tr>
                    <th>Customer:</th>
                    <td id="fbCustomer"></td>
                </tr>

                <tr>
                    <th>Product:</th>
                    <td id="fbProduct"></td>
                </tr>

                <tr>
                    <th>Rating:</th>
                    <td id="fbRating"></td>
                </tr>

                <tr>
                    <th>Content:</th>
                    <td id="fbContent"></td>
                </tr>

                <tr>
                    <th>Date:</th>
                    <td id="fbDate"></td>
                </tr>

            </table>

            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeFeedbackDetail()">Close</button>
            </div>

        </div>

    </div>

</div>

<script>

    function openFeedbackDetail(id, customer, product, rating, content, date) {

        document.getElementById("fbId").innerText = id;
        document.getElementById("fbCustomer").innerText = customer;
        document.getElementById("fbProduct").innerText = product;

        let stars = "";
        for (let i = 0; i < rating; i++) {
            stars += "⭐";
        }

        document.getElementById("fbRating").innerText = stars;

        document.getElementById("fbContent").innerText = content;

        document.getElementById("fbDate").innerText = date;

        document.getElementById("feedbackDetailModal").style.display = "flex";

    }

    function closeFeedbackDetail() {
        document.getElementById("feedbackDetailModal").style.display = "none";
    }

</script>