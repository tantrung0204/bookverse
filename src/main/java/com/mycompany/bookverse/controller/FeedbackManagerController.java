/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.mycompany.bookverse.model.Feedback;
import com.mycompany.bookverse.service.FeedbackService;
import com.mycompany.bookverse.utils.PaginationConfig;
import jakarta.servlet.http.*;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "FeedbackManagerController", urlPatterns = {"/feedback"})
public class FeedbackManagerController extends HttpServlet {

    private FeedbackService service = new FeedbackService();

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
            out.println("<title>Servlet FeedbackManagerController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackManagerController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        int page = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;

        switch (action) {

            case "search":

                String keyword = request.getParameter("keyword");
                String ratingStr = request.getParameter("rating");

                Integer rating = null;

                if (ratingStr != null && !ratingStr.isEmpty()) {
                    rating = Integer.parseInt(ratingStr);
                }

                List<Feedback> searchList = service.search(keyword, rating, page, pageSize);

                int totalSearchPages = service.getSearchPages(keyword, rating, pageSize);

                request.setAttribute("feedbacks", searchList);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalSearchPages);
                request.setAttribute("keyword", keyword);
                request.setAttribute("rating", rating);

                break;

            default:

                List<Feedback> list = service.getByPage(page, pageSize);

                int totalPages = service.getTotalPages(pageSize);

                request.setAttribute("feedbacks", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);

        }

        request.setAttribute("contentPage", "feedback-list.jsp");
        request.setAttribute("activeMenu", "feedback");

        request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                .forward(request, response);

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

        if ("delete".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));

            String msg = service.delete(id);

            request.getSession().setAttribute("successMessage", msg);

            response.sendRedirect(request.getContextPath() + "/feedback");
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
