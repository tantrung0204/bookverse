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

    public List<Customer> findAll() {
        // Khởi tạo entity manager
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Câu lệnh JPQL (Lấy đối tượng Customer)
//            String jpql = "SELECT c FROM Customer c";
//            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
//            
//            return query.getResultList();
            // Chỉ cần gọi tên định danh đã khai báo trong Model
            return em.createNamedQuery("Customer.findAll", Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
  
//    để gửi notification
    public List<Customer> getActiveCustomers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Customer c WHERE c.status = 1",
                    Customer.class
            ).getResultList();
        }finally {
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
            }
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
}
