/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

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
    public boolean updateStaff(Staff staff) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(staff);
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
    public boolean updateCustomer(Customer customer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(customer);
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
    public List<Customer> getAllCustomer(){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Customer.findAll", Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
