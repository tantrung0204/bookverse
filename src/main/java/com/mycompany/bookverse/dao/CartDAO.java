/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import java.util.List;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author TrungNT - CE200064
 */
public class CartDAO {

    public List<Cart> getCartByCustomerId(int customerId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT c FROM Cart c WHERE c.customerId.customerId = :customerId";
            TypedQuery<Cart> query = em.createQuery(sql, Cart.class);
            query.setParameter("customerId", customerId);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Cart findByCustomerAndProduct(int customerId, int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT c FROM Cart c WHERE c.customerId.customerId = :cid AND c.productId.productId = :pid";
            TypedQuery<Cart> query = em.createQuery(sql, Cart.class);
            query.setParameter("cid", customerId);
            query.setParameter("pid", productId);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Cart findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cart.class, id);
        } finally {
            em.close();
        }

    }

    public void save(Cart cartItem) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (cartItem.getCartId() == null) {
                // Chưa có ID -> Thêm mới
                em.persist(cartItem);
            } else {
                // Đã có ID -> Cập nhật
                em.merge(cartItem);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void delete(int cartId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Cart cartItem = findById(cartId);

            if (cartItem != null) {
                em.remove(cartItem);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
