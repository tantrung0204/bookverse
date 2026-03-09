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
import com.mycompany.bookverse.utils.JPAUtil;
import com.mycompany.bookverse.utils.PaginationConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
@MultipartConfig
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
                int page = 1;
                String pageParam = request.getParameter("page");

                if (pageParam != null) {
                    page = Integer.parseInt(pageParam);
                }

                int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                List<Notification> list = service.getByPage(page, pageSize);
                for (Notification n : list) {
                    n.setTotalSent(service.getTotalSent(n.getNotificationId()));
                    n.setTotalRead(service.getTotalRead(n.getNotificationId()));
                }
                int totalPages = service.getTotalPages(pageSize);

                request.setAttribute("notifications", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("contentPage", "notification-list.jsp");
                request.setAttribute("activeMenu", "notification");
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                        .forward(request, response);
                break;

            case "search":
                String keyword = request.getParameter("keyword");

                int pageSearch = 1;
                String pageParamSearch = request.getParameter("page");
                if (pageParamSearch != null) {
                    pageSearch = Integer.parseInt(pageParamSearch);
                }

                int pageSizeSearch = PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                List<Notification> searchList = service.searchByPage(keyword, pageSearch, pageSizeSearch);

                for (Notification n : searchList) {
                    n.setTotalSent(service.getTotalSent(n.getNotificationId()));
                    n.setTotalRead(service.getTotalRead(n.getNotificationId()));
                }

                int totalPagesSearch = service.getTotalSearchPages(keyword, pageSizeSearch);

                request.setAttribute("notifications", searchList);
                request.setAttribute("currentPage", pageSearch);
                request.setAttribute("totalPages", totalPagesSearch);
                request.setAttribute("keyword", keyword);
                request.setAttribute("contentPage", "notification-list.jsp");
                request.setAttribute("activeMenu", "notification");

                request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
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
                Part filePart = request.getPart("image");
                String fileName = filePart.getSubmittedFileName();

                String imagePath = null;

                if (fileName != null && !fileName.isEmpty()) {

                    String uploadPath = getServletContext().getRealPath("") + "uploads";
                    java.io.File uploadDir = new java.io.File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    filePart.write(uploadPath + java.io.File.separator + fileName);

                    imagePath = "uploads/" + fileName;
                }

                nCreate.setImageUrl(imagePath);

                String msgCreate = service.create(nCreate);

                if (!msgCreate.contains("successfully")) {
                    request.setAttribute("createError", msgCreate);
                    request.setAttribute("openCreate", true);
                    request.setAttribute("notifications", service.getAll());
                    request.setAttribute("contentPage", "notification-list.jsp");
                    request.setAttribute("activeMenu", "notification");
                    request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                            .forward(request, response);
                    return;
                }

                request.getSession().setAttribute("successMessage", msgCreate);
                response.sendRedirect(request.getContextPath() + "/notification");
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
