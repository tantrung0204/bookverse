/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Supplier;
import com.mycompany.bookverse.service.SupplierService;
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
@WebServlet(name = "SupplierController", urlPatterns = {"/supplier"})
public class SupplierController extends HttpServlet {

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
        List<Supplier> list = supplierService.getAllSuppliers();

        request.setAttribute("suppliers", list);
        request.setAttribute("contentPage", "supplier-list.jsp");
        request.setAttribute("activeMenu", "supplier");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    private void getSearchSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");

       List <Supplier> list = supplierService.getSearchByName(keyword);

       if (list == null || list.isEmpty()){
           request.setAttribute("message", "No supplier found");
       } else {
           request.setAttribute("suppliers", list);
       }

        request.setAttribute("keyword", keyword);
        request.setAttribute("contentPage", "supplier-list.jsp");
        request.setAttribute("activeMenu", "supplier");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);

    }

}
