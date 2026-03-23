/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;
import com.mycompany.bookverse.dao.StatisticDAO;
import com.mycompany.bookverse.model.Order;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 *
 * @author huyqu
 */
public class StatisticService {

    private StatisticDAO statisticDAO = new StatisticDAO();

    public BigDecimal getTotalRevenue(Date startDate, Date endDate) {
        return statisticDAO.getTotalRevenue(startDate, endDate);
    }

    public long getTotalOrders(Date startDate, Date endDate) {
        return statisticDAO.getTotalOrders(startDate, endDate);
    }

    public long getTotalCustomers(Date startDate, Date endDate) {
        return statisticDAO.getTotalCustomers(startDate, endDate);
    }

    public Object[] getTopProduct(Date startDate, Date endDate) {
        return statisticDAO.getTopProduct(startDate, endDate);
    }

    public List<Object[]> getOrdersByStatus(Date startDate, Date endDate) {
        return statisticDAO.getOrdersByStatus(startDate, endDate);
    }

    public List<Order> getCompletedOrders(Date startDate, Date endDate) {
        return statisticDAO.getCompletedOrders(startDate, endDate);
    }
}
