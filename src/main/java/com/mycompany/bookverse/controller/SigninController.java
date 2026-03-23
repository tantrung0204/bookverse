/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.service.CustomerService;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.service.StaffService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author TrungNT - CE200064
 */
@WebServlet(name = "SigninController", urlPatterns = {"/signin"})
public class SigninController extends HttpServlet {

    private CustomerService customerService = new CustomerService();
    private StaffService staffService = new StaffService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String role = (String) session.getAttribute("role");

            if ("customer".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
            return;
        }

        request.getRequestDispatcher("/views/public/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        HttpSession session = request.getSession();

        if ("customer".equals(role)) {
            Customer customer = customerService.signin(username, password);
            if (customer != null) {
                session.setAttribute("user", customer);
                session.setAttribute("role", "customer");
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
        } else {
            Staff staff = staffService.signin(username, password);
            if (staff != null) {
                session.setAttribute("user", staff);
                session.setAttribute("role", staff.getRoleName());
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
        }

        request.setAttribute("errorMessage", "Username or password is incorrect or account is inactive!");
        request.setAttribute("username", username);

        request.setAttribute("selectedRole", role);
        request.getRequestDispatcher("/views/public/signin.jsp").forward(request, response);
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
