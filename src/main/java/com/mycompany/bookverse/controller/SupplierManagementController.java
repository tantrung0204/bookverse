/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Supplier;
import com.mycompany.bookverse.service.SupplierService;
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
 * @author NganTTK-CE190411
 */
@WebServlet(name = "SupplierController", urlPatterns = {"/dashboard/supplier"})
public class SupplierManagementController extends HttpServlet {

    private SupplierService supplierService = new SupplierService();

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
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                getListSuppliers(request, response);
                break;
            case "search":
                getSearchSupplier(request, response);
                break;
            case "detail":
                getDetailSupplier(request, response);
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
            getListSuppliers(request, response);
            return;
        }

        switch (action) {
            case "create":
                handleCreateSupplier(request, response);
                break;
            case "edit":
                handleEditSupplier(request, response);
                break;
            case "delete":
                handleDeleteSupplier(request, response);
                break;
            default:
                throw new AssertionError();
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

    private void getListSuppliers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<Supplier> list = supplierService.getAllSuppliers(page);
        long totalPages = supplierService.getTotalPages();

        request.setAttribute("suppliers", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("contentPage", "supplier-list.jsp");
        request.setAttribute("activeMenu", "supplier");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    private void getSearchSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        int page = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<Supplier> list = supplierService.searchPaging(keyword, page);

        long totalPages = supplierService.getTotalSearchPages(keyword);

        if (list == null || list.isEmpty()) {
            request.setAttribute("message", "No supplier found");
        } else {
            request.setAttribute("suppliers", list);
        }

        request.setAttribute("keyword", keyword);
         request.setAttribute("currentPage", 1);
         request.setAttribute("totalPages", totalPages);
          request.setAttribute("maxPageNodes", PaginationConfig.MAX_PAGE_NODES);
         
        request.setAttribute("contentPage", "supplier-list.jsp");
        request.setAttribute("activeMenu", "supplier");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);

    }

    private void getDetailSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));

        response.setContentType("application/json");
    }

    private void handleCreateSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("supplierName");
        String email = request.getParameter("supplierEmail");
        String phone = request.getParameter("supplierPhone");
        String address = request.getParameter("supplierAddress");
        String statusRaw = request.getParameter("status");

        try {
            supplierService.createSupplier(name, email, phone, address, statusRaw);
            request.getSession().setAttribute("successMsg", "Create supplier successfully");
            response.sendRedirect(request.getContextPath() + "/dashboard/supplier");
        } catch (IllegalArgumentException e) {
            request.setAttribute("createError", e.getMessage());
            request.setAttribute("openCreatePopup", true);
            request.setAttribute("createName", name);
            request.setAttribute("createEmail", email);
            request.setAttribute("createPhone", phone);
            request.setAttribute("createAddress", address);
            request.setAttribute("createStatus", statusRaw);
            request.setAttribute("suppliers", supplierService.getAllSuppliers(1));
            request.setAttribute("contentPage", "supplier-list.jsp");
            request.setAttribute("activeMenu", "supplier");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);
        }

    }

    private void handleEditSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("supplierId");
        String name = request.getParameter("supplierName");
        String email = request.getParameter("supplierEmail");
        String phone = request.getParameter("supplierPhone");
        String address = request.getParameter("supplierAddress");
        String statusRaw = request.getParameter("status");

        try {

            supplierService.editSupplier(idRaw, name, email, phone, address, statusRaw);

            request.getSession().setAttribute("successMsg", "Edit supplier successfully");

            response.sendRedirect(request.getContextPath() + "/dashboard/supplier");
        } catch (IllegalArgumentException e) {

            request.setAttribute("editError", e.getMessage());
            request.setAttribute("openEditPopup", true);
            request.setAttribute("editId", idRaw);
            request.setAttribute("editName", name);
            request.setAttribute("editEmail", email);
            request.setAttribute("editPhone", phone);
            request.setAttribute("editAddress", address);
            request.setAttribute("editStatus", statusRaw);
            request.setAttribute("suppliers", supplierService.getAllSuppliers(1));
            request.setAttribute("contentPage", "supplier-list.jsp");
            request.setAttribute("activeMenu", "supplier");

            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);

        }
    }

    private void handleDeleteSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
         boolean success = supplierService.deleteSupplier(idParam);

        if (!success) {
            request.getSession().setAttribute("errorMsg",
                    "Cannot delete this supplier");
        } else {
            request.getSession().removeAttribute("errorMsg");
            request.getSession().setAttribute("successMsg",
                    "Delete supplier successfully");
        }

        response.sendRedirect("supplier");
    }

}
