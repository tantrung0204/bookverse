/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.CustomerNotification;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 *
 * @author Admin
 */
public class CustomerNotificationDAO {

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

}
