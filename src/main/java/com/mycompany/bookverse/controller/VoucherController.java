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
                String idStr = request.getParameter("id");

                if (idStr == null || idStr.trim().isEmpty()) {
                    request.setAttribute("message", "Invalid voucher ID");
                    request.getRequestDispatcher("/views/voucher-list.jsp")
                            .forward(request, response);
                    return;
                }
                try {
                    int id = Integer.parseInt(idStr);
                    Voucher voucher = voucherService.getVoucherById(id);

                    if (voucher == null) {
                        request.setAttribute("message", "Voucher not found");
                        request.getRequestDispatcher("/views/voucher-list.jsp")
                                .forward(request, response);
                    } else {
                        request.setAttribute("voucher", voucher);
                        request.getRequestDispatcher("/views/voucher-detail.jsp")
                                .forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("message", "Voucher ID must be a number");
                    request.getRequestDispatcher("/views/voucher-list.jsp")
                            .forward(request, response);
                }
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
                try {
                    Voucher vCreate = new Voucher();
                    vCreate.setVoucherCode(request.getParameter("code"));
                    vCreate.setDiscountPercent(new BigDecimal(request.getParameter("discount")));
                    vCreate.setAvailableQuantity(Integer.parseInt(request.getParameter("quantity")));
                    vCreate.setStatus(Integer.parseInt(request.getParameter("status")));

                    vCreate.setStartDate(new java.util.Date());
                    vCreate.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));

                    String msg = voucherService.createVoucher(vCreate);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("message", msg);
                        request.setAttribute("openCreate", true);
                        request.getRequestDispatcher("/views/voucher-list.jsp")
                                .forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("message", msg);
                    response.sendRedirect(request.getContextPath() + "/voucher");

                } catch (Exception e) {
                    request.setAttribute("message", "Invalid input data");
                    request.setAttribute("openCreate", true);
                    request.getRequestDispatcher("/views/voucher-list.jsp")
                            .forward(request, response);
                }
                break;
            case "edit":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Voucher old = voucherService.getVoucherById(id);

                    if (old == null) {
                        request.getSession().setAttribute("message", "Voucher not found");
                        response.sendRedirect(request.getContextPath() + "/voucher");
                        return;
                    }

                    Voucher vEdit = new Voucher();
                    vEdit.setVoucherId(id);
                    vEdit.setVoucherCode(request.getParameter("code"));
                    vEdit.setDiscountPercent(new BigDecimal(request.getParameter("discount")));
                    vEdit.setAvailableQuantity(Integer.parseInt(request.getParameter("quantity")));
                    vEdit.setStatus(Integer.parseInt(request.getParameter("status")));
                    vEdit.setStartDate(old.getStartDate());
                    vEdit.setExpiryDate(Date.valueOf(request.getParameter("expiryDate")));

                    String msg = voucherService.updateVoucher(vEdit);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("message", msg);
                        request.setAttribute("voucher", old);
                        request.setAttribute("openEdit", true);
                        request.getRequestDispatcher("/views/voucher-detail.jsp")
                                .forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("message", msg);
                    response.sendRedirect(request.getContextPath() + "/voucher?action=detail&id=" + id);

                } catch (Exception e) {
                    request.getSession().setAttribute("message", "Invalid input data");
                    response.sendRedirect(request.getContextPath() + "/voucher");
                }
                break;

            case "delete":
                try {
                    int idDelete = Integer.parseInt(request.getParameter("id"));
                    String deleteMsg = voucherService.deleteVoucher(idDelete);
                    request.getSession().setAttribute("message", deleteMsg);
                } catch (Exception e) {
                    request.getSession().setAttribute("message", "Invalid voucher ID");
                }

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
