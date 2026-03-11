/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CustomerDAO;
import com.mycompany.bookverse.dao.CustomerNotificationDAO;
import com.mycompany.bookverse.dao.NotificationDAO;
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.CustomerNotification;
import com.mycompany.bookverse.model.Notification;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NotificationService {

    private final NotificationDAO dao = new NotificationDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final CustomerNotificationDAO customerNotificationDAO = new CustomerNotificationDAO();

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
        List<Customer> activeCustomers = customerDAO.getActiveCustomers();

        for (Customer c : activeCustomers) {
            CustomerNotification cn = new CustomerNotification();
            cn.setCustomerId(c);
            cn.setNotificationId(n);
            cn.setIsRead(false);

            customerNotificationDAO.create(cn);
        }
        return "Create notification successfully";
    }

    public String delete(int id) {
        Notification n = dao.findById(id);
        if (n == null) {
            return "Notification not found";
        }
        customerNotificationDAO.deleteByNotificationId(id);
        dao.delete(id);
        return "Delete notification successfully";
    }

    public List<Notification> search(String keyword) {
        return dao.searchByTitle(keyword);
    }

    public List<Notification> getByPage(int page, int pageSize) {
        return dao.getByPage(page, pageSize);
    }

    public int getTotalPages(int pageSize) {
        long totalItems = dao.countAll();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public long getTotalSent(int notificationId) {
        return customerNotificationDAO.countSent(notificationId);
    }

    public long getTotalRead(int notificationId) {
        return customerNotificationDAO.countRead(notificationId);
    }

    public List<Notification> searchByPage(String keyword, int page, int pageSize) {
        return dao.searchByPage(keyword, page, pageSize);
    }

    public int getTotalSearchPages(String keyword, int pageSize) {
        long totalItems = dao.countSearch(keyword);
        return (int) Math.ceil((double) totalItems / pageSize);
    }
    
    public List<CustomerNotification> getCustomerNotifications(int customerId, int page, int pageSize) {
        return customerNotificationDAO.getNotificationsByCustomer(customerId, page, pageSize);
    }

}
