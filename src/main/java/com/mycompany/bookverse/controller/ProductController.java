/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.dao.StationeryDAO;
import com.mycompany.bookverse.model.Author;
import com.mycompany.bookverse.model.Product;
import com.mycompany.bookverse.model.Book;
import com.mycompany.bookverse.model.Stationery;
import com.mycompany.bookverse.model.Stationery;
import com.mycompany.bookverse.service.BookService;
import com.mycompany.bookverse.service.ProductService;
import com.mycompany.bookverse.service.StationeryService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
@WebServlet(name = "ProductController", urlPatterns = {"/product"})
public class ProductController extends HttpServlet {

    private ProductService productService = new ProductService();
    private BookService bookService = new BookService();
    private StationeryService stationeryService = new StationeryService();

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
                getListProducts(request, response);
                break;
            case "search":
                getSearchProduct(request, response);
                break;
            case "detail":
                getDetailProduct(request, response);
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
        String action = request.getParameter("action");
        if (action == null) {
            getListProducts(request, response);
            return;
        }

        switch (action) {
            case "create":
                handleCreateAction(request, response);
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

    private void getListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        List<Product> list;

        if (type == null || type.equals("book")) {
            list = productService.getAllBooks();
            request.setAttribute("type", "book");
        } else {
            list = productService.getAllStationery();
            request.setAttribute("type", "stationery");
        }

        request.setAttribute("products", list);
        request.setAttribute("type", type);
        request.setAttribute("contentPage", "product-list.jsp");
        request.setAttribute("activeMenu", "product");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    private void handleCreateAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        if (type == null) {
            type = "book"; // default
        }

        // ================= PRODUCT CHUNG =================
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setPrice(new BigDecimal(request.getParameter("price")));
        product.setStockQuantity(Integer.parseInt(request.getParameter("stock")));
        product.setStatus(1);
        product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));

        // insert product trước
        Product savedProduct = productService.create(product);

        // ================= THEO TYPE =================
        if ("book".equals(type)) {
            bookService.createBook(
                    savedProduct.getProductId(),
                    request.getParameter("isbn"),
                    request.getParameter("publisher"),
                    Integer.parseInt(request.getParameter("publishedYear"))
            );
        } else if ("stationery".equals(type)) {
            stationeryService.createStationery(
                    savedProduct.getProductId(),
                    request.getParameter("color"),
                    request.getParameter("material")
            );
        }

        // quay về list đúng type
        response.sendRedirect(request.getContextPath()
                + "/product?action=list&type=" + type);
    }

    private void getSearchProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

        List<Product> searchList = productService.searchProducts(keyword);

        if (searchList == null || searchList.isEmpty()) {
            request.setAttribute("message", "No category found");
        } else {
            request.setAttribute("products", searchList);
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("contentPage", "product-list.jsp");
        request.setAttribute("activeMenu", "product");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);

    }

   
    private void getDetailProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int productId = Integer.parseInt(request.getParameter("productId"));

        Product p = productService.findById(productId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (p == null) {
            response.getWriter().print("{}");
            return;
        }

        StringBuilder json = new StringBuilder("{");

        json.append("\"productId\":").append(p.getProductId()).append(",");
        json.append("\"name\":\"").append(p.getName()).append("\",");
        json.append("\"price\":").append(p.getPrice()).append(",");
        json.append("\"imageUrl\":\"").append(p.getImageUrl() == null ? "" : p.getImageUrl()).append("\",");
        json.append("\"stockQuantity\":").append(p.getStockQuantity()).append(",");
        json.append("\"averageRating\":").append(p.getAverageRating()).append(",");
        json.append("\"soldQuantity\":").append(p.getSoldQuantity()).append(",");
        json.append("\"category\":\"").append(p.getCategoryName()).append("\",");
        json.append("\"type\":\"").append(p.getType()).append("\"");

        // ===== BOOK =====
        if ("Book".equals(p.getType())) {
            Book b = bookService.findByProductId(productId);

            json.append(",\"isbn\":\"").append(b.getIsbn()).append("\",");
            json.append("\"publisher\":\"").append(b.getPublisher()).append("\",");
            json.append("\"publishedYear\":").append(b.getPublishedYear()).append(",");
            json.append("\"genre\":\"").append(b.getGenreName()).append("\",");

            json.append("\"authors\":[");
            int i = 0;
            for (Author a : b.getAuthorCollection()) {
                json.append("\"").append(a.getAuthorName()).append("\"");
                if (++i < b.getAuthorCollection().size()) {
                    json.append(",");
                }
            }
            json.append("]");
        }

        // ===== STATIONERY =====
        if ("Stationery".equals(p.getType())) {
            Stationery s = stationeryService.findByProductId(productId);
            json.append(",\"color\":\"").append(s.getColor()).append("\",");
            json.append("\"material\":\"").append(s.getMaterial()).append("\"");
        }

        json.append("}");
        response.getWriter().print(json.toString());
    }
    
}
