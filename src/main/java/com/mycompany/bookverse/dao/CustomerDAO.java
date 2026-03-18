/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class CustomerDAO {

    public List<Customer> findAll(int page, int pageSize) {
        // Khởi tạo entity manager
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Câu lệnh JPQL (Lấy đối tượng Customer)
            String jpql = "SELECT c FROM Customer c ORDER BY c.customerId DESC";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setMaxResults(com.mycompany.bookverse.utils.PaginationConfig.ADMIN_ITEMS_PER_PAGE);
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.remove(customer);
                em.getTransaction().commit();
                return true;
            }
            return false;
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

    public List<Customer> search(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String hql = "SELECT c FROM Customer c WHERE c.fullName LIKE :keyword OR c.email LIKE :keyword OR c.phoneNumber LIKE :keyword";
            TypedQuery<Customer> query = em.createQuery(hql, Customer.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean checkDuplicateEmail(String email, int currentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Tìm xem có email nào giống vậy mà ID khác với ông hiện tại không
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

    // ==========================================
    // SIGN IN
    // ==========================================
    public Customer findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createNamedQuery("Customer.findByUsername", Customer.class);
            query.setParameter("username", username);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    // ==========================================
    // FORGOT PASSWORD
    // ==========================================
    public Customer findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Customer> query = em.createNamedQuery("Customer.findByEmail", Customer.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
}
