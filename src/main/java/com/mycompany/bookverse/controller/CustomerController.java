/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.service.CustomerService;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
@WebServlet(name = "CustomerController", urlPatterns = {"/customer"})
public class CustomerController extends HttpServlet {
    
    private CustomerService customerService = new CustomerService();

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
        List<Customer> list = customerService.getAllCustomers();
        request.setAttribute("customers", list);
        // Định nghĩa file nội dung
        request.setAttribute("contentPage", "customer-list.jsp");
        // Đánh dấu menu active
        request.setAttribute("activeMenu", "customer");
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
            response.sendRedirect("customer");
            return;
        }

       
        if ("create".equals(action)) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String username = request.getParameter("username"); 
            
            Customer newCustomer = new Customer();
            newCustomer.setFullName(fullName);
            newCustomer.setEmail(email);
            newCustomer.setPasswordHash(password); 
            newCustomer.setUsername(username);
            newCustomer.setStatus(1); 

            int result = customerService.addCustomer(newCustomer);
            
            
            if (result == 0) {
                response.sendRedirect("customer?msg=success_add");
            } else if (result == 2) {
                response.sendRedirect("customer?msg=missing_info");
            } else {
                response.sendRedirect("customer?msg=error_db");
            }
        } 
        
       
        else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("customerId"));
            Customer editCustomer = customerService.getCustomerById(id);
            
            if (editCustomer != null) {
                editCustomer.setFullName(request.getParameter("fullName"));
                editCustomer.setEmail(request.getParameter("email"));
               
                
                int result = customerService.editCustomer(editCustomer);
                if (result == 0) {
                    response.sendRedirect("customer?msg=success_edit");
                } else {
                    response.sendRedirect("customer?msg=error_edit");
                }
            }
        } 
        
       
        else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("customerId"));
            boolean success = customerService.deleteCustomer(id);
            
            if (success) {
                response.sendRedirect("customer?msg=success_delete");
            } else {
                response.sendRedirect("customer?msg=error_delete");
            }
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
