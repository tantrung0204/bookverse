package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.OrderDAO;
import com.mycompany.bookverse.model.Order;
import java.util.List;

/**
 * @author huyqu
 */
public class OrderService {
    
    private OrderDAO orderDAO = new OrderDAO();

    public List<Order> getAllOrders(int page, int pageSize) {
        return orderDAO.findAll(page, pageSize);
    }

    public long getTotalOrders() {
        return orderDAO.getTotalOrders();
    }

    public Order getOrderById(int id) {
        return orderDAO.findById(id);
    }

    public List<Order> searchOrders(String keyword, int page, int pageSize) {
        return orderDAO.searchOrders(keyword, page, pageSize);
    }
    
    public long getTotalSearchOrders(String keyword) {
        return orderDAO.getTotalSearchOrders(keyword);
    }

    public String editOrder(int id, boolean isPaid, String orderStatus) {
        Order oldOrder = orderDAO.findById(id);
        if (oldOrder != null) {
            oldOrder.setIsPaid(isPaid);
            oldOrder.setOrderStatus(orderStatus);
            
            boolean result = orderDAO.update(oldOrder);
            return result ? "Edit successfully" : "Edit false";
        }
        return "Order not found";
    }
    
 
    public boolean cancelOrder(int id) {
        Order order = orderDAO.findById(id);
        if (order != null && !"Cancelled".equals(order.getOrderStatus())) {
            order.setOrderStatus("Cancelled");
            return orderDAO.update(order);
        }
        return false;
    }
}