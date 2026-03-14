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
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.Feedback;
import com.mycompany.bookverse.service.FeedbackService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "FeedbackController", urlPatterns = {"/feedback"})
@MultipartConfig
public class FeedbackController extends HttpServlet {

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
            out.println("<title>Servlet FeedbackController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackController at " + request.getContextPath() + "</h1>");
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
            action = "my";
        }

        switch (action) {

            case "edit":
                showEdit(request, response);
                break;

            case "delete":
                deleteFeedback(request, response);
                break;

            default:
                myFeedback(request, response);
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
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.getWriter().write("no_action");
            return;
        }

        switch (action) {

            case "create":
                createFeedback(request, response);
                break;

            case "update":
                updateFeedback(request, response);
                break;

            default:
                response.getWriter().write("invalid_action");
        }
    }

    /* ================= MY FEEDBACK ================= */
    private void myFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        List<Feedback> list = service.getByCustomer(customer.getCustomerId());

        request.setAttribute("feedbacks", list);

        request.getRequestDispatcher("/views/customer/own-feedback.jsp")
                .forward(request, response);
    }

    /* ================= SHOW EDIT ================= */
    private void showEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Feedback f = service.getByPage(1, 1)
                .stream()
                .filter(x -> x.getFeedbackId() == id)
                .findFirst()
                .orElse(null);

        request.setAttribute("feedback", f);

        request.getRequestDispatcher("/views/customer/edit-feedback.jsp")
                .forward(request, response);
    }

    /* ================= CREATE ================= */
    private void createFeedback(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");

        if (user == null) {
            response.getWriter().write("not_customer");
            return;
        }

        Customer customer = (Customer) user;

        try {

            String pid = request.getParameter("productId");

            if (pid == null || pid.isEmpty()) {
                response.getWriter().write("productId_missing");
                return;
            }

            int productId = Integer.parseInt(pid);
            int rating = Integer.parseInt(request.getParameter("rating"));
            String content = request.getParameter("content");

            service.create(customer.getCustomerId(), productId, rating, content);

            response.getWriter().write("success");

        } catch (Exception e) {

            response.setContentType("text/plain");

            e.printStackTrace(response.getWriter());

        }
    }

    /* ================= UPDATE ================= */
    private void updateFeedback(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        try {

            int id = Integer.parseInt(request.getParameter("id"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String content = request.getParameter("content");

            service.update(id, rating, content);

            response.getWriter().write("success");

        } catch (Exception e) {

            response.getWriter().write("error");

        }
    }

    /* ================= DELETE ================= */
    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        Customer customer = (Customer) session.getAttribute("user");

        int id = Integer.parseInt(request.getParameter("id"));

        service.deleteForCustomer(id, customer.getCustomerId());

        response.sendRedirect("feedback?action=my");
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
