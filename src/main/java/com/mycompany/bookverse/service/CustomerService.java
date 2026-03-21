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

    public int addCustomer(Customer customer) {

        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()
                || customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            return 2;
        }

        boolean success = customerDAO.create(customer);
        if (success) {
            return 1;
        } else {
            return 0;
        }
    }

    public int editCustomer(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            return 2;
        }

        boolean success = customerDAO.update(customer);
        if (success) {
            return 1;
        } else {
            return 0;
        }
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

    public Customer signin(String username, String password) {
        Customer customer = customerDAO.findByUsername(username);
        System.out.println(customer);
        System.out.println(password);
        System.out.println(customer.getPasswordHash());
        System.out.println(PasswordUtil.checkPassword(password, customer.getPasswordHash()));
        if (customer != null) {
            if (customer.getStatus() != 1) {
                return null;
            }
            if (PasswordUtil.checkPassword(password, customer.getPasswordHash())) {
                return customer;
            }
        }
        return null;
    }

    // ==========================================
    // FORGOT PASSWORD
    // ==========================================
    public Customer getCustomerByEmail(String email) {
        return customerDAO.findByEmail(email);
    }

    public boolean updatePassword(String email, String newPlainPassword) {
        Customer customer = customerDAO.findByEmail(email);
        if (customer != null) {
            customer.setPasswordHash(PasswordUtil.hashPassword(newPlainPassword));
            return customerDAO.update(customer);
        }
        return false;
    }
}
