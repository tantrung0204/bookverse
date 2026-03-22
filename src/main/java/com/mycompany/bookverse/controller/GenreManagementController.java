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
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author LECOO
 */
@WebServlet(name = "GenreManagementController", urlPatterns = {"/dashboard/genre"})
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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
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
                        request.setAttribute("editId", request.getSession().getAttribute("editId"));
                        request.setAttribute("editName", (String) request.getSession().getAttribute("editName"));
                        request.setAttribute("editDesc", (String) request.getSession().getAttribute("editDesc"));
                        request.setAttribute("editStatus", request.getSession().getAttribute("editStatus"));

                        request.getSession().removeAttribute("editError");
                        request.getSession().removeAttribute("openEditPopup");
                        request.getSession().removeAttribute("editId");
                        request.getSession().removeAttribute("editName");
                        request.getSession().removeAttribute("editDesc");
                        request.getSession().removeAttribute("editStatus");

                    } else if (createError != null) {
                        request.setAttribute("createError", (String) request.getSession().getAttribute("createError"));
                        request.setAttribute("openCreatePopup", true);
                        request.setAttribute("createName", (String) request.getSession().getAttribute("createName"));
                        request.setAttribute("createDesc", (String) request.getSession().getAttribute("createDesc"));
                        request.setAttribute("createStatus", request.getSession().getAttribute("createStatus"));

                        request.getSession().removeAttribute("createError");
                        request.getSession().removeAttribute("openCreatePopup");
                        request.getSession().removeAttribute("createName");
                        request.getSession().removeAttribute("createDesc");
                        request.getSession().removeAttribute("createStatus");
                    } else if (deleteError != null) {
                        request.setAttribute("deleteError", deleteError);
                        request.getSession().removeAttribute("deleteError");
                    }

                    int totalPages = genreServices.getTotalPages(pageSize);
                    List<Genre> genres = genreServices.getGenresByPage(page, pageSize);
                    if (genres == null || genres.isEmpty()) {
                        request.setAttribute("message", "No genres found");
                    } else {
                        request.setAttribute("maxNode", maxNode);
                        request.setAttribute("genres", genres);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages", totalPages);
                    }
                    request.setAttribute("contentPage", "genre-list.jsp");
                    request.setAttribute("activeMenu", "genre");
                    request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                    break;
                case "search":
                    String keyword = request.getParameter("keyword");
                    totalPages = genreServices.getTotalPagesByKeyword(keyword, pageSize);
                    List<Genre> searchList = genreServices.searchByKeyword(keyword, page, pageSize);
                    if (searchList == null || searchList.isEmpty()) {
                        request.setAttribute("message", "No genre found for:" + keyword);
                    } else {
                        request.setAttribute("maxNode", maxNode);
                        request.setAttribute("keyword", keyword);
                        request.setAttribute("genres", searchList);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages", totalPages);

                    }
                    request.setAttribute("contentPage", "genre-list.jsp");
                    request.setAttribute("activeMenu", "genre");
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
        request.getRequestDispatcher("/views/public/signin.jsp").forward(request, response);
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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
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

                        Genre genre = new Genre();
                        genre.setGenreId(id);
                        genre.setGenreName(name);
                        genre.setStatus(status);
                        genre.setDescriptionText(des);
                        String msg = genreServices.editGenre(genre);

                        if (!msg.contains("successfully")) {
                            request.getSession().setAttribute("editError", msg);
                            request.getSession().setAttribute("editId", id);
                            request.getSession().setAttribute("editName", name);
                            request.getSession().setAttribute("editDesc", des);
                            request.getSession().setAttribute("editStatus", status);
                            response.sendRedirect("genre");
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

                        Genre genre = new Genre();
                        genre.setGenreName(name);
                        genre.setDescriptionText(description);
                        genre.setStatus(status);
                        String msg = genreServices.insertGenre(genre);
                        if (!msg.contains("successfully")) {
                            request.getSession().setAttribute("createError", msg);
                            request.getSession().setAttribute("createName", name);
                            request.getSession().setAttribute("createDesc", description);
                            request.getSession().setAttribute("createStatus", status);

                            response.sendRedirect("genre");
                            return;
                        }
                        request.getSession().setAttribute("success", "Create successfully");
                        response.sendRedirect("genre");
                    } catch (IOException | NumberFormatException e) {
                        response.sendRedirect("genre");
                    }
                    break;
                case "delete":
                    try {
                        int id = Integer.parseInt(request.getParameter("id"));
                        String msg = genreServices.deleteGenre(id);
                        if (!msg.contains("successfully")) {
                            request.getSession().setAttribute("deleteError", msg);
                            response.sendRedirect("genre");
                            return;
                        }
                    } catch (IOException | NumberFormatException e) {
                        response.sendRedirect("genre");
                    }
                    request.getSession().setAttribute("success", "Delete successfully");
                    response.sendRedirect("genre");
                    break;
                default:
                    response.sendRedirect("genre");
                    break;
            }
        }
        request.getRequestDispatcher("/views/public/signin.jsp").forward(request, response);
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
