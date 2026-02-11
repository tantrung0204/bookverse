/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.mycompany.bookverse.model.Voucher;
import com.mycompany.bookverse.service.VoucherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "VoucherController", urlPatterns = {"/voucher"})
public class VoucherController extends HttpServlet {

    private VoucherService voucherService = new VoucherService();

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
            out.println("<title>Servlet VoucherController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VoucherController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "list":
                List<Voucher> vouchers = voucherService.getVouchers();
                if (vouchers == null || vouchers.isEmpty()) {
                    request.setAttribute("message", "No vouchers found");
                } else {
                    request.setAttribute("vouchers", vouchers);
                }
                request.getRequestDispatcher("/views/voucher-list.jsp")
                        .forward(request, response);
                break;
            case "detail":
                int id = Integer.parseInt(request.getParameter("id"));
                Voucher voucher = voucherService.getVoucherById(id);
                request.setAttribute("voucher", voucher);
                request.getRequestDispatcher("/views/voucher-detail.jsp")
                        .forward(request, response);
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Voucher> searchList = voucherService.searchVouchers(keyword);

                if (searchList == null || searchList.isEmpty()) {
                    request.setAttribute("message", "No voucher found");
                } else {
                    request.setAttribute("vouchers", searchList);
                }
                request.getRequestDispatcher("/views/voucher-list.jsp")
                        .forward(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/voucher");
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
            response.sendRedirect(request.getContextPath() + "/voucher");
            return;
        }
        switch (action) {
            case "create":
                Voucher vCreate = new Voucher();
                vCreate.setVoucherCode(request.getParameter("code"));
                vCreate.setDiscountPercent(new BigDecimal(request.getParameter("discount")));
                vCreate.setAvailableQuantity(Integer.parseInt(request.getParameter("quantity")));
                vCreate.setStatus(Integer.parseInt(request.getParameter("status")));
                vCreate.setStartDate(Date.valueOf(request.getParameter("startDate")));
                vCreate.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));
                String createMsg = voucherService.createVoucher(vCreate);
                request.getSession().setAttribute("message", createMsg);
                response.sendRedirect(request.getContextPath() + "/voucher");
                break;
            case "edit":
                Voucher vEdit = new Voucher();
                vEdit.setVoucherId(Integer.parseInt(request.getParameter("id")));
                vEdit.setVoucherCode(request.getParameter("code"));
                vEdit.setDiscountPercent(new BigDecimal(request.getParameter("discount")));
                vEdit.setAvailableQuantity(Integer.parseInt(request.getParameter("quantity")));
                vEdit.setStatus(Integer.parseInt(request.getParameter("status")));
                vEdit.setStartDate(Date.valueOf(request.getParameter("startDate")));
                vEdit.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));
                String editMsg = voucherService.updateVoucher(vEdit);
                request.getSession().setAttribute("message", editMsg);
                response.sendRedirect("voucher?action=detail&id=" + vEdit.getVoucherId());
                break;
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                String deleteMsg = voucherService.deleteVoucher(id);
                request.getSession().setAttribute("message", deleteMsg);
                response.sendRedirect(request.getContextPath() + "/voucher");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/voucher");
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

}
