<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product detail</title>
        <style>
            /* CSS cơ bản cho giao diện đẹp hơn */
            .container {
                display: flex;
                gap: 30px;
                padding: 20px;
                font-family: sans-serif;
            }
            .product-img {
                flex: 1;
            }
            .product-img img {
                width: 100%;
                max-width: 400px;
                border-radius: 8px;
                border: 1px solid #ddd;
            }
            .product-info {
                flex: 2;
            }

            .badge {
                padding: 5px 10px;
                border-radius: 4px;
                color: white;
                font-weight: bold;
                font-size: 0.8rem;
            }
            .badge-book {
                background-color: #007bff;
            }
            .badge-stationery {
                background-color: #6f42c1;
            }

            .price {
                font-size: 1.5rem;
                color: #dc3545;
                font-weight: bold;
            }
            .specs-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            .specs-table th, .specs-table td {
                padding: 8px;
                border-bottom: 1px solid #eee;
                text-align: left;
            }

            /* CSS cho phần mua hàng */
            .action-area {
                margin-top: 30px;
                padding: 20px;
                background-color: #f8f9fa;
                border-radius: 8px;
                border: 1px solid #e9ecef;
            }
            .qty-input {
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
                width: 60px;
                text-align: center;
                margin-right: 15px;
            }
            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                font-size: 1rem;
                transition: 0.2s;
            }
            .btn:hover {
                opacity: 0.9;
            }
            .btn-cart {
                background-color: #ffc107;
                color: #000;
                margin-right: 10px;
            } /* Màu vàng */
            .btn-buy {
                background-color: #dc3545;
                color: #fff;
            } /* Màu đỏ */
            .btn:disabled {
                background-color: #ccc;
                cursor: not-allowed;
            }
            /* CSS cho thông báo (Alert) */
            #toast {
                visibility: hidden; /* Mặc định ẩn */
                min-width: 250px;
                background-color: #333;
                color: #fff;
                text-align: center;
                border-radius: 4px;
                padding: 16px;
                position: fixed;
                z-index: 1000;
                right: 30px;
                top: 30px;
                font-size: 17px;
                box-shadow: 0px 4px 8px rgba(0,0,0,0.2);
                display: flex;
                align-items: center;
                gap: 10px;
                opacity: 0;
                transition: opacity 0.5s ease-in-out;
            }

            /* Class này sẽ được JS thêm vào để hiện toast */
            #toast.show {
                visibility: visible;
                opacity: 1;
                animation: slideIn 0.5s, fadeOut 0.5s 2.5s forwards;
                /* Giải thích animation: 
                   - slideIn: chạy trong 0.5s đầu
                   - fadeOut: chờ 2.5s rồi chạy trong 0.5s, sau đó giữ nguyên trạng thái ẩn (forwards)
                */
            }

            /* Màu sắc theo trạng thái */
            .toast-success {
                background-color: #28a745 !important; /* Xanh lá */
                border-left: 5px solid #1e7e34;
            }

            .toast-error {
                background-color: #dc3545 !important; /* Đỏ */
                border-left: 5px solid #bd2130;
            }

            /* Hiệu ứng trượt từ phải sang */
            @keyframes slideIn {
                from {
                    right: -300px;
                    opacity: 0;
                }
                to {
                    right: 30px;
                    opacity: 1;
                }
            }

            /* Hiệu ứng mờ dần đi */
            @keyframes fadeOut {
                from {
                    opacity: 1;
                }
                to {
                    opacity: 0;
                    visibility: hidden;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="product-img">
                <img src="${p.imageUrl}" alt="${p.name}" />
            </div>
            <div class="product-info">
                <c:if test="${p.type == 'Book'}">
                    <span class="badge badge-book">Book</span>
                </c:if>
                <c:if test="${p.type == 'Stationery'}">
                    <span class="badge badge-stationery">Stationery</span>
                </c:if>

                <h1>${p.name}</h1>
                <p class="price">
                    <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                </p>
                <p>Status: 
                    <c:choose>
                        <c:when test="${p.stockQuantity > 0}">
                            <span style="color: green">Available (${p.stockQuantity})</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red">Unavailable</span>
                        </c:otherwise>
                    </c:choose>
                </p>

                <hr/>

                <div class="action-area">
                    <form action="cart" method="post">
                        <input type="hidden" name="productId" value="${p.productId}"/>

                        <label for="quantity">Quantity:</label>
                        <input type="number" id="quantity" name="quantity" class="qty-input" 
                               value="1" min="1" max="${p.stockQuantity}" required 
                               ${p.stockQuantity <= 0 ? 'disabled' : ''} />

                        <br><br>

                        <button type="submit" name="action" value="add" class="btn btn-cart" 
                                ${p.stockQuantity <= 0 ? 'disabled' : ''}>
                            <i class="fa fa-shopping-cart"></i> Add to Cart
                        </button>

                        <button type="submit" name="action" value="buy" class="btn btn-buy" 
                                ${p.stockQuantity <= 0 ? 'disabled' : ''}>
                            Buy Now
                        </button>
                    </form>
                </div>
                <h3>Description</h3>
                <table class="specs-table">
                    <c:if test="${p.type == 'Book'}">
                        <tr>
                            <th>Author:</th>
                            <td>
                                <c:forEach var="a" items="${p.authorCollection}">
                                    ${a.authorName} 
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <th>Publisher:</th>
                            <td>${p.publisher}</td>
                        </tr>
                        <tr>
                            <th>Published Year:</th>
                            <td>${p.publishedYear}</td>
                        </tr>
                        <tr>
                            <th>ISBN:</th>
                            <td>${p.isbn}</td>
                        </tr>
                        <tr>
                            <th>Translator:</th>
                            <td>${p.translator}</td>
                        </tr>
                        <tr>
                            <th colspan="2">Description:</th>
                        </tr>
                        <tr>
                            <td colspan="2">${p.descriptionText}</td>
                        </tr>
                    </c:if>

                    <c:if test="${p.type == 'Stationery'}">
                        <tr>
                            <th>Color:</th>
                            <td>${p.color}</td>
                        </tr>
                        <tr>
                            <th>Material:</th>
                            <td>${p.material}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>

        <c:if test="${not empty sessionScope.cartMessage}">

            <div id="toast" class="${sessionScope.messageType == 'error' ? 'toast-error' : 'toast-success'}">
                <c:if test="${sessionScope.messageType != 'error'}">
                    <i class="fa fa-check-circle"></i>
                </c:if>
                <c:if test="${sessionScope.messageType == 'error'}">
                    <i class="fa fa-exclamation-circle"></i>
                </c:if>

                <span>${sessionScope.cartMessage}</span>
            </div>

            <script>
                window.onload = function () {
                    var x = document.getElementById("toast");
                    // Thêm class "show" để kích hoạt CSS animation
                    x.className += " show";

                    // Sau 3 giây (3000ms), animation fadeOut trong CSS sẽ tự làm nó biến mất.
                    // Ta set timeout để xóa hẳn nó khỏi DOM nếu muốn, hoặc để nguyên cũng được vì nó đã tàng hình.
                    setTimeout(function () {
                        x.className = x.className.replace(" show", "");
                    }, 3000);
                };
            </script>

            <c:remove var="cartMessage" scope="session"/>
            <c:remove var="messageType" scope="session"/>
        </c:if>
    </body>
</html>