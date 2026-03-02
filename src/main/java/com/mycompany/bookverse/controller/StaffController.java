/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.service.StaffService;
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
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
@WebServlet(name = "StaffController", urlPatterns = {"/staff"})
public class StaffController extends HttpServlet {

    private StaffService staffService = new StaffService();

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
            out.println("<title>Servlet SignupController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SignupController at " + request.getContextPath() + "</h1>");
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
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "search":
                String keyword = request.getParameter("keyword");
                List<Staff> searchResults = staffService.searchStaffs(keyword);
                request.setAttribute("staffs", searchResults);
                request.setAttribute("searchKeyword", keyword);
                break;

            case "view":
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
                List<Staff> listForView = staffService.getAllStaffs();
                request.setAttribute("staffs", listForView);
                break;

            case "list":
            default:
                List<Staff> list = staffService.getAllStaffs();
                request.setAttribute("staffs", list);
                break;
        }

        request.setAttribute("contentPage", "staff-list.jsp");
        request.setAttribute("activeMenu", "staff");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("staff");
            return;
        }

        switch (action) {
            case "create":
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String username = request.getParameter("username");

                Staff newStaff = new Staff();
                newStaff.setFullName(fullName);
                //newStaff.setEmail(email);
                newStaff.setPasswordHash(password);
                newStaff.setUsername(username);
                newStaff.setStatus(1);

                Part avatarPart = request.getPart("avatar");
                String fileName = "";
                if (avatarPart != null && avatarPart.getSize() > 0) {
                    fileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
                    newStaff.setProfileImageUrl(fileName);
                }
                int result = staffService.addStaff(newStaff);

                if (result == 0) {
                    response.sendRedirect("staff?msg=success_add");
                } else if (result == 2) {
                    response.sendRedirect("staff?msg=missing_info");
                } else {
                    response.sendRedirect("staff?msg=error_db");
                }
                break;

            case "edit":
                try {
                    int id = Integer.parseInt(request.getParameter("staffId"));
                    Staff editStaff = staffService.getStaffById(id);

                    if (editStaff != null) {
                        editStaff.setFullName(request.getParameter("fullName"));
                        //editStaff.setEmail(request.getParameter("email"));

                        int resultEdit = staffService.editStaff(editStaff);
                        if (resultEdit == 0) {
                            response.sendRedirect("staff?msg=success_edit");
                        } else {
                            response.sendRedirect("staff?msg=error_edit");
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error Edit - Invalid ID: " + e.getMessage());
                    response.sendRedirect("staff?msg=error_invalid_id");
                }
                break;

            case "delete":
                try {
                    int idDelete = Integer.parseInt(request.getParameter("staffId"));
                    boolean success = staffService.deleteStaff(idDelete);

                    if (success) {
                        response.sendRedirect("staff?msg=success_delete");
                    } else {
                        response.sendRedirect("staff?msg=error_delete");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error Delete - Invalid ID: " + e.getMessage());
                    response.sendRedirect("staff?msg=error_invalid_id");
                }
                break;

            default:
                response.sendRedirect("staff");
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
