/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.service.StaffService;
import com.mycompany.bookverse.utils.PaginationConfig;
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

/**
 *
 * @author TrungNT - CE200064
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
@WebServlet(name = "StaffManagementController", urlPatterns = { "/dashboard/staff" })
public class StaffManagementController extends HttpServlet {

    private StaffService staffService = new StaffService();

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
            action = "list";
        }

        switch (action) {
            case "search":
                String keyword = request.getParameter("keyword");

                if (keyword == null || keyword.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff?action=list");
                    return;
                }
                keyword = keyword.trim();

                if (keyword.length() > 50) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff?action=list&msg=error_keyword_long");
                    return;
                }

                if (!keyword.matches("^[a-zA-Z0-9À-ỹ\\s._-]+$")) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff?action=list&msg=error_keyword_invalid");
                    return;
                }

                List<Staff> searchResults = staffService.searchStaffs(keyword);
                request.setAttribute("staffs", searchResults);
                request.setAttribute("searchKeyword", keyword);
                break;

            case "view": {
                try {
                    int id = Integer.parseInt(request.getParameter("staffId"));
                    Staff c = staffService.getStaffById(id);
                    if (c != null) {
                        request.setAttribute("staffDetail", c);
                        request.setAttribute("openViewModal", true);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error View Staff: " + e.getMessage());
                }
                int page = 1;
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

                int pageSize = com.mycompany.bookverse.utils.PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                List<Staff> listForView = staffService.getAllStaffs(page, pageSize);
                long totalStaffs = staffService.getTotalStaffs();
                int totalPages = (int) Math.ceil((double) totalStaffs / pageSize);

                request.setAttribute("staffs", listForView);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                break;
            }

            case "list":
            default:

                int page = 1;
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

                int pageSize = com.mycompany.bookverse.utils.PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                List<Staff> listForView = staffService.getAllStaffs(page, pageSize);
                long totalStaffs = staffService.getTotalStaffs();
                int totalPages = (int) Math.ceil((double) totalStaffs / pageSize);

                request.setAttribute("staffs", listForView);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                break;
        }

        request.setAttribute("contentPage", "staff-list.jsp");
        request.setAttribute("activeMenu", "staff");
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
            response.sendRedirect(request.getContextPath() + "/dashboard/staff");
            return;
        }

        switch (action) {
            case "create": {
                try {
                    String fullName = request.getParameter("fullName");
                    String password = request.getParameter("password");
                    String username = request.getParameter("username");
                    String roleName = request.getParameter("roleName");
                    String msg = staffService.insertStaff(fullName, username, password, roleName);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("createError", msg);
                        request.setAttribute("openCreatePopup", true);
                        request.setAttribute("createUsername", username);
                        request.setAttribute("createFullName", fullName);

                        int page = 1;
                        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                        List<Staff> staffs = staffService.getAllStaffs(page, pageSize);
                        request.setAttribute("staffs", staffs);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages",
                                (int) Math.ceil((double) staffService.getTotalStaffs() / pageSize));
                        request.setAttribute("pageSize", pageSize);

                        request.setAttribute("contentPage", "staff-list.jsp");
                        request.setAttribute("activeMenu", "staff");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("success", "Create successfully");
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff");

                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff");
                }
                break;
            }
            case "edit": {
                try {
                    int staffId = Integer.parseInt(request.getParameter("staffId"));
                    String editFullName = request.getParameter("fullName");
                    String editPassword = request.getParameter("password");
                    String editUsername = request.getParameter("username");
                    String editRole = request.getParameter("roleName");
                    String profileImageUrl = null;

                    Part filePart = request.getPart("avatarFile");

                    if (filePart != null && filePart.getSize() > 0) {

                        String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName()
                                .toString();

                        String uploadPath = getServletContext().getRealPath("") + java.io.File.separator + "assets"
                                + java.io.File.separator + "images";
                        java.io.File uploadDir = new java.io.File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }

                        String newFileName = System.currentTimeMillis() + "_" + fileName;
                        filePart.write(uploadPath + java.io.File.separator + newFileName);

                        profileImageUrl = "assets/images/" + newFileName;
                    }

                    String msg = staffService.editStaff(staffId, editFullName, editPassword, profileImageUrl, editRole);

                    if (!msg.contains("successfully")) {
                        request.setAttribute("editError", msg);
                        request.setAttribute("openEditPopup", true);

                        // Giữ lại form
                        request.setAttribute("editStaffId", staffId);
                        request.setAttribute("editStaffUsername", editUsername);
                        request.setAttribute("editStaffFullName", editFullName);
                        request.setAttribute("editStaffRole", editRole);

                        // Load bảng nền
                        int page = 1;
                        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                        List<Staff> staffs = staffService.getAllStaffs(page, pageSize);
                        request.setAttribute("staffs", staffs);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages",
                                (int) Math.ceil((double) staffService.getTotalStaffs() / pageSize));
                        request.setAttribute("pageSize", pageSize);

                        request.setAttribute("contentPage", "staff-list.jsp");
                        request.setAttribute("activeMenu", "staff");
                        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                        return;
                    }

                    request.getSession().setAttribute("success", "Edit successfully");
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff");

                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff");
                }
                break;
            }

            case "delete":
                try {
                    int idDelete = Integer.parseInt(request.getParameter("staffId"));
                    boolean success = staffService.deleteStaff(idDelete);

                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/dashboard/staff?msg=success_delete");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/dashboard/staff?msg=error_delete");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error Delete - Invalid ID: " + e.getMessage());
                    response.sendRedirect(request.getContextPath() + "/dashboard/staff?msg=error_invalid_id");
                }
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/dashboard/staff");
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
