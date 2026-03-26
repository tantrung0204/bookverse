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
@WebServlet(name = "CustomerManagementController", urlPatterns = { "/dashboard/customer" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)

public class CustomerManagementController extends HttpServlet {

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

    private boolean isValidFullName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[\\p{L}][\\p{L}\\s]*$");
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9][a-zA-Z0-9._-]{4,}$");
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        email = email.trim();
        return email.matches("^[a-zA-Z0-9][a-zA-Z0-9._+-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
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
        if (action == null || action.trim().isEmpty()) {
            /////
            action = "list";
        }

        switch (action) {
            case "search":
                String keyword = request.getParameter("keyword");

                if (keyword == null || keyword.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer?action=list");
                    return;
                }
                keyword = keyword.trim();

                if (keyword.length() > 50) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer?action=list&msg=error_keyword_long");
                    return;
                }

                if (!keyword.matches("^[\\p{L}0-9 @.\\-_]+$")) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer?action=list&msg=error_keyword_invalid");
                    return;
                }

                List<Customer> searchResults = customerService.searchCustomers(keyword);
                request.setAttribute("customers", searchResults);
                request.setAttribute("searchKeyword", keyword);
                break;

            case "view": {
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
                int page = 1;
                ///////
                String pageStr = request.getParameter("page");

                if (pageStr != null && !pageStr.trim().isEmpty()) {
                    try {
                        page = Integer.parseInt(pageStr);
                        if (page < 1) {
                            page = 1;
                        }
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }

                int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;

                List<Customer> list = customerService.getAllCustomers(page, pageSize);
                long totalCustomers = customerService.getTotalCustomers();
                int totalPages = (int) Math.ceil((double) totalCustomers / pageSize);

                request.setAttribute("customers", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                break;
            }

            case "list":
            default: {

                int page = 1;
                ///////
                String pageStr = request.getParameter("page");

                if (pageStr != null && !pageStr.trim().isEmpty()) {
                    try {
                        page = Integer.parseInt(pageStr);
                        if (page < 1) {
                            page = 1;
                        }
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
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
            response.sendRedirect(request.getContextPath() + "/dashboard/customer");
            return;
        }

        switch (action) {
            case "create": {
                try {
                    String fullName = request.getParameter("fullName");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    String username = request.getParameter("username");
                    String phone = request.getParameter("phone");

                    String msg = customerService.insertCustomer(fullName, email, phone, username, password);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("createError", msg);
                        request.setAttribute("openCreatePopup", true);
                        request.setAttribute("createUsername", username);
                        request.setAttribute("createFullName", fullName);
                        request.setAttribute("createEmail", email);

                        int page = 1;
                        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                        List<Customer> customers = customerService.getAllCustomers(page, pageSize);
                        request.setAttribute("customers", customers);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages",
                                (int) Math.ceil((double) customerService.getTotalCustomers() / pageSize));
                        request.setAttribute("pageSize", pageSize);

                        request.setAttribute("contentPage", "customer-list.jsp");
                        request.setAttribute("activeMenu", "customer");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("success", "Create successfully");
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer");

                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer");
                }
                break;
            }

            case "edit": {
                try {
                    int customerId = Integer.parseInt(request.getParameter("customerId"));
                    String fullName = request.getParameter("fullName");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String password = request.getParameter("password");
                    String username = request.getParameter("username");
                    String address = request.getParameter("address");

                    String profileImageUrl = null;
                    Part filePart = request.getPart("avatarFile");

                    if (filePart != null && filePart.getSize() > 0) {

                        String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName()
                                .toString();

                        String uploadPath = getServletContext().getRealPath("") + java.io.File.separator + "assets"
                                + java.io.File.separator + "images" + java.io.File.separator + "avatars";
                        java.io.File uploadDir = new java.io.File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }

                        String newFileName = System.currentTimeMillis() + "_" + fileName;
                        filePart.write(uploadPath + java.io.File.separator + newFileName);

                        profileImageUrl = request.getContextPath() + "/assets/images/avatars/" + newFileName;
                    }

                    String msg = customerService.editCustomer(customerId, fullName, email, phone, address, password,
                            profileImageUrl);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("editError", msg);
                        request.setAttribute("openEditPopup", true);

                        // Giữ lại form
                        request.setAttribute("editCustomerId", customerId);
                        request.setAttribute("editCustomerUsername", username);
                        request.setAttribute("editCustomerFullName", fullName);
                        request.setAttribute("editCustomerEmail", email);
                        request.setAttribute("editCustomerPhone", phone);
                        request.setAttribute("editCustomerAddress", address);

                        // Load bảng nền
                        int page = 1;
                        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                        List<Customer> customers = customerService.getAllCustomers(page, pageSize);
                        request.setAttribute("customers", customers);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages",
                                (int) Math.ceil((double) customerService.getTotalCustomers() / pageSize));
                        request.setAttribute("pageSize", pageSize);

                        request.setAttribute("contentPage", "customer-list.jsp");
                        request.setAttribute("activeMenu", "customer");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("success", "Edit successfully");
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer");

                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer");
                }
                break;
            }

            case "delete":
                try {
                    int idDelete = Integer.parseInt(request.getParameter("customerId"));
                    boolean success = customerService.deleteCustomer(idDelete);

                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/dashboard/customer?msg=success_delete");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/dashboard/customer?msg=error_delete");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error Delete - Invalid ID: " + e.getMessage());
                    response.sendRedirect(request.getContextPath() + "/dashboard/customer?msg=error_invalid_id");
                }
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/dashboard/customer");
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
