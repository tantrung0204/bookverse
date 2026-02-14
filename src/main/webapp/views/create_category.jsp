<%-- 
    Document   : create_category
    Created on : 12 Feb 2026, 13:37:11
    Author     : NganTTK-CE190411
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/category?action=create" method="post"> 

            <table border="1">
                <tr>
                    <td>Category Name</td>
                    <td>
                        <input type="text" name="categoryName" required>
                    </td>
                </tr>

                <tr>
                    <td>Description</td>
                    <td>
                        <textarea name="descriptionText"></textarea>
                    </td>
                </tr>

                <tr>
                    <td>Status</td>
                    <td>
                        <select name="status">
                            <option value="1">Active</option>
                            <option value="0">Inactive</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <button type="submit">Create</button>
                    </td>
                </tr>
            </table>

        </form>
    </body>
</html>
