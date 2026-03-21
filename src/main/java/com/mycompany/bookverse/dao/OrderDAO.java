package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * @author huyqu
 */
public class OrderDAO {

    public List<Order> findAll(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            
            TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o ORDER BY o.orderId DESC", Order.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
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

    public Order findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Order.class, id);
        } finally {
            em.close();
        }
    }

    public boolean update(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(order);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    public List<Order> searchOrders(String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            
            String jpql = "SELECT o FROM Order o WHERE LOWER(CONCAT('o00', CAST(o.orderId AS string))) LIKE :kw OR LOWER(o.receiverPhone) LIKE LOWER(:kw) OR LOWER(o.customerId.fullName) LIKE LOWER(:kw) OR LOWER(o.orderStatus) LIKE LOWER(:kw) ORDER BY o.orderId DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long getTotalSearchOrders(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            
            String jpql = "SELECT COUNT(o) FROM Order o WHERE LOWER(CONCAT('o00', CAST(o.orderId AS string))) LIKE :kw OR LOWER(o.receiverPhone) LIKE LOWER(:kw) OR LOWER(o.customerId.fullName) LIKE LOWER(:kw) OR LOWER(o.orderStatus) LIKE LOWER(:kw)";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
}
