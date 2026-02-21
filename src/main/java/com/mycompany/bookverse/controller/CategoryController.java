/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Category;
import com.mycompany.bookverse.service.CategoryService;
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
        String view = request.getParameter("view");
        if (view == null) {
            view = "list";
        }

        switch (view) {
            case "list":
                getListCategories(request, response);
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

    private void getListCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<Category> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = categoryService.getsearchByName(keyword);

            if (list == null || list.isEmpty()) {
                request.setAttribute("message", "No category found for: " + keyword);
            }
            request.setAttribute("keyword", keyword);
        } else {
            list = categoryService.getAllCategories();
        }
        request.setAttribute("categories", list);
        request.setAttribute("contentPage", "category-list.jsp");
        request.setAttribute("activeMenu", "category");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    private void handleCreateAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("categoryName");
        String desc = request.getParameter("descriptionText");
        String statusRaw = request.getParameter("status");

        boolean hasError = false;

        int status = 1;
        if (statusRaw != null && !statusRaw.isEmpty()) {
            status = Integer.parseInt(statusRaw);
        }

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("createError", "Category name must not be empty");
            hasError = true;
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("createError", "Category name contains invalid characters");
            hasError = true;
        }
        if (desc == null || desc.trim().isEmpty()) {
            request.setAttribute("createError", "Description must not be empty");
            hasError = true;
        } else if (!desc.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("createError", "Description contains invalid characters");
            hasError = true;
        }
        if (categoryService.existCategoryName(name.trim())) {
            request.setAttribute("createError", "Category already exists");
            hasError = true;
        }
        if (hasError) {
            request.setAttribute("openCreatePopup", true);
            request.setAttribute("createName", name);
            request.setAttribute("createDesc", desc);
            request.setAttribute("createStatus", status);

            request.setAttribute("categories", categoryService.getAllCategories());
            request.setAttribute("contentPage", "category-list.jsp");
            request.setAttribute("activeMenu", "category");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
            return;
        }

        Category category = new Category();
        category.setCategoryName(name);
        category.setDescriptionText(desc);
        category.setStatus(status);
        categoryService.createCategory(category);

        request.getSession().removeAttribute("errorMsg");
        request.getSession().setAttribute("successMsg", "Category created successfully");
        response.sendRedirect(request.getContextPath() + "/category");
    }

    public void handleEditAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("categoryName");
        String desc = request.getParameter("descriptionText");
        String statusRaw = request.getParameter("status");

        boolean hasError = false;

        int status = 1;
        if (statusRaw != null && !statusRaw.isEmpty()) {
            status = Integer.parseInt(statusRaw);
        }

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("editError", "Category name must not be empty");
            hasError = true;
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("editError", "Category name contains invalid characters");
            hasError = true;
        }

        if (desc == null || desc.trim().isEmpty()) {
            request.setAttribute("editError", "Description must not be empty");
            hasError = true;
        } else if (!desc.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("editError", "Description contains invalid characters");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("openEditPopup", true);
            request.setAttribute("editId", id);
            request.setAttribute("editName", name);
            request.setAttribute("editDesc", desc);
            request.setAttribute("editStatus", statusRaw);

            request.setAttribute("categories", categoryService.getAllCategories());
            request.setAttribute("contentPage", "category-list.jsp");
            request.setAttribute("activeMenu", "category");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);
            return;
        }
        if (categoryService.existCategory(name.trim(), id)) {
            request.setAttribute("editError", "Category already exists");
            request.setAttribute("openEditPopup", true);
            request.setAttribute("editId", id);
            request.setAttribute("editName", name);
            request.setAttribute("editDesc", desc);
            request.setAttribute("editStatus", status);

            request.setAttribute("categories", categoryService.getAllCategories());
            request.setAttribute("contentPage", "category-list.jsp");
            request.setAttribute("activeMenu", "category");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);
            return;
        }

        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        category.setDescriptionText(desc);
        category.setStatus(status);

        categoryService.editCategory(category);

        request.getSession().removeAttribute("errorMsg");
        request.getSession().setAttribute("successMsg", "Category edied successfully");
        response.sendRedirect(request.getContextPath() + "/category");

    }

    private void handleDeleteAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("category");
            return;
        }

        int id = Integer.parseInt(idParam);

        boolean success = categoryService.deleteCategory(id);

        if (!success) {
            request.getSession().setAttribute("errorMsg", "Cannot delete this category because it is currently in use");
        } else {
            request.getSession().removeAttribute("errorMsg");
            request.getSession().setAttribute("successMsg", "Category deteled successfully");
        }
        response.sendRedirect("category");
    }

}
