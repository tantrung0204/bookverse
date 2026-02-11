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
            case "add":
                getAddCategory(request, response);
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

    }

    private void getListCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> list = categoryService.getAllcategorys();
        request.setAttribute("category_list", list);
        request.getRequestDispatcher("/views/category_list.jsp").forward(request, response);
    }

    private void getViewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Category category = categoryService.getCategoryById(categoryId);
        request.setAttribute("view_detail", category);
        request.getRequestDispatcher("/views/view_detail_category.jsp").forward(request, response);
    }

    private void getAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
