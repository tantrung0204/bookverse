/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author huyqu
 */
public class StatisticDAO {

    public BigDecimal getTotalRevenue() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            BigDecimal total = em.createQuery("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderStatus = 'Completed'", BigDecimal.class).getSingleResult();
            return total != null ? total : BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    public long getTotalOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public long getTotalCustomers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public Object[] getTopProduct() {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            String jpql = "SELECT p.name, SUM(oi.orderQuantity) FROM OrderItem oi JOIN oi.productId p JOIN oi.orderId o "
                    + "WHERE o.orderStatus = 'Completed' GROUP BY p.name ORDER BY SUM(oi.orderQuantity) DESC";
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setMaxResults(1);
            List<Object[]> result = query.getResultList();

            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
            return new Object[]{"No Data", 0};
        } catch (Exception e) {
            return new Object[]{"No Data", 0};
        } finally {
            em.close();
        }
    }

    public List<Object[]> getOrdersByStatus() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT o.orderStatus, COUNT(o) FROM Order o GROUP BY o.orderStatus", Object[].class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Order> getCompletedOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT o FROM Order o WHERE o.orderStatus = 'Completed' ORDER BY o.createdAt ASC", Order.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Order> getRecentOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o ORDER BY o.createdAt DESC", Order.class);
            query.setMaxResults(5);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
