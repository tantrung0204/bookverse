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
import com.mycompany.bookverse.model.Notification;
import com.mycompany.bookverse.service.NotificationService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
public class NotificationController extends HttpServlet {

    private NotificationService service = new NotificationService();

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
            out.println("<title>Servlet NotificationController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NotificationController at " + request.getContextPath() + "</h1>");
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

        switch (action) {

            case "list":
                request.setAttribute("notifications", service.getAll());
                request.getRequestDispatcher("/views/notification-list.jsp")
                        .forward(request, response);
                break;

            case "detail":
                int id = Integer.parseInt(request.getParameter("id"));
                Notification n = service.getById(id);
                request.setAttribute("notification", n);
                request.getRequestDispatcher("/views/notification-detail.jsp")
                        .forward(request, response);
                break;

            case "search":
                String keyword = request.getParameter("keyword");
                request.setAttribute("notifications", service.search(keyword));
                request.getRequestDispatcher("/views/notification-list.jsp")
                        .forward(request, response);
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

        switch (action) {

            case "create":
                Notification nCreate = new Notification();
                nCreate.setTitle(request.getParameter("title"));
                nCreate.setContentText(request.getParameter("content"));
                nCreate.setImageUrl(request.getParameter("image"));

                String msgCreate = service.create(nCreate);

                if (!msgCreate.contains("successfully")) {
                    request.setAttribute("createError", msgCreate);
                    request.setAttribute("openCreate", true);
                    request.setAttribute("notifications", service.getAll());
                    request.getRequestDispatcher("/views/notification-list.jsp")
                            .forward(request, response);
                    return;
                }

                request.getSession().setAttribute("successMessage", msgCreate);
                response.sendRedirect(request.getContextPath() + "/notification");
                break;

            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                String page = request.getParameter("page");

                Notification nEdit = new Notification();
                nEdit.setNotificationId(id);
                nEdit.setTitle(request.getParameter("title"));
                nEdit.setContentText(request.getParameter("content"));
                nEdit.setImageUrl(request.getParameter("image"));

                String msgEdit = service.update(nEdit);
                boolean isListPage = page.equals("listPage");

                if (!msgEdit.contains("successfully")) {

                    request.setAttribute("editError", msgEdit);
                    request.setAttribute("openEdit", true);

                    if (isListPage) {
                        request.setAttribute("notifications", service.getAll());
                        request.getRequestDispatcher("/views/notification-list.jsp")
                                .forward(request, response);
                    } else {
                        request.setAttribute("notification", service.getById(id));
                        request.getRequestDispatcher("/views/notification-detail.jsp")
                                .forward(request, response);
                    }
                    return;
                }

                request.getSession().setAttribute("successMessage", msgEdit);

                if (isListPage) {
                    response.sendRedirect(request.getContextPath() + "/notification?action=list");
                } else {
                    response.sendRedirect(request.getContextPath() + "/notification?action=detail&id=" + id);
                }
                break;

            case "delete":
                int idDelete = Integer.parseInt(request.getParameter("id"));
                String msgDelete = service.delete(idDelete);

                request.getSession().setAttribute("successMessage", msgDelete);
                response.sendRedirect(request.getContextPath() + "/notification");
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
