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
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Genre genre = genreServices.findGenreById(id);
                request.setAttribute("genre_detail", genre);
                request.getRequestDispatcher("/views/genreDetail-view.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                System.out.println("id error, reload page");
            }
        }
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            Genre genre = genreServices.findGenreByName(keyword);
            request.setAttribute("genre", genre);
            request.getRequestDispatcher("/views/genre-view.jsp").forward(request, response);
        } else {
            List<Genre> list = genreServices.getAllGenres();
            request.setAttribute("genre_list", list);
            request.getRequestDispatcher("/views/genre-view.jsp").forward(request, response);
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
        String genreName = request.getParameter("genreName");
        String description = request.getParameter("description");
        int result = genreServices.insertGenre(genreName, description);
        switch (result) {
            case 0:
                request.setAttribute("result", "success");
                break;
            case 1:
                request.setAttribute("result", "false");
                break;
            case 2:
                request.setAttribute("result", "genre is aready exist");
                break;
            case 3:
                request.setAttribute("errorName", "Name is empty");
                request.setAttribute("result", "false");
                break;
            case 4:
                request.setAttribute("errorDes", "Description is empty");
                request.setAttribute("result", "false");
                break;
            case 5:
                request.setAttribute("errorName", "Name is empty");
                request.setAttribute("errorDes", "Description is empty");
                request.setAttribute("result", "false");
                break;
            default:
                break;
        }
        request.getRequestDispatcher("/views/createGenre-view.jsp").forward(request, response);
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
