package com.mycompany.bookverse.controller;

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
@WebServlet(name = "VerifyOTPController", urlPatterns = { "/verify-otp" })
public class VerifyOTPController extends HttpServlet {

    private static final long OTP_VALIDITY_MS = 5 * 60 * 1000; // 5 minutes
    private static final int MAX_ATTEMPTS = 3;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // must have OTP data in session
        if (session == null || session.getAttribute("otp") == null || session.getAttribute("otp_email") == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        request.getRequestDispatcher("/views/public/verify-otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // must have OTP data in session
        if (session == null || session.getAttribute("otp") == null || session.getAttribute("otp_email") == null) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        String action = request.getParameter("action");

        // ============ RESEND OTP ============
        if ("resend".equals(action)) {
            handleResendOTP(request, response, session);
            return;
        }

        // ============ VERIFY OTP ============
        handleVerifyOTP(request, response, session);
    }

    private void handleResendOTP(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        String email = (String) session.getAttribute("otp_email");

        // Generate new OTP
        String newOtp = OTPUtil.generateOTP();
        boolean emailSent = EmailUtil.sendOTP(email, newOtp);

        if (emailSent) {
            session.setAttribute("otp", newOtp);
            session.setAttribute("otp_created_time", System.currentTimeMillis());
            session.setAttribute("otp_attempt_count", 0);
            request.setAttribute("successMessage", "A new OTP has been sent to your email");
        } else {
            request.setAttribute("errorMessage", "Failed to resend OTP. Please try again.");
        }

        request.getRequestDispatcher("/views/public/verify-otp.jsp").forward(request, response);
    }

    private void handleVerifyOTP(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        String inputOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");
        long createdTime = (Long) session.getAttribute("otp_created_time");
        int attemptCount = (Integer) session.getAttribute("otp_attempt_count");

        // Check if OTP is expired
        if (System.currentTimeMillis() - createdTime > OTP_VALIDITY_MS) {
            request.setAttribute("errorMessage", "OTP expired");
            request.setAttribute("otpExpired", true);
            request.getRequestDispatcher("/views/public/verify-otp.jsp").forward(request, response);
            return;
        }

        // Check if OTP matches
        if (sessionOtp.equals(inputOtp)) {
            // OTP correct — allow password reset
            session.setAttribute("otp_verified", true);
            response.sendRedirect(request.getContextPath() + "/reset-password");
            return;
        }

        // OTP incorrect — increment attempt count
        attemptCount++;
        session.setAttribute("otp_attempt_count", attemptCount);

        if (attemptCount >= MAX_ATTEMPTS) {
            // Max attempts reached — invalidate and force restart
            session.removeAttribute("otp");
            session.removeAttribute("otp_email");
            session.removeAttribute("otp_created_time");
            session.removeAttribute("otp_attempt_count");
            session.setAttribute("errorMessage", "Too many failed attempts. Please try again.");
            response.sendRedirect(request.getContextPath() + "/forgot-password");
        } else {
            int remaining = MAX_ATTEMPTS - attemptCount;
            request.setAttribute("errorMessage", "Invalid OTP. " + remaining + " attempt(s) remaining.");
            request.getRequestDispatcher("/views/public/verify-otp.jsp").forward(request, response);
        }
    }
}
