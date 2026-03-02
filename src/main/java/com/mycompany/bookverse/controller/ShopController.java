/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.service.CategoryService;
import com.mycompany.bookverse.service.ProductService;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
@WebServlet(name = "ShopController", urlPatterns = {"/shop"})
public class ShopController extends HttpServlet {

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
            out.println("<title>Servlet ShopController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShopController at " + request.getContextPath() + "</h1>");
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

        // Lấy các tham số từ URL
        String type = request.getParameter("type");
        if (type == null) {
            type = "book";
        }

        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        String sort = request.getParameter("sort");

        // Xử lý mảng Genre IDs
        String[] genreParams = request.getParameterValues("genre");
        List<Integer> genreIds = new ArrayList<>();
        if (genreParams != null) {
            for (String g : genreParams) {
                try {
                    genreIds.add(Integer.parseInt(g));
                } catch (NumberFormatException e) {
                }
            }
        }

        String keyword = request.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
        }

        // Lấy danh sách sản phẩm
        List<? extends Product> productList = productService.getFilteredProducts(type, genreIds, sort, keyword, page);
        // Lấy tổng số lượng để tính phân trang
        long totalItems = productService.getTotalCount(type, genreIds, keyword);
        // Tính toán tổng số trang
        int pageSize = PaginationConfig.HOMEPAGE_ITEMS_PER_PAGE;
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Lấy danh sách Genre cho filter
        List<Genre> allGenres = productService.getAllGenres();
        List<Category> categories = categoryService.getAllCategories();

        // Đẩy dữ liệu sang JSP
        request.setAttribute("categories", categories);
        request.setAttribute("productList", productList);
        request.setAttribute("totalProducts", totalItems);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("allGenres", allGenres);

        // Giữ lại trạng thái bộ lọc
        request.setAttribute("selectedType", type);
        request.setAttribute("selectedSort", sort);
        request.setAttribute("selectedGenres", genreIds);
        request.setAttribute("searchKeyword", keyword);

        request.getRequestDispatcher("/views/public/shop.jsp").forward(request, response);
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
