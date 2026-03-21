/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.StaffDAO;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.PasswordUtil;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class StaffService {

    private StaffDAO staffDAO;

    public StaffService() {
        this.staffDAO = new StaffDAO();
    }

    public List<Staff> getAllStaffs(int page, int pageSize) {
        return staffDAO.findAll(page, pageSize);
    }

    public long getTotalStaffs() {
        return staffDAO.getTotalStaffs();
    }

    public Staff getStaffById(int id) {
        return staffDAO.findById(id);
    }

    public String insertStaff(String fullName, String username, String password, String roleName) {
        String error = "";

        if (fullName == null || fullName.trim().isEmpty()) {
            error += "Full Name can not be empty.\n";
        }

        if (username == null || username.trim().isEmpty()) {
            error += "Username can not be empty.\n";
        }

        if (staffDAO.checkUsernameExists(username)) {
            return "Username already exists!";
        }

        if (!error.isEmpty()) {
            return error;
        }

        Staff newStaff = new Staff();
        newStaff.setFullName(fullName);
        newStaff.setUsername(username);
        newStaff.setPasswordHash(PasswordUtil.hashPassword(password));
        newStaff.setStatus(1);
        newStaff.setCreatedAt(new java.util.Date());
        newStaff.setRoleName(roleName);
        newStaff.setProfileImageUrl("assets/images/default-avt.jpg");

        boolean result = staffDAO.create(newStaff);
        if (result) {
            return "Create successfully";
        } else {
            return "Create false";
        }

    }

    public String editStaff(int id, String fullName, String password, String profileImageUrl, String roleName) {

        String error = "";

        if (fullName == null || fullName.trim().isEmpty()) {
            error += "Full Name can not be empty.\n";
        }

        if (!error.isEmpty()) {
            return error;
        }

        Staff oldStaff = staffDAO.findById(id);
        if (oldStaff != null) {
            oldStaff.setFullName(fullName);
            oldStaff.setRoleName(roleName); // Cập nhật role

            if (password != null && !password.trim().isEmpty()) {
                oldStaff.setPasswordHash(PasswordUtil.hashPassword(password));
            }
            if (profileImageUrl != null) {
                oldStaff.setProfileImageUrl(profileImageUrl);
            }

            boolean result = staffDAO.update(oldStaff);

            if (result) {
                return "Edit successfully";
            } else {
                return "Edit false";
            }
        }
        return "Staff not found";

    }

    public int editStaff(Staff staff) {
        if (staff.getFullName() == null || staff.getFullName().trim().isEmpty()) {
            return 2;
        }

        boolean success = staffDAO.update(staff);
        if (success) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean deleteStaff(int id) {
        return staffDAO.delete(id);
    }

    public List<Staff> searchStaffs(String keyword) {
        return staffDAO.search(keyword);
    }

    public Staff signin(String username, String password) {
        Staff staff = staffDAO.findByUsername(username);
        if (staff != null) {
            if (staff.getStatus() != 1) {
                return null;
            }
            if (PasswordUtil.checkPassword(password, staff.getPasswordHash())) {
                return staff;
            }
        }
        return null;
    }
}
