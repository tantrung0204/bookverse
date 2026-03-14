/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.service.CartService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.service.CategoryService;
import java.math.BigDecimal;

/**
 *
 * @author TrungNT - CE200064
 */
@WebServlet(name = "CartController", urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    private CartService cartService = new CartService();
    private CategoryService categoryService = new CategoryService();

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
            out.println("<title>Servlet CartController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
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
                viewCart(request, response);
                break;
            case "buy": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    HttpSession session = request.getSession();
                    session.setAttribute("buyNowProductId", productId);
                    session.setAttribute("buyNowQuantity", quantity);

                    response.sendRedirect(request.getContextPath() + "/checkout?mode=buyNow");
                    break;
            }
            default:
                viewCart(request, response);
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
            response.sendRedirect("cart");
            return;
        }

        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "update":
                updateQuantity(request, response);
                break;
            case "delete":
                deleteFromCart(request, response);
                break;
            default:
                response.sendRedirect("cart");
                break;
        }
    }

    private int getAuthenticatedCustomerId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Object userObj = session.getAttribute("user");

        if ("customer".equals(role) && userObj instanceof Customer) {
            Customer customer = (Customer) userObj;
            return customer.getCustomerId();
        }
        return -1;
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int customerId = getAuthenticatedCustomerId(request);
        if (customerId == -1) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        List<Cart> cartItems = cartService.getCustomerCart(customerId);
        BigDecimal cartTotal = cartService.calculateCartTotal(cartItems);
        List<Category> categories = categoryService.getActiveSubCategories();

        request.setAttribute(
                "cartList", cartItems);
        request.setAttribute(
                "cartTotal", cartTotal);
        request.setAttribute(
                "categories", categories);
        request.getRequestDispatcher(
                "/views/customer/cart.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String referer = request.getHeader("referer");

        int customerId = getAuthenticatedCustomerId(request);
        if (customerId == -1) {
            session.setAttribute("errorMessage", "Please login as a customer to add items to your cart.");
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            String quantityRaw = request.getParameter("quantity");
            int quantity = (quantityRaw == null || quantityRaw.isEmpty()) ? 1 : Integer.parseInt(quantityRaw);

            cartService.addToCart(customerId, productId, quantity);

            session.setAttribute("cartMessage", "Product added to cart successfully!");
            session.setAttribute("messageType", "success");

        } catch (NumberFormatException e) {
            session.setAttribute("cartMessage", "Failed to add product! Invalid quantity.");
            session.setAttribute("messageType", "error");
        } catch (Exception e) {
            session.setAttribute("cartMessage", e.getMessage());
            session.setAttribute("messageType", "error");
        }

        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect("cart");
        }
    }

    private void updateQuantity(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int customerId = getAuthenticatedCustomerId(request);
        if (customerId == -1) {
            out.print("{\"status\": \"error\", \"message\": \"Session expired or unauthorized access. Please login again.\"}");
            out.flush();
            return;
        }

        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int newQuantity = Integer.parseInt(request.getParameter("quantity"));

            try {
                cartService.updateCartQuantity(cartId, newQuantity);
            } catch (Exception ex) {
                // NẾU LỖI DO VƯỢT QUÁ KHO -> Trả về JSON báo lỗi
                out.print("{");
                out.print("\"status\": \"error\",");
                out.print("\"message\": \"The quantity exceeds the available stock!\"");
                out.print("}");
                out.flush();
                return;
            }

            List<Cart> cartItems = cartService.getCustomerCart(customerId);
            BigDecimal grandTotal = cartService.calculateCartTotal(cartItems);

            BigDecimal itemTotal = BigDecimal.ZERO;
            int totalItems = 0;
            for (Cart c : cartItems) {
                Product p = c.getProductId();
                if (p.getStatus() != null && p.getStatus() == 1 && p.getStockQuantity() != null
                        && p.getStockQuantity() > 0) {
                    totalItems += c.getCartQuantity();
                }
                if (c.getCartId() == cartId) {
                    itemTotal = p.getPrice().multiply(new BigDecimal(newQuantity));
                }
            }

            // Trả về JSON thành công: {"status":"success", "itemTotal": 100000,
            // "grandTotal": 500000}
            out.print("{");
            out.print("\"status\": \"success\",");
            out.print("\"itemTotal\": " + itemTotal + ",");
            out.print("\"grandTotal\": " + grandTotal);
            out.print("}");
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteFromCart(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        int customerId = getAuthenticatedCustomerId(request);
        if (customerId == -1) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }
        try {
            int cartId = Integer.parseInt(request.getParameter("cartId"));

            boolean isRemoved = cartService.removeCartItem(cartId, customerId);

            if (isRemoved) {
                session.setAttribute("cartMessage", "Item removed from cart successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("cartMessage", "Action denied! You don't have permission to remove this item.");
                session.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("cartMessage", "An error occurred while processing your request.");
            session.setAttribute("messageType", "error");
        }

        // 4. Load lại trang giỏ hàng
        response.sendRedirect("cart");
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
