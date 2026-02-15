/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CustomerDAO;
import com.mycompany.bookverse.model.*;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();
    
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }
}
