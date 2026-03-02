/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.NotificationDAO;
import com.mycompany.bookverse.model.Notification;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NotificationService {

    private final NotificationDAO dao = new NotificationDAO();

    public List<Notification> getAll() {
        return dao.getAll();
    }

    public Notification getById(int id) {
        return dao.findById(id);
    }

    public String create(Notification n) {

        if (n.getTitle() == null || n.getTitle().trim().isEmpty()) {
            return "Title cannot be empty";
        }

        if (n.getContentText() == null || n.getContentText().trim().isEmpty()) {
            return "Content cannot be empty";
        }

        n.setCreatedAt(new Date());
        dao.create(n);
        return "Create notification successfully";
    }

    public String update(Notification n) {

        Notification old = dao.findById(n.getNotificationId());
        if (old == null) {
            return "Notification not found";
        }

        if (n.getTitle() == null || n.getTitle().trim().isEmpty()) {
            return "Title cannot be empty";
        }

        if (n.getContentText() == null || n.getContentText().trim().isEmpty()) {
            return "Content cannot be empty";
        }

        n.setCreatedAt(old.getCreatedAt());
        dao.update(n);
        return "Update notification successfully";
    }

    public String delete(int id) {
        Notification n = dao.findById(id);
        if (n == null) {
            return "Notification not found";
        }

        dao.delete(id);
        return "Delete notification successfully";
    }

    public List<Notification> search(String keyword) {
        return dao.searchByTitle(keyword);
    }
}
