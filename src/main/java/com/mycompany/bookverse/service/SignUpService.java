/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;
import com.mycompany.bookverse.dao.SignUpDAO;
import com.mycompany.bookverse.model.Customer;

public class SignUpService {

    private SignUpDAO signUpDAO = new SignUpDAO();

    private String hashMD5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }

    public String register(String username, String fullName, String email, String phone, String password) {
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
        
        newCustomer.setPasswordHash(hashMD5(password)); // Mã hóa
        newCustomer.setStatus(1); // Active
        newCustomer.setCreatedAt(new java.util.Date());

        boolean isSuccess = signUpDAO.registerCustomer(newCustomer);
        return isSuccess ? "Success" : "Failed to create account. Please try again.";
    }
}
