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
                        <textarea name="categoryName">${CategoryName}</textarea>
                        <c:if test="${not empty nameError}">
                            <p style="color:red; font-weight:bold;">
                                ${nameError}
                            </p>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td>Description</td>
                    <td>
                        <textarea name="descriptionText">${descriptionText}</textarea>
                        <c:if test="${not empty descError}">
                            <p style="color:red; font-weight:bold;">
                                ${descError}
                            </p>
                        </c:if>
                    </td>
                </tr>

                <tr>
                    <td>Status</td>
                    <td>
                        <select name="status">
                            <option value="1" ${status == 1 ? "selected" : ""}>Active</option>
                            <option value="0" ${status == 0 ? "selected" : ""}>Inactive</option>
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
