/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import java.util.List;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import com.mycompany.bookverse.model.Feedback;
import java.util.Date;

/**
 *
 * @author TrungNT - CE200064
 */
public class FeedbackDAO {

    public List<Feedback> getPaginatedFeedbacksByProduct(int productId, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT f FROM Feedback f WHERE f.productId.productId = :productId ORDER BY f.feedbackId DESC";
            TypedQuery<Feedback> query = em.createQuery(sql, Feedback.class);
            query.setParameter("productId", productId);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Feedback> list = query.getResultList();

            for (Feedback fb : list) {
                if (fb.getCustomerId() != null) {
                    fb.getCustomerId().getFullName();
                    fb.getCustomerId().getProfileImageUrl();
                }
            }
            return list;
        } finally {
            em.close();
        }
    }

    public List<Feedback> getByPage(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Feedback> q = em.createQuery(
                    "SELECT f FROM Feedback f ORDER BY f.feedbackId DESC",
                    Feedback.class
            );
            q.setFirstResult((page - 1) * pageSize);
            q.setMaxResults(pageSize);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public long countAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(f) FROM Feedback f",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Feedback> search(String keyword, Integer rating, int page, int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        String jpql = "SELECT f FROM Feedback f WHERE 1=1";

        if (keyword != null && !keyword.isEmpty()) {
            jpql += " AND LOWER(f.productId.name) LIKE LOWER(:keyword)";
        }

        if (rating != null) {
            jpql += " AND f.rating = :rating";
        }

        jpql += " ORDER BY f.feedbackId DESC";

        TypedQuery<Feedback> query = em.createQuery(jpql, Feedback.class);

        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        if (rating != null) {
            query.setParameter("rating", rating);
        }

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public long countSearch(String keyword, Integer rating) {

        EntityManager em = JPAUtil.getEntityManager();

        String jpql = "SELECT COUNT(f) FROM Feedback f WHERE 1=1";

        if (keyword != null && !keyword.isEmpty()) {
            jpql += " AND LOWER(f.productId.name) LIKE LOWER(:keyword)";
        }

        if (rating != null) {
            jpql += " AND f.rating = :rating";
        }

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        if (rating != null) {
            query.setParameter("rating", rating);
        }

        return query.getSingleResult();
    }

    public Feedback findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Feedback.class, id);
    }

    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Feedback f = em.find(Feedback.class, id);
            if (f != null) {
                em.remove(f);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Feedback> getByCustomerId(int customerId) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql = "SELECT f FROM Feedback f WHERE f.customerId.customerId = :cid ORDER BY f.createdAt DESC";

            TypedQuery<Feedback> query = em.createQuery(jpql, Feedback.class);

            query.setParameter("cid", customerId);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public boolean create(int customerId, int productId, int rating, String content) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            Customer customer = em.find(Customer.class, customerId);
            Product product = em.find(Product.class, productId);

            if (customer == null || product == null) {
                throw new RuntimeException("Customer or Product not found");
            }

            Feedback feedback = new Feedback();

            feedback.setCustomerId(customer);
            feedback.setProductId(product);
            feedback.setRating(rating);
            feedback.setContentText(content);
            feedback.setCreatedAt(new Date());

            em.persist(feedback);

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

    public void update(Feedback feedback) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            em.merge(feedback);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public boolean isProductReviewed(int customerId, int productId) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            Long count = em.createQuery(
                    "SELECT COUNT(f) FROM Feedback f "
                    + "WHERE f.customerId.customerId = :cid "
                    + "AND f.productId.productId = :pid",
                    Long.class)
                    .setParameter("cid", customerId)
                    .setParameter("pid", productId)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }

}
