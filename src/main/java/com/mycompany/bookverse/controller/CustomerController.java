/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.service.CustomerService;
import com.mycompany.bookverse.utils.PasswordUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.List;
import com.mycompany.bookverse.utils.PaginationConfig;

/**
 *
 * @author TrungNT - CE200064
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
@WebServlet(name = "CustomerController", urlPatterns = { "/customer" })
public class CustomerController extends HttpServlet {

    private CustomerService customerService = new CustomerService();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SignupController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SignupController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "search":
                String keyword = request.getParameter("keyword");
                List<Customer> searchResults = customerService.searchCustomers(keyword);
                request.setAttribute("customers", searchResults);
                request.setAttribute("searchKeyword", keyword);
                break;

            case "view":
                try {
                    int id = Integer.parseInt(request.getParameter("customerId"));
                    Customer c = customerService.getCustomerById(id);
                    if (c != null) {

                        int totalOrders = (c.getOrderCollection() != null) ? c.getOrderCollection().size() : 0;
                        request.setAttribute("customerDetail", c);
                        request.setAttribute("totalOrders", totalOrders);
                        request.setAttribute("openViewModal", true);

                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error View Customer: " + e.getMessage());
                }
                List<Customer> listForView = customerService.getAllCustomers(1, PaginationConfig.ADMIN_ITEMS_PER_PAGE);
                request.setAttribute("customers", listForView);
                break;

            case "list":
            default:
                // 1. Lấy trang hiện tại từ URL (mặc định là 1)
                int page = 1;
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                }

                // 2. Lấy số lượng trên 1 trang từ PaginationConfig
                int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                // 3. Lấy dữ liệu danh sách và tổng số trang
                List<Customer> list = customerService.getAllCustomers(page, pageSize);
                long totalCustomers = customerService.getTotalCustomers();
                int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

                // 4. Gửi dữ liệu sang JSP
                request.setAttribute("customers", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                break;
        }

        request.setAttribute("contentPage", "customer-list.jsp");
        request.setAttribute("activeMenu", "customer");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("customer");
            return;
        }

        switch (action) {
            case "create":
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String username = request.getParameter("username");
                String phone = request.getParameter("phone");
                String hashedPassword = PasswordUtil.hashPassword(password);

                Customer newCustomer = new Customer();
                newCustomer.setFullName(fullName);
                newCustomer.setEmail(email);
                newCustomer.setPasswordHash(hashedPassword);
                newCustomer.setUsername(username);
                newCustomer.setPhoneNumber(phone);
                newCustomer.setStatus(1);
                newCustomer.setCreatedAt(new java.util.Date());

                Part avatarPart = request.getPart("avatar");
                String fileName = "";
                if (avatarPart != null && avatarPart.getSize() > 0) {
                    fileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
                    newCustomer.setProfileImageUrl(fileName);
                } else {
                    newCustomer.setProfileImageUrl("assets/images/default-avt.jpg");
                }
                int result = customerService.addCustomer(newCustomer);

                if (result == 1) {
                    response.sendRedirect("customer?msg=success_add");
                } else if (result == 2) {
                    response.sendRedirect("customer?msg=missing_info");
                } else {
                    response.sendRedirect("customer?msg=error_db");
                }
                break;

            case "edit":
                try {
                    int id = Integer.parseInt(request.getParameter("customerId"));
                    Customer editCustomer = customerService.getCustomerById(id);

                    if (editCustomer != null) {
                        editCustomer.setFullName(request.getParameter("fullName"));
                        editCustomer.setEmail(request.getParameter("email"));
                        editCustomer.setPhoneNumber(request.getParameter("phone"));

                        String newPassword = request.getParameter("password");
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            String hashedNewPassword = PasswordUtil.hashPassword(newPassword);
                            editCustomer.setPasswordHash(hashedNewPassword);
                        }

                        int resultEdit = customerService.editCustomer(editCustomer);
                        if (resultEdit == 1) {
                            response.sendRedirect("customer?msg=success_edit");
                        } else {
                            response.sendRedirect("customer?msg=error_edit");
                        }

                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error Edit - Invalid ID: " + e.getMessage());
                    response.sendRedirect("customer?msg=error_invalid_id");
                }
                break;

            case "delete":
                try {
                    int idDelete = Integer.parseInt(request.getParameter("customerId"));
                    boolean success = customerService.deleteCustomer(idDelete);

                    if (success) {
                        response.sendRedirect("customer?msg=success_delete");
                    } else {
                        response.sendRedirect("customer?msg=error_delete");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error Delete - Invalid ID: " + e.getMessage());
                    response.sendRedirect("customer?msg=error_invalid_id");
                }
                break;

            default:
                response.sendRedirect("customer");
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
