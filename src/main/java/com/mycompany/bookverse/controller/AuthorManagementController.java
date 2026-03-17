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
@WebServlet(name = "AuthorManagementController", urlPatterns = {"/dashboard/author"})
public class AuthorManagementController extends HttpServlet {

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
        if (view == null || view.isEmpty()) {
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
        int maxNode = PaginationConfig.MAX_PAGE_NODES;
        switch (view) {
            case "list":
                String success = (String) request.getSession().getAttribute("success");
                String createError = (String) request.getSession().getAttribute("createError");
                String editError = (String) request.getSession().getAttribute("editError");
                String deleteError = (String) request.getSession().getAttribute("deleteError");
                if (success != null) {
                    request.setAttribute("success", success);
                    request.getSession().removeAttribute("success");
                } else if (editError != null) {
                    request.setAttribute("editError", (String) request.getSession().getAttribute("editError"));
                    request.setAttribute("openEditPopup", true);
                    request.setAttribute("editId", request.getSession().getAttribute("ceditId"));
                    request.setAttribute("editName", (String) request.getSession().getAttribute("editName"));
                    request.setAttribute("editBirth", request.getSession().getAttribute("editBirth"));
                    request.setAttribute("editNat", (String) request.getSession().getAttribute("editNat"));
                    request.setAttribute("editBio", (String) request.getSession().getAttribute("editBio"));

                    request.getSession().removeAttribute("editError");
                    request.getSession().removeAttribute("openEditPopup");
                    request.getSession().removeAttribute("editId");
                    request.getSession().removeAttribute("editName");
                    request.getSession().removeAttribute("editBirth");
                    request.getSession().removeAttribute("editNat");
                    request.getSession().removeAttribute("editBio");
                } else if (createError != null) {
                    request.setAttribute("createError", (String) request.getSession().getAttribute("createError"));
                    request.setAttribute("openCreatePopup", true);
                    request.setAttribute("createName", (String) request.getSession().getAttribute("createName"));
                    request.setAttribute("birth", request.getSession().getAttribute("birth"));
                    request.setAttribute("nationality", (String) request.getSession().getAttribute("nationality"));
                    request.setAttribute("biography", (String) request.getSession().getAttribute("biography"));

                    request.getSession().removeAttribute("createError");
                    request.getSession().removeAttribute("openCreatePopup");
                    request.getSession().removeAttribute("birth");
                    request.getSession().removeAttribute("nationality");
                    request.getSession().removeAttribute("biography");
                } else if (deleteError != null) {
                    request.setAttribute("deleteError", deleteError);
                    request.getSession().removeAttribute("deleteError");
                }

                int totalPages = authorServices.getTotalPages(pageSize);
                List<Author> authors = authorServices.getAuthorsByPage(page, pageSize);
                if (authors == null || authors.isEmpty()) {
                    request.setAttribute("message", "No authors found");
                } else {
                    request.setAttribute("maxNode", maxNode);
                    request.setAttribute("authors", authors);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
                }
                request.setAttribute("contentPage", "author-list.jsp");
                request.setAttribute("activeMenu", "author");
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                totalPages = authorServices.getTotalPagesByKeyword(keyword, pageSize);
                List<Author> searchList = authorServices.searchByKeyword(keyword, page, pageSize);
                if (searchList == null || searchList.isEmpty()) {
                    request.setAttribute("message", "No author found for:" + keyword);
                } else {
                    request.setAttribute("maxNode", maxNode);
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
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
                        request.getSession().setAttribute("editError", msg);
                        request.getSession().setAttribute("editId", id);
                        request.getSession().setAttribute("editName", name);
                        request.getSession().setAttribute("editBirth", birthStr);
                        request.getSession().setAttribute("editNat", nat);
                        request.getSession().setAttribute("editBio", bio);
                        response.sendRedirect("author");
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
                        request.getSession().setAttribute("createError", msg);                      
                        request.getSession().setAttribute("createName", name);
                        request.getSession().setAttribute("birth", birth);
                        request.getSession().setAttribute("nationality", nat);
                        request.getSession().setAttribute("biography", bio);
                        response.sendRedirect("author");
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
                        request.getSession().setAttribute("deleteError", msg);
                        response.sendRedirect("author");
                        return;
                    }
                    request.getSession().setAttribute("success", "Delete successfully");
                    response.sendRedirect("author");
                } catch (IOException | NumberFormatException e) {
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
