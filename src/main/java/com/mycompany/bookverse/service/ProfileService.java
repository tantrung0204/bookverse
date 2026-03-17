/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CustomerDAO;
import com.mycompany.bookverse.dao.ProfileDAO;
import com.mycompany.bookverse.dao.StaffDAO;
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.Staff;

/**
 *
 * @author LECOO
 */
public class ProfileService {

    private ProfileDAO profileDAO = new ProfileDAO();
    private StaffDAO staffDAO= new StaffDAO();
    private CustomerDAO customerDAO= new CustomerDAO();
    public long CountOder(Customer customer) {
        return profileDAO.countOderByCustomer(customer);
    }

    public long countFeedback(Customer customer) {
        return profileDAO.countFeedbackByCustomer(customer);
    }
    public boolean updateInforForStaff(Staff staff){
        return staffDAO.update(staff);
    }
    public boolean updateInforForCustomer(Customer customer){
        return customerDAO.update(customer);
    }
}
