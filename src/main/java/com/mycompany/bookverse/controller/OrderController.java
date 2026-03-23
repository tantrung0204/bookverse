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
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.OrderItem;
import com.mycompany.bookverse.service.OrderService;
import com.mycompany.bookverse.utils.VNPayConfig;
import jakarta.servlet.http.*;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CustomerOrderController", urlPatterns = { "/customer-order" })
public class OrderController extends HttpServlet {

    private OrderService orderService = new OrderService();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet OrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {

            case "detail":
                viewDetail(request, response);
                break;

            case "list":
            default:
                listOrders(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {

            case "cancel":
                cancelOrder(request, response);
                break;

            case "confirm":
                confirmOrder(request, response);
                break;

            case "retryPayment":
                retryPayment(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/customer-order?action=list");
        }
    }

    /* ================= LIST ORDER ================= */
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Customer customer = (Customer) session.getAttribute("user");

        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/views/public/signin.jsp");
            return;
        }

        int customerId = customer.getCustomerId();

        List<Order> orders = orderService.getOrdersByCustomer(customerId);

        request.setAttribute("openCustomerProfile", "yes");
        request.setAttribute("openOrderHistory", "yes");
        request.setAttribute("activeMenu", "orderHistory");
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/views/public/profile.jsp")
                .forward(request, response);
    }

    /* ================= ORDER DETAIL ================= */
    private void viewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("id"));

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        Order order = orderService.getOrder(orderId);
        List<OrderItem> items = orderService.getOrderItems(orderId);

        if (customer != null) {
            for (OrderItem item : items) {

                boolean reviewed = orderService.isReviewed(
                        customer.getCustomerId(),
                        item.getProductId().getProductId());

                item.setReviewed(reviewed);
            }
        }

        request.setAttribute("order", order);
        request.setAttribute("items", items);

        request.getRequestDispatcher("/views/customer/order-detail.jsp")
                .forward(request, response);
    }

    /* ================= CANCEL ORDER ================= */
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String error = orderService.cancelOrder(orderId);

        if (error != null) {
            request.getSession().setAttribute("orderError", error);
        }

        response.sendRedirect(request.getContextPath() + "/customer-order?action=list");
    }

    /* ================= CONFIRM RECEIVED ================= */
    private void confirmOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        orderService.confirmReceived(orderId);

        response.sendRedirect(request.getContextPath() + "/customer-order?action=list");
    }

    /* ================= RETRY PAYMENT (ONLINE unpaid) ================= */
    private void retryPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.getOrderById(orderId);

        if (order == null || !"ONLINE".equalsIgnoreCase(order.getPaymentMethod())
                || (order.getIsPaid() != null && order.getIsPaid())
                || !"Pending".equalsIgnoreCase(order.getOrderStatus())) {
            session.setAttribute("orderError", "This order cannot be paid online.");
            response.sendRedirect(request.getContextPath() + "/customer-order?action=list");
            return;
        }

        try {
            String baseUrl = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath();
            String returnUrl = baseUrl + VNPayConfig.VNP_RETURN_URL;
            String ipAddress = VNPayConfig.getIpAddress(request);

            String paymentUrl = VNPayConfig.createPaymentUrl(
                    order.getOrderId(),
                    order.getTotalAmount(),
                    "BookVerse Order #" + order.getOrderId(),
                    ipAddress,
                    returnUrl);

            response.sendRedirect(paymentUrl);
        } catch (Exception e) {
            session.setAttribute("orderError", "Failed to create payment: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer-order?action=list");
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
