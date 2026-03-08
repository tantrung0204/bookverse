/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.OrderDAO;
import com.mycompany.bookverse.dao.OrderItemDAO;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.OrderItem;
import java.util.List;

/**
 *
 * @author Admin
 */
public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDAO.getOrdersByCustomerId(customerId);
    }

    public Order getOrder(int orderId) {
        return orderDAO.findById(orderId);
    }

    public boolean cancelOrder(int orderId) {

        Order order = orderDAO.findById(orderId);

        if (order != null && "Pending".equalsIgnoreCase(order.getOrderStatus())) {

            order.setOrderStatus("Canceled");
            orderDAO.update(order);
            return true;
        }

        return false;
    }

    public boolean confirmReceived(int orderId) {

        Order order = orderDAO.findById(orderId);

        if (order != null && "Shipping".equalsIgnoreCase(order.getOrderStatus())) {

            order.setOrderStatus("Completed");
            orderDAO.update(order);
            return true;
        }

        return false;
    }

    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemDAO.getByOrder(orderId);
    }
}
