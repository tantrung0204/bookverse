/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.CustomerNotification;
import com.mycompany.bookverse.model.Notification;
import com.mycompany.bookverse.service.NotificationService;
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
 * @author NganTTK-CE190411
 */
@WebServlet(name = "NotificationCustomerController", urlPatterns = {"/notification/customer"})
public class NotificationCustomerController extends HttpServlet {
    
    private NotificationService notificationService = new NotificationService();

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
                getListCusNotificaton(request, response);
                break;
            case "detail":
                getDetailCusNotification(request, response);
                break;
            default:
                throw new AssertionError();
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

    private void getListCusNotificaton(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }
        
        Object user = session.getAttribute("user");
        
        if (user == null || !(user instanceof Customer)) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }
        
        Customer customer = (Customer) user;
        
        int customerId = customer.getCustomerId();
        
        int page = 1;
        
        String pageParam = request.getParameter("page");
        
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        if (page < 1) {
            page = 1;
        }
        
        List<CustomerNotification> list = notificationService.getCustomerNotifications(customer.getCustomerId(), page, PaginationConfig.MAX_PAGE_NODES);
        System.out.println("Notification size: " + list.size());
        request.setAttribute("notifications", list);
        request.getRequestDispatcher("/views/customer/notification-customer.jsp").forward(request, response);
        
    }
    
    private void getDetailCusNotification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        notificationService.markAsRead(id);
        
        CustomerNotification cn = notificationService.getCustomerNotificationById(id);

        // lấy Notification thật
        Notification notification = cn.getNotificationId();

        request.setAttribute("notification", notification);
        
        request.getRequestDispatcher("/views/customer/cus-notification-detail.jsp").forward(request, response);
    }
    
}
