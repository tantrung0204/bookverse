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
import java.util.Date;
import java.util.List;
/**
 *
 * @author huyqu
 */
public class StatisticDAO {

    public BigDecimal getTotalRevenue(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderStatus = 'Completed'";
            if (startDate != null && endDate != null) {
                jpql += " AND o.createdAt >= :startDate AND o.createdAt <= :endDate";
            }
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            if (startDate != null && endDate != null) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }
            BigDecimal total = query.getSingleResult();
            return total != null ? total : BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    public long getTotalOrders(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(o) FROM Order o";
            if (startDate != null && endDate != null) {
                jpql += " WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            if (startDate != null && endDate != null) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public long getTotalCustomers(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM Customer c";
            if (startDate != null && endDate != null) {
                jpql += " WHERE c.createdAt >= :startDate AND c.createdAt <= :endDate";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            if (startDate != null && endDate != null) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public Object[] getTopProduct(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p.name, SUM(oi.orderQuantity) FROM OrderItem oi JOIN oi.productId p JOIN oi.orderId o "
                    + "WHERE o.orderStatus = 'Completed'";
            if (startDate != null && endDate != null) {
                jpql += " AND o.createdAt >= :startDate AND o.createdAt <= :endDate";
            }
            jpql += " GROUP BY p.name ORDER BY SUM(oi.orderQuantity) DESC";
            
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            if (startDate != null && endDate != null) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }
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

    public List<Object[]> getOrdersByStatus(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT o.orderStatus, COUNT(o) FROM Order o";
            if (startDate != null && endDate != null) {
                jpql += " WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate";
            }
            jpql += " GROUP BY o.orderStatus";
            
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            if (startDate != null && endDate != null) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Order> getCompletedOrders(Date startDate, Date endDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT o FROM Order o WHERE o.orderStatus = 'Completed'";
            if (startDate != null && endDate != null) {
                jpql += " AND o.createdAt >= :startDate AND o.createdAt <= :endDate";
            }
            jpql += " ORDER BY o.createdAt ASC";
            
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            if (startDate != null && endDate != null) {
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
