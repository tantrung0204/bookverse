/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author huyqu
 */
public class StaffDAO {

    public List<Staff> findAll(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Staff> query = em.createQuery("SELECT s FROM Staff s ORDER BY s.staffId DESC", Staff.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long getTotalStaffs() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(s) FROM Staff s", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public Staff findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Staff.class, id);
        } finally {
            em.close();
        }
    }

    public boolean create(Staff staff) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(staff);
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

    public boolean update(Staff staff) {
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

    public boolean delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Staff staff = em.find(Staff.class, id);
            if (staff == null) {
                return false;
            }
            em.getTransaction().begin();
            em.remove(staff);
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

    public List<Staff> search(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String hql = "SELECT s FROM Staff s WHERE LOWER(s.fullName) LIKE LOWER(:keyword) OR LOWER(s.username) LIKE LOWER(:keyword) ORDER BY s.staffId DESC";
            TypedQuery<Staff> query = em.createQuery(hql, Staff.class);
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
            return query.getResultList();
        } finally { em.close(); }
    }
    
    public boolean checkUsernameExists(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM Staff c WHERE c.username = :username";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // ==========================================
    // CÁC HÀM DÀNH CHO SIGN IN
    // ==========================================
    public Staff findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Staff> query = em.createNamedQuery("Staff.findByUsername", Staff.class);
            query.setParameter("username", username);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}
