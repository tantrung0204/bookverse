/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Author;
import com.mycompany.bookverse.service.AuthorService;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author LECOO
 */
@WebServlet(name = "AuthorController", urlPatterns = {"/author"})
public class AuthorController extends HttpServlet {

    private AuthorService authorServices = new AuthorService();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AuthorController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthorController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        if (view == null) {
            view = "list";
        }
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
        
        switch (view) {
            case "list":
                String success = (String) request.getSession().getAttribute("success");
                if (success != null) {
                    request.setAttribute("success", success);
                    request.getSession().removeAttribute("success");
                }
                List<Author> authors = authorServices.getAuthorsByPage(page, pageSize);
                if (authors == null || authors.isEmpty()) {
                    request.setAttribute("message", "No vouchers found");
                } else {
                    request.setAttribute("authors", authors);
                    request.setAttribute("contentPage", "author-list.jsp");
                    request.setAttribute("activeMenu", "author");
                }
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Author> searchList = authorServices.searchAuthors(keyword);
                if (searchList == null || searchList.isEmpty()) {
                    request.setAttribute("message", "No author found for:" + keyword);
                } else {
                    request.setAttribute("authors", searchList);
                }
                request.setAttribute("contentPage", "author-list.jsp");
                request.setAttribute("activeMenu", "author");
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "detail":
                String idStr = request.getParameter("id");
                try {
                    int id = Integer.parseInt(idStr);
                    Author Author = authorServices.findAuthorById(id);
                    if (Author == null) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    } else {
                        Long quantity = authorServices.countBooksByAuthorId(id);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"quantity\": " + quantity + "}");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("author");
                }
            default:
                response.sendRedirect("author");
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("author");
            return;
        }
        switch (action) {
            case "edit":
                try {
                    int id = Integer.parseInt(request.getParameter("authorId"));
                    Author oldAuthor = authorServices.findAuthorById(id);
                    if (oldAuthor == null) {
                        request.setAttribute("editError", "Author not found");
                        request.getRequestDispatcher("/views/dashboard/author-list.jsp")
                                .forward(request, response);
                        return;
                    }
                    String name = request.getParameter("authorName");
                    String nat = request.getParameter("nationality");
                    String birthStr = request.getParameter("birth");
                    String bio = request.getParameter("biography");
                    String msg = authorServices.editAuthor(id, name, birthStr, nat, bio);
                    if (!msg.contains("successfully")) {
                        request.setAttribute("editError", msg);
                        request.setAttribute("openEditPopup", true);
                        request.setAttribute("editId", id);
                        request.setAttribute("editName", name);
                        request.setAttribute("editBirth", birthStr);
                        request.setAttribute("editNat", nat);
                        request.setAttribute("editBio", bio);
                        List<Author> authors = authorServices.getAllAuthors();
                        request.setAttribute("authors", authors);
                        request.setAttribute("contentPage", "author-list.jsp");
                        request.setAttribute("activeMenu", "author");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }
                    request.getSession().setAttribute("success", "Edit successfully");
                    response.sendRedirect("author");
                } catch (ServletException | IOException | NumberFormatException e) {
                    response.sendRedirect("author");
                }
                break;
            case "create":
                try {
                    String name = request.getParameter("name");
                    String birth = request.getParameter("birth");
                    String nat = request.getParameter("nationality");
                    String bio = request.getParameter("biography");
                    String msg = authorServices.insertAuthor(name, birth, nat, bio);
                    if (!msg.contains("successfully")) {
                        request.setAttribute("createError", msg);
                        request.setAttribute("openCreatePopup", true);
                        request.setAttribute("createName", name);
                        request.setAttribute("birth", birth);
                        request.setAttribute("nationality", nat);
                        request.setAttribute("biography", bio);
                        List<Author> authors = authorServices.getAllAuthors();
                        request.setAttribute("authors", authors);
                        request.setAttribute("contentPage", "author-list.jsp");
                        request.setAttribute("activeMenu", "author");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }
                    request.getSession().setAttribute("success", "Create successfully");
                    response.sendRedirect("author");
                } catch (IOException | NumberFormatException e) {
                    response.sendRedirect("author");
                }
                break;
            case "delete":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String msg = authorServices.deleteAuthor(id);
                    if (!msg.contains("successfully")) {
                        request.setAttribute("deleteError", msg);
                        List<Author> Authors = authorServices.getAllAuthors();
                        request.setAttribute("authors", Authors);
                        request.setAttribute("contentPage", "author-list.jsp");
                        request.setAttribute("activeMenu", "author");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }
                    request.getSession().setAttribute("success", "Deletech successfully");
                    response.sendRedirect("author");
                } catch (ServletException | IOException | NumberFormatException e) {
                    request.getSession().setAttribute("success", "Loi quan que gi vay");
                    response.sendRedirect("author");
                }

                break;
            default:
                response.sendRedirect("author");
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
