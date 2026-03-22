/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.model.Notification;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NotificationDAO {

    public List<Notification> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Notification.findAll", Notification.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Notification findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Notification.findByNotificationId", Notification.class)
                    .setParameter("notificationId", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void create(Notification n) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(n);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Notification n = em.find(Notification.class, id);
            if (n == null) {
                return false;
            }

            em.getTransaction().begin();
            em.remove(n);
            em.getTransaction().commit();
            return true;
        } finally {
            em.close();
        }
    }

    public List<Notification> searchByTitle(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT n FROM Notification n WHERE n.title LIKE :kw",
                    Notification.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Notification> getByPage(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notification n ORDER BY n.notificationId DESC", Notification.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(n) FROM Notification n",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Notification> searchByPage(String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT n FROM Notification n WHERE n.title LIKE :kw ORDER BY n.notificationId DESC",
                    Notification.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countSearch(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(n) FROM Notification n WHERE n.title LIKE :kw",
                    Long.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

//    public static void main(String[] args) {
//        NotificationDAO dao = new NotificationDAO();
//        Notification n = new Notification();
//        dao.create(n);
//    }
    
}
