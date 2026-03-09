<%-- 
    Document   : signin-customer
    Created on : Mar 9, 2026, 12:28:18 PM
    Author     : TrungNT - CE200064
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign in - Bookverse</title>
        <style>
            .error-msg {
                color: red;
                margin-bottom: 10px;
            }
            .login-container {
                width: 300px;
                margin: 100px auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <h2>Sign in</h2>

            <% if (request.getAttribute("errorMessage") != null) {%>
            <div class="error-msg">
                <%= request.getAttribute("errorMessage")%>
            </div>
            <% }%>

            <form action="${pageContext.request.contextPath}/signin" method="POST">
                <div>
                    <label for="username">Username:</label><br/>
                    <input type="text" id="username" name="username" value="${username}" required />
                </div>
                <br/>
                <div>
                    <label for="password">Password:</label><br/>
                    <input type="password" id="password" name="password" minlength="8" maxlength="20" required />
                </div>
                <br/>

                <div>
                    <label>Sign in as:</label><br/>
                    <input type="radio" id="roleCustomer" name="role" value="customer" 
                           ${empty selectedRole or selectedRole == 'customer' ? 'checked' : ''} />
                    <label for="roleCustomer">Customer</label>

                    <input type="radio" id="roleStaff" name="role" value="staff" 
                           ${selectedRole == 'staff' ? 'checked' : ''} />
                    <label for="roleStaff">Staff</label>
                </div>
                <br/>

                <button type="submit">Sign in</button>
            </form>
        </div>
    </body>
</html>
