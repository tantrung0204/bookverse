/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.CustomerNotification;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CustomerNotificationDAO {

    public static List<CustomerNotification> getNotificationsByCustomer;

    public void create(CustomerNotification cn) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cn);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public long countSent(int notificationId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(cn) FROM CustomerNotification cn WHERE cn.notificationId.notificationId = :id",
                    Long.class
            ).setParameter("id", notificationId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public long countRead(int notificationId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(cn) FROM CustomerNotification cn WHERE cn.notificationId.notificationId = :id AND cn.isRead = true",
                    Long.class
            ).setParameter("id", notificationId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public void deleteByNotificationId(int notificationId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.createQuery("DELETE FROM CustomerNotification cn WHERE cn.notificationId.notificationId = :id")
                    .setParameter("id", notificationId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    public List<CustomerNotification> getNotificationsByCustomer(int customerId, int page, int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            int offset = Math.max(0, (page - 1) * pageSize);

            return em.createQuery(
                    "SELECT cn FROM CustomerNotification cn "
                    + "WHERE cn.customerId.customerId = :customerId "
                    + "ORDER BY cn.notificationId.createdAt DESC",
                    CustomerNotification.class)
                    .setParameter("customerId", customerId)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .getResultList();

        } finally {
            em.close();
        }
    }
    
    public void markAsRead(int id) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin();

            CustomerNotification cn = em.find(CustomerNotification.class, id);

            if (cn != null) {
                cn.setIsRead(true);
            }

            tx.commit();

        } finally {
            em.close();
        }
    }
    
    public CustomerNotification getById(int id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(CustomerNotification.class, id);
        } finally {
            em.close();
        }
    }



}
