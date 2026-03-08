/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.service.ProductService;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.service.CategoryService;
import com.mycompany.bookverse.utils.PaginationConfig;
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
 * @author TrungNT - CE200064
 */
@WebServlet(name = "ProductDetailController", urlPatterns = {"/product-detail"})
public class ProductDetailController extends HttpServlet {

    private ProductService productService = new ProductService();
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
            out.println("<title>Servlet ProductDetailController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailController at " + request.getContextPath() + "</h1>");
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
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                response.sendRedirect("home");
                return;
            }
            int productId = Integer.parseInt(idStr);

            String pageStr = request.getParameter("page");
            int page = 1;
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }

            Product product = productService.getProductDetail(productId);

            if (product == null) {
                response.sendRedirect("home");
                return;
            }

            String productType = "product";
            if (product instanceof Book) {
                productType = "book";
            } else if (product instanceof Stationery) {
                productType = "stationery";
            }

            List<Product> relatedProducts = productService.getRelatedProducts(product);

            // Gọi danh sách 5 Feedback hiển thị
            List<Feedback> feedbackList = productService.getProductFeedbacks(productId, page);
            List<Category> categories = categoryService.getActiveSubCategories();

            // Tính toán tổng số trang feedback
            int totalFeedbacks = product.getReviewCount();
            int totalFbPages = (int) Math.ceil((double) totalFeedbacks / PaginationConfig.FEEDBACK_ITEMS_PER_PAGE);

            // Gửi dữ liệu product
            request.setAttribute("product", product);
            request.setAttribute("productType", productType);
            request.setAttribute("relatedProducts", relatedProducts);
            request.setAttribute("categories", categories);

            // Gửi dữ liệu Feedback
            request.setAttribute("feedbackList", feedbackList);
            request.setAttribute("totalFbPages", totalFbPages);
            request.setAttribute("currentFbPage", page);

            request.getRequestDispatcher("/views/public/product-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
            return;
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
        processRequest(request, response);
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
