/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.ProfileDAO;
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.utils.PasswordUtil;

/**
 *
 * @author LECOO
 */
public class ProfileService {

    private ProfileDAO profileDAO = new ProfileDAO();

    public long CountOder(Customer customer) {
        return profileDAO.countOderByCustomer(customer);
    }

    public long countFeedback(Customer customer) {
        return profileDAO.countFeedbackByCustomer(customer);
    }

    public boolean updateInforForStaff(Staff staff) {
        return profileDAO.updateStaff(staff);
    }

    public boolean updateInforForCustomer(Customer customer) {
        return profileDAO.updateCustomer(customer);
    }

    public String CheckValidEditInformation(String fullName, String phoneNumber, String address, String role, int id) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Full name can't be empty.";
        }
        if (!fullName.matches("^[A-Za-zÀ-ỹ0-9]+( [A-Za-zÀ-ỹ0-9]+)*$")) {
            return "Full name contains invalid characters.";
        }
        if (role.equals("customer")) {

            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return "Phone number can't be empty.";
            }
            if (!phoneNumber.matches("^0(3|5|7|8|9)[0-9]{8}$")) {
                return "Phone number must start with 0 and contain 10 digits...";
            }
            for (Customer customer : profileDAO.getAllCustomer()) {
                if (customer.getPhoneNumber().equalsIgnoreCase(phoneNumber.trim()) && id != customer.getCustomerId()) {
                    return "This phone number is already in use, please enter a different phone number.";
                }
            }
            if (address == null || address.trim().isEmpty()) {
                return "Address can't be empty.";
            }
            if (!address.matches("^[a-zA-Z0-9À-ỹ\\s,./-]{5,100}$")) {
                return "Invalid address. It must be 5–100 characters and only include letters, numbers, spaces, and , . / -";
            }
        }
        return "";
    }

    public String CheckValidChangePassword(String oldPass, String newPass, String reNewPass, Customer customer, Staff staff) {
        if (oldPass == null || oldPass.trim().isEmpty()) {
            return "Old password can't be empty.<br>";
        }
        if (!oldPass.matches("^[a-zA-Z0-9!@#$%^&*]{8,20}$")) {
            return "old password must be 8–16 characters and only contain letters, numbers or !@#$%^&*<br>";
        }
        if (customer != null) {
            if (!PasswordUtil.checkPassword(oldPass, customer.getPasswordHash())) {//kiểm tra mật khẩu cũ có đúng ko.
                return "The current password is incorrect.<br>";
            }
        } else {
            if (!PasswordUtil.checkPassword(oldPass, staff.getPasswordHash())) {//kiểm tra mật khẩu cũ có đúng ko.
                return "The current password is incorrect.<br>";
            }
        }
        if (newPass == null || newPass.trim().isEmpty()) {
            return "New password can't be empty.<br>";
        }
        if (!newPass.matches("^[a-zA-Z0-9!@#$%^&*]{8,20}$")) {
            return "New password must be 8–16 characters and only contain letters, numbers or !@#$%^&*<br>";
        }
        if(newPass.equalsIgnoreCase(oldPass)){
            return"The new password must not be the same as the old password, please re-enter it.";
        }
        if (reNewPass == null || reNewPass.trim().isEmpty()) {
            return "Confirm password can't be empty.<br>";
        }
        if (!reNewPass.matches("^[a-zA-Z0-9!@#$%^&*]{8,20}$")) {
            return "Confirm password must be 8–16 characters and only contain letters, numbers or !@#$%^&*<br>";
        }
        if (!newPass.equals(reNewPass)) {
            return "New password and confirm password do not match.<br>";
        }
        return "";
    }
}
