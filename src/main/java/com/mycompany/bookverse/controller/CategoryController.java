/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Category;
import com.mycompany.bookverse.service.CategoryService;
import com.mycompany.bookverse.service.ProductService;
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
 * @author NganTTK-CE190411
 */
@WebServlet(name = "CategoryController", urlPatterns = {"/category"})
public class CategoryController extends HttpServlet {

    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();

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
                getListCategories(request, response);
                break;
            case "detail":
                getDetailCategory(request, response);
                break;
            case "search":
                getSearchCategories(request, response);
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
            getListCategories(request, response);
            return;
        }

        switch (action) {
            case "create":
                handleCreateAction(request, response);
                break;
            case "edit":
                handleEditAction(request, response);
                break;
            case "delete":
                handleDeleteAction(request, response);
                break;
        }
    }

    private void getListCategories(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;

        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<Category> list = categoryService.getAllCategoriesPage(page);

        long totalPages = categoryService.getTotalPages();

        request.setAttribute("categories", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("contentPage", "category-list.jsp");
        request.setAttribute("activeMenu", "category");

        request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                .forward(request, response);
    }

    private void getSearchCategories(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        int page = 1;

        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<Category> list = categoryService.searchPaging(keyword, page);

        long totalPages = categoryService.getTotalSearchPages(keyword);

        request.setAttribute("categories", list);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", 1);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("contentPage", "category-list.jsp");
        request.setAttribute("activeMenu", "category");

        request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                .forward(request, response);
    }

    private void getDetailCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 1. Lấy categoryId từ request
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        // 2. Đếm số sản phẩm thuộc category
        long quantity = productService.countProductByCategoryId(categoryId);

        // 3. Trả JSON cho popup
        response.setContentType("application/json");
        response.getWriter().print("{\"quantity\": " + quantity + "}");
    }

    private void handleCreateAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("categoryName");
        String desc = request.getParameter("descriptionText");
        String statusRaw = request.getParameter("status");
        String parentRaw = request.getParameter("parent");

        try {

            categoryService.createCategory(name, desc, statusRaw, parentRaw);

            request.getSession().setAttribute("successMsg",
                    "Create category successfully");

            response.sendRedirect(request.getContextPath() + "/category");

        } catch (IllegalArgumentException e) {

            request.setAttribute("createError", e.getMessage());
            request.setAttribute("openCreatePopup", true);
            request.setAttribute("createName", name);
            request.setAttribute("createDesc", desc);
            request.setAttribute("createStatus", statusRaw);
            request.setAttribute("createParent", parentRaw);
            request.setAttribute("categories", categoryService.getAllCategoriesPage(1));
            request.setAttribute("contentPage", "category-list.jsp");
            request.setAttribute("activeMenu", "category");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);
        }
    }

    public void handleEditAction(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("categoryId");
        String name = request.getParameter("categoryName");
        String desc = request.getParameter("descriptionText");
        String statusRaw = request.getParameter("status");

        try {

            categoryService.editCategory(idRaw, name, desc, statusRaw);

            request.getSession().setAttribute("successMsg",
                    "Edit category successfully");

            response.sendRedirect(request.getContextPath() + "/category");

        } catch (IllegalArgumentException e) {

            request.setAttribute("editError", e.getMessage());
            request.setAttribute("openEditPopup", true);
            request.setAttribute("editId", idRaw);
            request.setAttribute("editName", name);
            request.setAttribute("editDesc", desc);
            request.setAttribute("editStatus", statusRaw);
            request.setAttribute("categories",categoryService.getAllCategoriesPage(1));
            request.setAttribute("contentPage", "category-list.jsp");
            request.setAttribute("activeMenu", "category");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);
        }
    }

    private void handleDeleteAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        boolean success = categoryService.deleteCategory(idParam);

        if (!success) {
            request.getSession().setAttribute("errorMsg",
                    "Cannot delete this category because it is currently in use");
        } else {
            request.getSession().removeAttribute("errorMsg");
            request.getSession().setAttribute("successMsg",
                    "Delete category successfully");
        }

        response.sendRedirect("category");
    }

}
