/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.OrderItem;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author Admin
 */
public class OrderItemDAO {

    public List<OrderItem> getByOrder(int orderId) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<OrderItem> q = em.createQuery(
                    "SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId = :oid",
                    OrderItem.class);

            q.setParameter("oid", orderId);

            return q.getResultList();

        } finally {
            em.close();
        }
    }
}
