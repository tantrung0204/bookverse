/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Genre;
import com.mycompany.bookverse.service.GenreService;
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
@WebServlet(name = "GenreManagementController", urlPatterns = {"/genre"})
public class GenreManagementController extends HttpServlet {

    private GenreService genreServices = new GenreService();

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
            out.println("<title>Servlet GenreController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GenreController at " + request.getContextPath() + "</h1>");
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
        int maxNode = PaginationConfig.MAX_PAGE_NODES;
        switch (view) {
            case "list":
                String success = (String) request.getSession().getAttribute("success");
                if (success != null) {
                    request.setAttribute("success", success);
                    request.getSession().removeAttribute("success");
                }

                int totalPages = genreServices.getTotalPages(pageSize);
                List<Genre> genres = genreServices.getGenresByPage(page, pageSize);
                if (genres == null || genres.isEmpty()) {
                    request.setAttribute("message", "No vouchers found");
                } else {
                    request.setAttribute("maxNote", maxNode);
                    request.setAttribute("genres", genres);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("contentPage", "genre-list.jsp");
                    request.setAttribute("activeMenu", "genre");
                }
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                totalPages = genreServices.getTotalPagesByKeyword(keyword, pageSize);
                List<Genre> searchList = genreServices.searchByKeyword(keyword, page, pageSize);
                if (searchList == null || searchList.isEmpty()) {
                    request.setAttribute("message", "No genre found for:" + keyword);
                } else {
                    request.setAttribute("genres", searchList);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("contentPage", "genre-list.jsp");
                    request.setAttribute("activeMenu", "genre");
                }

                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "detail":
                String idStr = request.getParameter("id");
                try {
                    int id = Integer.parseInt(idStr);
                    Genre genre = genreServices.findGenreById(id);
                    if (genre == null) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    } else {
                        long quantity = genreServices.countProductByGenreId(id);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("{\"quantity\": " + quantity + "}");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("genre");
                }
            default:
                response.sendRedirect("genre");
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
            response.sendRedirect("genre");
            return;
        }
        switch (action) {
            case "edit":
                try {
                    int id = Integer.parseInt(request.getParameter("genreId"));
                    Genre oldGenre = genreServices.findGenreById(id);
                    if (oldGenre == null) {
                        request.setAttribute("editError", "Genre not found");
                        request.getRequestDispatcher("/views/dashboard/genre-list.jsp")
                                .forward(request, response);
                        return;
                    }
                    String name = request.getParameter("genreName");
                    String des = request.getParameter("descriptionText");
                    int status = Integer.parseInt(request.getParameter("status"));
                    String msg = genreServices.editGenre(id, name, des, status);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("editError", msg);
                        request.setAttribute("openEditPopup", true);
                        request.setAttribute("editId", id);
                        request.setAttribute("editName", name);
                        request.setAttribute("editDesc", des);
                        request.setAttribute("editStatus", status);
                        List<Genre> genres = genreServices.getAllGenres();
                        request.setAttribute("genres", genres);
                        request.setAttribute("contentPage", "genre-list.jsp");
                        request.setAttribute("activeMenu", "genre");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }
                    request.getSession().setAttribute("success", "Edit successfully");
                    response.sendRedirect("genre");
                } catch (ServletException | IOException | NumberFormatException e) {
                    response.sendRedirect("genre");
                }
                break;
            case "create":
                try {
                    String name = request.getParameter("name");
                    String description = request.getParameter("description");
                    int status = Integer.parseInt(request.getParameter("status"));
                    String msg = genreServices.insertGenre(name, description, status);
                    if (!msg.contains("successfully")) {
                        request.setAttribute("createError", msg);
                        request.setAttribute("openCreatePopup", true);
                        request.setAttribute("createName", name);
                        request.setAttribute("createDesc", description);
                        request.setAttribute("createStatus", status);
                        List<Genre> genres = genreServices.getAllGenres();
                        request.setAttribute("genres", genres);
                        request.setAttribute("contentPage", "genre-list.jsp");
                        request.setAttribute("activeMenu", "genre");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }
                    request.getSession().setAttribute("success", "Create successfully");
                    response.sendRedirect("genre");
                } catch (ServletException | IOException | NumberFormatException e) {
                    response.sendRedirect("genre");
                }
                break;
            case "delete":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String msg = genreServices.deleteGenre(id);
                    if (!msg.contains("successfully")) {
                        request.setAttribute("deleteError", msg);
                        List<Genre> genres = genreServices.getAllGenres();
                        request.setAttribute("genres", genres);
                        request.setAttribute("contentPage", "genre-list.jsp");
                        request.setAttribute("activeMenu", "genre");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }
                } catch (ServletException | IOException | NumberFormatException e) {
                    response.sendRedirect("genre");
                }
                request.getSession().setAttribute("success", "Deletech successfully");
                response.sendRedirect("genre");
                break;
            default:
                response.sendRedirect("genre");
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
