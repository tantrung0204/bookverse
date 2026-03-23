<%-- 
    Document   : customer-viewProfile
    Created on : Mar 14, 2026, 9:16:48 PM
    Author     : LECOO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="col-md-9">

    <div class="profile-card">

        <div class="d-flex align-items-center mb-4">

            <div class="avatar me-4">
                <img src="${user.profileImageUrl}" alt="Avatar" onerror="this.src='${pageContext.request.contextPath}/assets/images/default-avt.jpg';">
            </div>

            <div>
                <h2 class="mb-1">${user.username}</h2>
                <p class="mb-1 text-muted">${user.email}</p>
                <p class="text-muted mb-2">${user.address}</p> 

                <a class="btn btn-sm editProfile" href="${pageContext.request.contextPath}/profile?view=edit"> 
                    Edit Profile
                </a>

            </div>

        </div>


        <!-- stats -->
        <div class="row g-3">

            <a class="col-md-6" style=" text-decoration: none;  color: inherit;">
                <div class="stat-box stat-orders" >

                    <h5>Total Orders</h5>
                    <div class="stat-number">${countOrder}</div>

                </div>
            </a>                               

            <a class="col-md-6" style=" text-decoration: none;  color: inherit;">
                <div class="stat-box stat-reviews" href="#">

                    <h5>Reviews Written</h5>
                    <div class="stat-number">${countFeedback}</div>

                </div>
            </a>


        </div>
    </div>

</div>
