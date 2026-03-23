<%-- 
    Document   : own-feedback
    Created on : Mar 13, 2026, 4:13:14 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Review History</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/own-feedback.css">
    </head>

    <body>

        <div class="review-container">

            <h1 class="title">Review History</h1>
            <p class="subtitle">Manage and view all your book reviews</p>

            <div class="review-count">
                ${feedbacks.size()} reviews found
            </div>

            <c:forEach var="fb" items="${feedbacks}">

                <div class="review-card">

                    <div class="book-image">
                        <img src="${fb.productId.imageUrl}" alt="book" onerror="this.src='${pageContext.request.contextPath}/assets/images/no-product-image.jpg';">
                    </div>

                    <div class="review-content">

                        <div class="review-top">

                            <div class="review-info">

                                <h3>${fb.productId.name}</h3>

                                <p class="author">
                                    Product Review
                                </p>

                                <div class="rating">

                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i <= fb.rating}">
                                                ⭐
                                            </c:when>   
                                        </c:choose>
                                    </c:forEach>

                                    <span>(${fb.rating}/5)</span>

                                </div>

                                <div class="date">
                                    Reviewed on 
                                    <fmt:formatDate value="${fb.createdAt}" pattern="MMMM dd, yyyy"/>
                                </div>

                            </div>

                            <div class="actions">

                                <button class="btn-edit"
                                        onclick="openEditModal(
                                        ${fb.feedbackId},
                                        ${fb.rating},
                                                        `${fb.contentText}`
                                                        )">
                                    Edit Review
                                </button>

                                <a class="btn-delete"
                                   href="${pageContext.request.contextPath}/feedback?action=delete&id=${fb.feedbackId}"
                                   onclick="return confirm('Delete this review?')">
                                    Delete
                                </a>

                            </div>

                        </div>

                        <p class="review-text">
                            ${fb.contentText}
                        </p>

                    </div>

                </div>

            </c:forEach>

        </div>

        <div id="editModal" class="modal">

            <div class="modal-content">

                <span class="close" onclick="closeEditModal()">&times;</span>

                <h3>Edit Review</h3>

                <form id="editForm">

                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" id="editFeedbackId">

                    <div class="form-group">
                        <label>Rating</label>
                        <select name="rating" id="editRating">
                            <option value="5">⭐⭐⭐⭐⭐</option>
                            <option value="4">⭐⭐⭐⭐</option>
                            <option value="3">⭐⭐⭐</option>
                            <option value="2">⭐⭐</option>
                            <option value="1">⭐</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Comment</label>
                        <textarea name="content" id="editContent" rows="4"></textarea>
                    </div>

                    <button type="submit" class="btn-confirm">
                        Update Review
                    </button>

                </form>

            </div>

        </div>
        <script>
            function openEditModal(id, rating, content) {

                document.getElementById("editFeedbackId").value = id;

                document.getElementById("editRating").value = rating;

                document.getElementById("editContent").value = content;

                document.getElementById("editModal").style.display = "flex";
            }

            function closeEditModal() {
                document.getElementById("editModal").style.display = "none";
            }

            document.getElementById("editForm").onsubmit = function (e) {

                e.preventDefault();

                let formData = new FormData(this);

                fetch("${pageContext.request.contextPath}/feedback", {
                    method: "POST",
                    body: formData
                })
                        .then(res => res.text())
                        .then(data => {

                            if (data === "success") {
                                alert("Review updated successfully!");
                                location.reload();
                            } else {
                                alert("Update failed!");
                            }

                        });

            }
        </script>

    </body>
</html>

