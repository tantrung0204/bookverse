package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.service.CustomerService;
import com.mycompany.bookverse.utils.EmailUtil;
import com.mycompany.bookverse.utils.OTPUtil;
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
@WebServlet(name = "ForgotPasswordController", urlPatterns = { "/forgot-password" })
public class ForgotPasswordController extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/public/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        // Validate email format
        if (email == null || email.trim().isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            request.setAttribute("errorMessage", "Invalid email format");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/public/forgot-password.jsp").forward(request, response);
            return;
        }

        email = email.trim();

        // Check if email exists in database
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            request.setAttribute("errorMessage", "Email not found");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/public/forgot-password.jsp").forward(request, response);
            return;
        }

        // Generate OTP and send email
        String otp = OTPUtil.generateOTP();
        boolean emailSent = EmailUtil.sendOTP(email, otp);

        if (!emailSent) {
            request.setAttribute("errorMessage", "Failed to send OTP. Please try again later.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/public/forgot-password.jsp").forward(request, response);
            return;
        }

        // Store OTP data in session
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("otp_email", email);
        session.setAttribute("otp_created_time", System.currentTimeMillis());
        session.setAttribute("otp_attempt_count", 0);

        response.sendRedirect(request.getContextPath() + "/verify-otp");
    }
}
