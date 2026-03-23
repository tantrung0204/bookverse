<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/dashboard-index.css">

            <div class="container-fluid">
                <div class="page-header">
                    <p class="title">Dashboard Overview</p>
                    <p class="subtitle">Welcome to the Bookverse Administration System</p>
                </div>

                <div class="content-card">
                    <div class="welcome-container">
                        <i class="bi bi-house-door welcome-icon"></i>
                        <h3 class="welcome-title">
                            Welcome to the ${fn:toUpperCase(fn:substring(sessionScope.role, 0,
                            1))}${fn:substring(sessionScope.role, 1, fn:length(sessionScope.role))} Panel
                        </h3>
                        <p class="welcome-message">
                            Select an option from the sidebar menu to get started.
                        </p>
                    </div>
                </div>
            </div>