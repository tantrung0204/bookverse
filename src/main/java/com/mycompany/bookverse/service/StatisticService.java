/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;
import com.mycompany.bookverse.dao.StatisticDAO;
import com.mycompany.bookverse.model.Order;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author huyqu
 */
public class StatisticService {

    private StatisticDAO statisticDAO = new StatisticDAO();

    public BigDecimal getTotalRevenue() {
        return statisticDAO.getTotalRevenue();
    }

    public long getTotalOrders() {
        return statisticDAO.getTotalOrders();
    }

    public long getTotalCustomers() {
        return statisticDAO.getTotalCustomers();
    }

    public Object[] getTopProduct() {
        return statisticDAO.getTopProduct();
    }

    public List<Object[]> getOrdersByStatus() {
        return statisticDAO.getOrdersByStatus();
    }

    public List<Order> getCompletedOrders() {
        return statisticDAO.getCompletedOrders();
    }

    public List<Order> getRecentOrders() {
        return statisticDAO.getRecentOrders();
    }
}
