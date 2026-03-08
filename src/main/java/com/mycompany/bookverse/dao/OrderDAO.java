/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
/**
 *
 * @author Admin
 */
public class OrderDAO {
     public List<Order> getOrdersByCustomerId(int customerId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o WHERE o.customerId.customerId = :cid ORDER BY o.createdAt DESC",
                    Order.class);

            query.setParameter("cid", customerId);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public Order findById(int orderId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Order.class, orderId);
        } finally {
            em.close();
        }
    }

    public void update(Order order) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
