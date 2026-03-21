/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CustomerDAO;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.PasswordUtil;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public List<Customer> getAllCustomers(int page, int pageSize) {
        return customerDAO.findAll(page, pageSize);
    }

    public long getTotalCustomers() {
        return customerDAO.getTotalCustomers();
    }

    public Customer getCustomerById(int id) {
        return customerDAO.findById(id);
    }

    public String insertCustomer(String fullName, String email, String phone, String username, String password) {
        String error = "";

        if (fullName == null || fullName.trim().isEmpty()) {
            error += "Full Name can not be empty.\n";
        }
        if (username == null || username.trim().isEmpty()) {
            error += "Username can not be empty.\n";
        }

        if (customerDAO.checkUsernameExists(username)) {
            return "Username already exists!";
        }
        if (customerDAO.checkDuplicateEmail(email, 0)) {
            return "Email already belongs to another user!";
        }

        if (!error.isEmpty()) {
            return error;
        }

        Customer newCustomer = new Customer();
        newCustomer.setFullName(fullName);
        newCustomer.setEmail(email);
        newCustomer.setPhoneNumber(phone);
        newCustomer.setUsername(username);
        newCustomer.setPasswordHash(PasswordUtil.hashPassword(password));
        newCustomer.setStatus(1);
        newCustomer.setCreatedAt(new java.util.Date());
        newCustomer.setProfileImageUrl("assets/images/default-avt.jpg");

        boolean result = customerDAO.create(newCustomer);
        if (result) { // result == true
            return "Create successfully";
        } else {
            return "Create false";
        }
    }

    public String editCustomer(int id, String fullName, String email, String phone, String address, String password, String profileImageUrl) {
        String error = "";

        if (fullName == null || fullName.trim().isEmpty()) {
            error += "Full Name can not be empty.\n";
        }

        if (customerDAO.checkDuplicateEmail(email, id)) {
            return "Email already belongs to another user!";
        }

        if (!error.isEmpty()) {
            return error;
        }

        Customer oldCustomer = customerDAO.findById(id);
        if (oldCustomer != null) {
            oldCustomer.setFullName(fullName);
            oldCustomer.setEmail(email);
            oldCustomer.setPhoneNumber(phone);
            oldCustomer.setAddress(address);

            if (password != null && !password.trim().isEmpty()) {
                oldCustomer.setPasswordHash(PasswordUtil.hashPassword(password));
            }
            
            if (profileImageUrl != null) {
            oldCustomer.setProfileImageUrl(profileImageUrl);
        }

            boolean result = customerDAO.update(oldCustomer); // Chọn hàm update
            if (result) {
                return "Edit successfully";
            } else {
                return "Edit false";
            }
        }
        return "Customer not found";
    }

    public boolean deleteCustomer(int id) {
        return customerDAO.delete(id);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerDAO.search(keyword);
    }

    public boolean isEmailExist(String email, int currentId) {
        return customerDAO.checkDuplicateEmail(email, currentId);
    }

    public boolean checkUsernameExists(String username) {
        return customerDAO.checkUsernameExists(username);
    }

    public List<Customer> getCustomersByPage(int page, int pageSize) {
        return customerDAO.findAll(page, pageSize);
    }

    public int getTotalPages(int pageSize) {
        long totalItems = customerDAO.getTotalCustomers();
        return (int) Math.ceil((double) totalItems / pageSize);
    }
}
