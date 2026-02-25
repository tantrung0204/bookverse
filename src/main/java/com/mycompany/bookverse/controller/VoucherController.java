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
import com.mycompany.bookverse.utils.JPAUtil;
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
        int page = 0;
        int pageSize = 0;
        String pageParam = "";
        long totalItems = 0;
        int totalPages = 0;
        if (action == null) {
            action = "list";
        }
        switch (action) {
            case "list":

                page = 1;
                pageSize = JPAUtil.PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                pageParam = request.getParameter("page");
                if (pageParam != null) {
                    try {
                        page = Integer.parseInt(pageParam);
                        if (page < 1) {
                            page = 1;
                        }
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }

                totalItems = voucherService.getTotalVoucherCount();
                totalPages = (int) Math.ceil((double) totalItems / pageSize);

                if (page > totalPages && totalPages > 0) {
                    page = totalPages;
                }

                List<Voucher> vouchers = voucherService.getVouchersPaging(page, pageSize);

                request.setAttribute("vouchers", vouchers);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("contentPage", "voucher-list.jsp");
                request.setAttribute("activeMenu", "voucher");

                request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                        .forward(request, response);
                break;

            case "search":
                String keyword = request.getParameter("keyword");

                page = 1;
                pageSize = JPAUtil.PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                pageParam = request.getParameter("page");
                if (pageParam != null) {
                    page = Integer.parseInt(pageParam);
                }

                totalItems = voucherService.countSearchVoucher(keyword);
                totalPages = (int) Math.ceil((double) totalItems / pageSize);

                List<Voucher> searchList
                        = voucherService.searchVouchersPaging(keyword, page, pageSize);

                request.setAttribute("vouchers", searchList);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("keyword", keyword);

                request.setAttribute("contentPage", "voucher-list.jsp");
                request.setAttribute("activeMenu", "voucher");

                request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                        .forward(request, response);
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
                        int page = 1;
                        int pageSize = JPAUtil.PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                        long totalItems = voucherService.getTotalVoucherCount();
                        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

                        List<Voucher> vouchers = voucherService.getVouchersPaging(page, pageSize);

                        request.setAttribute("vouchers", vouchers);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages", totalPages);

                        request.setAttribute("createError", msg);   // chỉ dùng createError
                        request.setAttribute("openCreate", true);
                        request.setAttribute("contentPage", "voucher-list.jsp");
                        request.setAttribute("activeMenu", "voucher");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                                .forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("successMessage", msg);
                    response.sendRedirect(request.getContextPath() + "/voucher");

                } catch (Exception e) {
                    List<Voucher> vouchers = voucherService.getVouchers();
                    request.setAttribute("vouchers", vouchers);

                    request.setAttribute("createError", "Invalid input data");
                    request.setAttribute("openCreate", true);
                    request.setAttribute("contentPage", "voucher-list.jsp");
                    request.setAttribute("activeMenu", "voucher");
                    request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
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
                        int page = 1;
                        int pageSize = JPAUtil.PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                        long totalItems = voucherService.getTotalVoucherCount();
                        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

                        List<Voucher> vouchers = voucherService.getVouchersPaging(page, pageSize);

                        request.setAttribute("vouchers", vouchers);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages", totalPages);
                        request.setAttribute("editError", msg);
                        request.setAttribute("voucher", old);

                        request.setAttribute("openEdit", true);
                        request.setAttribute("contentPage", "voucher-list.jsp");
                        request.setAttribute("activeMenu", "voucher");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                                .forward(request, response);
                        return;
                    }
                    request.getSession().setAttribute("successMessage", msg);
                    response.sendRedirect(request.getContextPath() + "/voucher?action=list");

                } catch (Exception e) {

                    List<Voucher> vouchers = voucherService.getVouchers();
                    request.setAttribute("vouchers", vouchers);

                    int id = Integer.parseInt(request.getParameter("id"));
                    Voucher old = voucherService.getVoucherById(id);

                    request.setAttribute("message", "Invalid input data");
                    request.setAttribute("voucher", old);
                    request.setAttribute("openEdit", true);

                    request.setAttribute("contentPage", "voucher-list.jsp");
                    request.setAttribute("activeMenu", "voucher");
                    request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                            .forward(request, response);
                }
                break;

            case "delete":
                try {
                    int idDelete = Integer.parseInt(request.getParameter("id"));
                    String deleteMsg = voucherService.deleteVoucher(idDelete);

                    request.getSession().setAttribute("successMessage", deleteMsg);

                } catch (Exception e) {
                    request.getSession().setAttribute("errorMessage", "Invalid voucher ID");
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
