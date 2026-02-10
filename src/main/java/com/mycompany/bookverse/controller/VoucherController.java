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

        VoucherService voucherService = new VoucherService();

        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            // LIST
            List<Voucher> vouchers = voucherService.getVouchers();
            if (vouchers == null || vouchers.isEmpty()) {
                request.setAttribute("message", "No vouchers found");
                request.getRequestDispatcher("/view/voucher-list.jsp")
                        .forward(request, response);
            } else {
                request.setAttribute("vouchers", vouchers);
                request.getRequestDispatcher("/views/voucher-list.jsp")
                        .forward(request, response);
            }

        } else if (action.equals("detail")) {
            // DETAIL
            int id = Integer.parseInt(request.getParameter("id"));
            Voucher voucher = voucherService.getVoucherById(id);

            request.setAttribute("voucher", voucher);
            request.getRequestDispatcher("/views/voucher-detail.jsp")
                    .forward(request, response);
        } else if ("search".equals(action)) {
            String keyword = request.getParameter("keyword");

            List<Voucher> vouchers = voucherService.searchVouchers(keyword);

            if (vouchers == null || vouchers.isEmpty()) {
                request.setAttribute("message", "No voucher found");
            } else {
                request.setAttribute("vouchers", vouchers);
                request.getRequestDispatcher("/views/voucher-list.jsp")
                        .forward(request, response);
            }
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
        VoucherService service = new VoucherService();
        if ("create".equals(action)) {
            Voucher v = new Voucher();
            v.setVoucherCode(request.getParameter("code"));
            v.setDiscountPercent(new BigDecimal(request.getParameter("discount")));
            v.setAvailableQuantity(Integer.parseInt(request.getParameter("quantity")));
            v.setStatus(Integer.parseInt(request.getParameter("status")));
            v.setStartDate(Date.valueOf(request.getParameter("startDate")));
            v.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));

            String message = service.createVoucher(v);

            request.getSession().setAttribute("message", message);
            response.sendRedirect(request.getContextPath() + "/voucher");
        } else if ("edit".equals(action)) {

            Voucher v = new Voucher();
            v.setVoucherId(Integer.parseInt(request.getParameter("id")));
            v.setVoucherCode(request.getParameter("code"));
            v.setDiscountPercent(new BigDecimal(request.getParameter("discount")));
            v.setAvailableQuantity(Integer.parseInt(request.getParameter("quantity")));
            v.setStatus(Integer.parseInt(request.getParameter("status")));
            v.setStartDate(Date.valueOf(request.getParameter("startDate")));
            v.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));

            String message = service.updateVoucher(v);
            request.getSession().setAttribute("message", message);

            response.sendRedirect("voucher?action=detail&id=" + v.getVoucherId());
        } else if ("delete".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));
            String message = service.deleteVoucher(id);

            request.getSession().setAttribute("message", message);
            response.sendRedirect(request.getContextPath() + "/voucher");
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
