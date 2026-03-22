package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.service.CustomerService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 *
 * @author TrungNT - CE200064
 */
@WebServlet(name = "ResetPasswordController", urlPatterns = { "/reset-password" })
public class ResetPasswordController extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // must have verified OTP
        if (session == null || session.getAttribute("otp_verified") == null
                || session.getAttribute("otp_email") == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        request.getRequestDispatcher("/views/public/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // must have verified OTP
        if (session == null || session.getAttribute("otp_verified") == null
                || session.getAttribute("otp_email") == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate password length
        if (newPassword == null || newPassword.length() < 8) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters");
            request.getRequestDispatcher("/views/public/reset-password.jsp").forward(request, response);
            return;
        }

        // Validate passwords match
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/views/public/reset-password.jsp").forward(request, response);
            return;
        }

        // Update password in database
        String email = (String) session.getAttribute("otp_email");
        boolean updated = customerService.updatePassword(email, newPassword);

        // Clear all OTP-related session attributes
        session.removeAttribute("otp");
        session.removeAttribute("otp_email");
        session.removeAttribute("otp_created_time");
        session.removeAttribute("otp_attempt_count");
        session.removeAttribute("otp_verified");

        if (updated) {
            session.setAttribute("successMessage",
                    "Password reset successfully! Please sign in with your new password.");
            response.sendRedirect(request.getContextPath() + "/signin");
        } else {
            session.setAttribute("errorMessage", "Failed to reset password. Please try again.");
            response.sendRedirect(request.getContextPath() + "/forgot-password");
        }
    }
}
