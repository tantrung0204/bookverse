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
            case "view_detail":
                getViewDetail(request, response);
                break;
            case "create":
                getCreateCategory(request, response);
                break;
//            case "delete":
//                getDeleteCategory(request, response);
//                break;
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
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    private void getViewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("category");
            return;
        }

        int categoryId = Integer.parseInt(idParam);
        Category category = categoryService.getCategoryById(categoryId);

        request.setAttribute("view_detail", category);
        request.getRequestDispatcher("/views/dashboard/view_detail_category.jsp")
                .forward(request, response);
    }

    private void getCreateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/dashboard/create_category.jsp").forward(request, response);
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
            request.setAttribute("nameError", "Thể loại không được để trống");
            hasError = true;
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("nameError", "Thể loại chứa các kí tự không hợp lệ");
            hasError = true;
        }
        if (desc == null || desc.trim().isEmpty()) {
            request.setAttribute("descError", "Mô tả không được để trống");
            hasError = true;
        } else if (!desc.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("descError", "Mô tả chứa các kí tự không hợp lệ");
            hasError = true;
        }
        if (categoryService.existCategoryName(name.trim())) {
            request.setAttribute("nameError", "Thể loại đã tồn tại");
            hasError = true;
        }
        if (hasError) {
            request.setAttribute("CategoryName", name);
            request.setAttribute("descriptionText", desc);
            request.setAttribute("status", status);

            request.getRequestDispatcher("/views/dashboard/create_category.jsp").forward(request, response);
            return;
        }

        Category category = new Category();
        category.setCategoryName(name);
        category.setDescriptionText(desc);
        category.setStatus(status);

        categoryService.createCategory(category);

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
            request.setAttribute("editError", "Thể loại không được để trống");
            hasError = true;
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("editError", "Thể loại chứa các kí tự không hợp lệ");
            hasError = true;
        }

        if (desc == null || desc.trim().isEmpty()) {
            request.setAttribute("editError", "Mô tả không được để trống");
            hasError = true;
        } else if (!desc.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            request.setAttribute("editError", "Mô tả chứa các kí tự không hợp lệ");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("openEditPopup", true);
            request.setAttribute("editId", id);
            request.setAttribute("editName", name);
            request.setAttribute("editDesc", desc);
            request.setAttribute("editStatus", statusRaw);
            request.setAttribute("category_list", categoryService.getAllCategories());
            request.getRequestDispatcher("/views/dashboard/category-list.jsp").forward(request, response);
            return;
        }
        if (categoryService.existCategory(name.trim(), id)) {
            request.setAttribute("editError", "Thể loại đã tồn tại");
            request.setAttribute("openEditPopup", true);
            request.setAttribute("editId", id);
            request.setAttribute("editName", name);
            request.setAttribute("editDesc", desc);
            request.setAttribute("editStatus", statusRaw);

            request.setAttribute("category_list", categoryService.getAllCategories());
            request.getRequestDispatcher("/views/dashboard/category-list.jsp").forward(request, response);
            return;
        }

        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        category.setDescriptionText(desc);
        category.setStatus(status);

        categoryService.editCategory(category);
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
            request.getSession().setAttribute(
                    "deleteError",
                    "Không thể xóa thể loại vì đang được sử dụng"
            );
        }

        response.sendRedirect("category");
    }

}
