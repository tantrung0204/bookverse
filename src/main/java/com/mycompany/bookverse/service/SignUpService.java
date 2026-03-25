/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;
import com.mycompany.bookverse.dao.SignUpDAO;
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.utils.PasswordUtil;

public class SignUpService {

    private SignUpDAO signUpDAO = new SignUpDAO();

    public String register(String username, String fullName, String email, String phone, Integer gender, String password) {
        if (signUpDAO.checkUsernameExists(username)) {
            return "Username is already taken!";
        }
        if (signUpDAO.checkEmailExists(email)) {
            return "Email is already registered!";
        }

        Customer newCustomer = new Customer();
        newCustomer.setUsername(username);
        newCustomer.setFullName(fullName);
        newCustomer.setEmail(email);
        newCustomer.setPhoneNumber(phone);
        if (gender != null) {
            newCustomer.setGender(gender);
        }
        
        newCustomer.setPasswordHash(PasswordUtil.hashPassword(password)); // Mã hóa
        newCustomer.setStatus(1); // Active
        newCustomer.setCreatedAt(new java.util.Date());

        boolean isSuccess = signUpDAO.registerCustomer(newCustomer);
        return isSuccess ? "Success" : "Failed to create account. Please try again.";
    }
}
