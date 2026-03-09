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

    public int addStaff(Staff staff) {

        // if (staff.getFullName() == null || staff.getFullName().trim().isEmpty()
        // || staff.getEmail() == null || staff.getEmail().trim().isEmpty()) {
        // return 2;
        // }

        boolean success = staffDAO.create(staff);
        if (success) {
            return 1;
        } else {
            return 0;
        }
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
