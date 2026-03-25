/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.service.ProfileService;
import com.mycompany.bookverse.utils.PasswordUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;

/**
 *
 * @author LECOO
 */
@WebServlet(name = "ProfileManagementController", urlPatterns = {"/profile"})
@MultipartConfig
public class ProfileManagementController extends HttpServlet {

    private ProfileService profileService = new ProfileService();

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
            out.println("<title>Servlet ProfileManagementController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileManagementController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false);
        String view = request.getParameter("view");
        if (session != null && session.getAttribute("user") != null) {
            String error = (String) session.getAttribute("error");
            if (error != null && !error.isEmpty()) {
                request.setAttribute("oldPass", (String) session.getAttribute("oldPass"));
                request.setAttribute("newPass", (String) session.getAttribute("newPass"));
                request.setAttribute("reNewPass", (String) session.getAttribute("reNewPass"));
                request.setAttribute("error", error);

                session.removeAttribute("oldPass");
                session.removeAttribute("newPass");
                session.removeAttribute("reNewPass");
                session.removeAttribute("error");
            } else {
                String success = (String) session.getAttribute("success");
                request.setAttribute("success", success);
                session.removeAttribute("success");
            }
            if ("customer".equals(session.getAttribute("role"))) {
                if ("edit".equals(view)) {
                    request.setAttribute("openCustomerProfile", "yes");
                    request.setAttribute("openCustomerEditProfile", "yes");
                    request.setAttribute("activeMenu", "edit");
                    request.getRequestDispatcher("/views/public/profile.jsp")
                            .forward(request, response);
                    return;
                } else if ("changePassword".equals(view)) {
                    request.setAttribute("openCustomerProfile", "yes");
                    request.setAttribute("activeMenu", "changePassword");
                    request.setAttribute("openCustomerChangePasswordProfile", "yes");
                    request.getRequestDispatcher("/views/public/profile.jsp")
                            .forward(request, response);
                    return;
                }
                Customer customer = (Customer) session.getAttribute("user");
                request.setAttribute("countOrder", profileService.countOder(customer));
                request.setAttribute("countFeedback", profileService.countFeedback(customer));
                request.setAttribute("openCustomerProfile", "yes");
                request.setAttribute("openCustomerViewProfile", "yes");
                request.setAttribute("activeMenu", "view");
                request.getRequestDispatcher("/views/public/profile.jsp")
                        .forward(request, response);
                return;
            }
            request.setAttribute("openStaffProfile", "yes");
            request.setAttribute("activeMenu", "staffView");
            request.getRequestDispatcher("/views/dashboard/dashboard.jsp")
                    .forward(request, response);
            return;
        }
        request.getRequestDispatcher("/views/public/signin.jsp").forward(request, response);

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
        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");
        String error;
        if (session != null && session.getAttribute("user") != null) {
            
            switch (action) {

                case "changeAvatar":
                    //Lấy file từ request.
                    Part filePart = request.getPart("avatarFile");

                    if (filePart == null || filePart.getSize() == 0) {
                        response.sendRedirect("profile");
                        return;
                    }
                    // đặt tên file, tránh đặt trùng tên thì + thêm time vào thời điểm tạo.
                    String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                    //Lấy đường dẫn tuyệt đối từ ổ đĩa.
                    String uploadPath = getServletContext().getRealPath("/assets/images/avatars");

                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    filePart.write(uploadPath + File.separator + fileName);

                    String imagePath = "assets/images/avatars/" + fileName;

                    if ("customer".equals(role)) {
                        Customer customer = (Customer) session.getAttribute("user");
                        customer.setProfileImageUrl(imagePath);
                        profileService.updateInforForCustomer(customer);
                        response.sendRedirect("profile?view=edit");
                        return;
                    } else {
                        Staff staff = (Staff) session.getAttribute("user");
                        staff.setProfileImageUrl(imagePath);
                        profileService.updateInforForStaff(staff);
                    }
                    response.sendRedirect("profile");
                    break;
                case "editInfo":
                    String fullName = request.getParameter("fullName");
                    String phoneNumber = request.getParameter("phoneNumber");
                    String address = request.getParameter("address");

                    if ("customer".equals(role)) {
                        Customer customer = (Customer) session.getAttribute("user");
                        error = profileService.checkValidEditInformation(fullName, phoneNumber, address, role, customer.getCustomerId());
                        if (error.isEmpty()) {
                            customer.setFullName(fullName);
                            customer.setPhoneNumber(phoneNumber);
                            customer.setAddress(address);
                            profileService.updateInforForCustomer(customer);
                        }
                    } else {
                        Staff staff = (Staff) session.getAttribute("user");
                        error = profileService.checkValidEditInformation(fullName, phoneNumber, address, role, 0);
                        if (error.isEmpty()) {
                            staff.setFullName(fullName);
                            profileService.updateInforForStaff(staff);
                        }
                    }
                    if (!error.isEmpty()) {
                        session.setAttribute("error", error);
                    } else {
                        session.setAttribute("success", "edit successfully" + error);
                    }
                    response.sendRedirect("profile?view=edit");
                    break;
                case "changePassword":
                    String oldPass = request.getParameter("oldPassword").toLowerCase();
                    String newPass = request.getParameter("newPassword").toLowerCase();
                    String reNewPass = request.getParameter("reNewPassword").toLowerCase();
                    String hashedNewPassword = PasswordUtil.hashPassword(reNewPass);

                    if ("customer".equals(role)) {
                        Customer customer = (Customer) session.getAttribute("user");
                        error = profileService.checkValidChangePassword(oldPass, newPass, reNewPass, customer, null);
                        if (error.isEmpty()) {
                            customer.setPasswordHash(hashedNewPassword);
                            profileService.updateInforForCustomer(customer);
                        }
                    } else {
                        Staff staff = (Staff) session.getAttribute("user");
                        error = profileService.checkValidChangePassword(oldPass, newPass, reNewPass, null, staff);
                        if (error.isEmpty()) {
                            staff.setPasswordHash(hashedNewPassword);
                            profileService.updateInforForStaff(staff);
                        }
                    }

                    if (!error.isEmpty()) {
                        session.setAttribute("oldPass", oldPass);
                        session.setAttribute("newPass", newPass);
                        session.setAttribute("reNewPass", reNewPass);
                        session.setAttribute("error", error);
                    } else {
                        session.setAttribute("success", "Change password successfully");
                    }
                    response.sendRedirect("profile?view=changePassword");
                    break;
                default:
                    response.sendRedirect("profile?view=edit");
            }
        } else {
            request.getRequestDispatcher("/views/public/signin.jsp").forward(request, response);
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
