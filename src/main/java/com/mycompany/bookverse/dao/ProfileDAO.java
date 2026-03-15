/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 *
 * @author LECOO
 */
public class ProfileDAO {

    public long countOderByCustomer(Customer customer) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        return em.createQuery(
                "SELECT COUNT(o) FROM Order o WHERE o.customerId = :customer",
                Long.class
        )
        .setParameter("customer", customer)
        .getSingleResult();
    } finally {
        em.close();
    }
}

    public long countFeedbackByCustomer(Customer customer) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        return em.createQuery(
                "SELECT COUNT(f) FROM Feedback f WHERE f.customerId = :customer",
                Long.class
        )
        .setParameter("customer", customer)
        .getSingleResult();
    } finally {
        em.close();
    }
}
}
