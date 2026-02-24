/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Genre;
import com.mycompany.bookverse.service.GenreService;
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
@WebServlet(name = "GenreController", urlPatterns = {"/genre"})
public class GenreController extends HttpServlet {

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
        switch (view) {
            case "list":
                List<Genre> genres = genreServices.getAllGenres();
                if (genres == null || genres.isEmpty()) {
                    request.setAttribute("message", "No vouchers found");
                } else {
                    request.setAttribute("genres", genres);
                    request.setAttribute("contentPage", "genre-list.jsp");
                    request.setAttribute("activeMenu", "genre");
                }
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Genre> searchList = genreServices.searchGenres(keyword);
                if (searchList == null || searchList.isEmpty()) {
                    request.setAttribute("message", "No genre found for:" + keyword);
                } else {
                    request.setAttribute("genres", searchList);
                }
                request.setAttribute("contentPage", "genre-list.jsp");
                request.setAttribute("activeMenu", "genre");
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "detail":
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    response.sendRedirect("genre");                  
                    return;
                }
                try {
                    int id = Integer.parseInt(idStr);
                    Genre genre = genreServices.findGenreById(id);
                    if (genre == null) {
                        response.sendRedirect("genre");                  
                    return;
                    } else {
                        request.setAttribute("genre", genre);
                        request.getRequestDispatcher("/views/dashboard/genre-detail.jsp")
                                .forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("genre");                  
                    return;
                }
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/genre");
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
            response.sendRedirect(request.getContextPath() + "/genre");
            return;
        }
        if (action.equals("edit")) {
            try {

                int id = Integer.parseInt(request.getParameter("id"));
                Genre oldGenre = genreServices.findGenreById(id);
                if (oldGenre == null) {
                    request.getSession().setAttribute("message", "Genre not found");
                    response.sendRedirect(request.getContextPath() + "/genre");
                    return;
                }
                String msg = genreServices.editGenre(id,
                        request.getParameter("name"),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("status")));

                if (!msg.contains("successfully")) {
                    request.setAttribute("message", msg);
                    request.setAttribute("genre", oldGenre);
                    request.setAttribute("openEdit", true);
                    request.getRequestDispatcher("/views/genre-detail.jsp")
                            .forward(request, response);
                    return;
                }

                request.getSession().setAttribute("message", msg);
                response.sendRedirect(request.getContextPath() + "/genre?action=detail&id=" + id);
            } catch (Exception e) {
                request.getSession().setAttribute("message", "Invalid input data");
                response.sendRedirect(request.getContextPath() + "/genre");
            }
        } else if (action.equals("create")) {
            String genreName = request.getParameter("name");
            String description = request.getParameter("description");
            String msg = genreServices.insertGenre(genreName, description);
            if (!msg.contains("successfully")) {
                request.setAttribute("message", msg);
                request.setAttribute("openCreate", true);
                request.getRequestDispatcher("/views/genre-list.jsp")
                        .forward(request, response);
                return;
            }
            request.getSession().setAttribute("message", msg);
            response.sendRedirect(request.getContextPath() + "/genre");
        } else if (action.equals("delete")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String msg = genreServices.deleteGenre(id);
                if (!msg.contains("successfully")) {
                    request.setAttribute("message", msg);
                    request.getRequestDispatcher("/views/genre-list.jsp")
                            .forward(request, response);
                    return;
                }
                request.getSession().setAttribute("message", msg);
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("message", "Invalid voucher ID");
            }
            response.sendRedirect(request.getContextPath() + "/genre");
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
