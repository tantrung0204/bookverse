package com.mycompany.bookverse.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 *
 *
 * @author TrungNT - CE200064
 */
public class EmailUtil {

    private static final String FROM_EMAIL = "bookverse.website@gmail.com";
    private static final String PASSWORD = "pmigpqarfwukjhgq";

    /**
     * Sends an OTP verification email to the specified address.
     *
     * @param toEmail recipient email address
     * @param otp the OTP code to send
     * @return true if email sent successfully, false otherwise
     */
    public static boolean sendOTP(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "Bookverse"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Bookverse - Password Reset OTP");

            String htmlContent = buildOtpEmailHtml(otp);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Builds a styled HTML email body for the OTP.
     */
    private static String buildOtpEmailHtml(String otp) {
        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head>"
                + "<body style='margin:0;padding:0;background-color:#f4f1ed;font-family:Georgia,serif;'>"
                + "<div style='max-width:500px;margin:40px auto;background:#ffffff;border-radius:12px;"
                + "box-shadow:0 4px 20px rgba(0,0,0,0.08);overflow:hidden;'>"
                + "<div style='background-color:#8B6B4C;padding:30px;text-align:center;'>"
                + "<h1 style='color:#ffffff;margin:0;font-size:26px;'>Bookverse</h1>"
                + "</div>"
                + "<div style='padding:35px;text-align:center;'>"
                + "<h2 style='color:#5d4a35;font-size:22px;margin-bottom:10px;'>Password Reset Request</h2>"
                + "<p style='color:#6b5a47;font-size:15px;line-height:1.6;'>"
                + "We received a request to reset your password.<br>Use the OTP code below to verify your identity:</p>"
                + "<div style='display:inline-block;background:#f9f5f0;border:2px dashed #8B6B4C;"
                + "border-radius:10px;padding:18px 40px;margin:25px 0;'>"
                + "<span style='font-size:36px;font-weight:bold;color:#8B6B4C;letter-spacing:8px;'>" + otp + "</span>"
                + "</div>"
                + "<p style='color:#999;font-size:13px;margin-top:20px;'>"
                + "This OTP is valid for <strong>5 minutes</strong>.<br>If you did not request this, please ignore this email.</p>"
                + "</div>"
                + "<div style='background:#f9f5f0;padding:15px;text-align:center;'>"
                + "<p style='color:#a89279;font-size:12px;margin:0;'>© 2026 Bookverse. All rights reserved.</p>"
                + "</div></div></body></html>";
    }
}
