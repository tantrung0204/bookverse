<%-- 
    Document   : index
    Created on : Feb 28, 2026, 3:13:06 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css"  type="text/css"/>
    </head>
    <body>
        <jsp:include page="header-index.jsp" />
        <jsp:include page="navbar.jsp" />

        <main class="container my-5"style="min-height: 60vh;">
            <h1>Welcome to Bookverse</h1>
            <p>Your vintage-modern bookstore experience.</p>
        </main>

        <jsp:include page="footer-index.jsp" />
        
        <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js" type="text/javascript"></script>
    </body>
</html>
