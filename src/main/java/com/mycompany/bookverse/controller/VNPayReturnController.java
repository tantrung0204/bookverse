/*
 * VNPayReturnController - Handles VNPay callback after payment
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.service.OrderService;
import com.mycompany.bookverse.utils.VNPayConfig;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author TrungNT - CE200064
 */
@WebServlet(name = "VNPayReturnController", urlPatterns = {"/vnpay-return"})
public class VNPayReturnController extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Collect all params
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            if (value != null && !value.isEmpty()) {
                params.put(name, value);
            }
        }

        // Verify signature
        boolean isValid = VNPayConfig.validateSignature(params);

        if (isValid) {
            String responseCode = params.get("vnp_ResponseCode");
            String txnRef = params.get("vnp_TxnRef");

            int orderId = VNPayConfig.extractOrderId(txnRef);

            if ("00".equals(responseCode)) {
                // Payment successful
                orderService.updatePaymentStatus(orderId, true);
                Order order = orderService.getOrderById(orderId);
                session.setAttribute("orderSuccess", order);
                session.setAttribute("paymentMessage", "Payment successful via VNPay!");
                response.sendRedirect(request.getContextPath()
                        + "/views/customer/order-success.jsp");
            } else {
                // Payment failed
                session.setAttribute("paymentError",
                        "Payment failed. Response code: " + responseCode);
                session.setAttribute("failedOrderId", orderId);
                response.sendRedirect(request.getContextPath()
                        + "/views/customer/order-success.jsp");
            }
        } else {
            // Invalid signature
            session.setAttribute("paymentError", "Invalid payment signature. Please contact support.");
            response.sendRedirect(request.getContextPath()
                    + "/views/customer/order-success.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "VNPay Return Controller";
    }
}
