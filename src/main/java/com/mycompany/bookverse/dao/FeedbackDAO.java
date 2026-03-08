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

/**
 *
 * @author TrungNT - CE200064
 */
public class FeedbackDAO {

    public List<Feedback> getPaginatedFeedbacksByProduct(int productId, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT f FROM Feedback f WHERE f.productId.productId = :productId ORDER BY f.createdAt DESC";
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
}
