/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class CustomerDAO {

    public List<Customer> findAll(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c ORDER BY c.customerId DESC", Customer.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long getTotalCustomers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    // để gửi notification
    public List<Customer> getActiveCustomers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Customer c WHERE c.status = 1",
                    Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Customer findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Customer.class, id); 
        } finally {
            em.close();
        }
    }

    public boolean create(Customer customer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public boolean update(Customer customer) {
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
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Customer customer = em.find(Customer.class, id);
            if (customer == null) {
                return false;
            }
            em.getTransaction().begin();
            em.remove(customer);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public List<Customer> search(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            String hql = "SELECT c FROM Customer c WHERE LOWER(c.fullName) LIKE LOWER(:keyword) OR LOWER(c.email) LIKE LOWER(:keyword) OR c.phoneNumber LIKE :keyword ORDER BY c.customerId DESC";
            TypedQuery<Customer> query = em.createQuery(hql, Customer.class);
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean checkDuplicateEmail(String email, int currentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM Customer c WHERE c.email = :email AND c.customerId != :id";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("email", email)
                    .setParameter("id", currentId)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public boolean checkUsernameExists(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM Customer c WHERE c.username = :username";
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

}
