<%-- 
    Document   : dashboard
    Created on : Feb 19, 2026, 9:19:08 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bookverse Dashboard</title>
        <link href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link href="${pageContext.request.contextPath}/styles/dashboard.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="d-flex">

            <jsp:include page="sidebar.jsp" />

            <div class="content-wrapper">
                <jsp:include page="header.jsp" />

                <main class="main-content">
                    <c:if test="${not empty requestScope.contentPage}">
                        <jsp:include page="${requestScope.contentPage}" />
                    </c:if>

                    <%-- Trang mặc định khi mới vào dashboard --%>
                    <c:if test="${empty requestScope.contentPage}">
                        <h3>Welcome to Dashboard</h3>
                    </c:if>
                </main>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
    </body>
</html>
