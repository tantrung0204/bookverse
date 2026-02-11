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
import org.hibernate.Hibernate;

/**
 *
 * @author TrungNT - CE200064
 */
public class ProductDAO {

    public List<Product> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Product> list = em.createNamedQuery("Product.findAll", Product.class)
                    .getResultList();

            for (Product p : list) {
                Hibernate.initialize(p.getFeedbackCollection());
                Hibernate.initialize(p.getOrderItemCollection());
            }

            return list;
        } finally {
            em.close();
        }
    }

    public Product findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Product p = em.find(Product.class, id);
            if (p != null) {
                Hibernate.initialize(p.getFeedbackCollection());
                Hibernate.initialize(p.getOrderItemCollection());

                if (p instanceof Book) {
                    Book b = (Book) p;
                    Hibernate.initialize(b.getAuthorCollection());
                }
            }
            return p;
        } finally {
            em.close();
        }
    }

    public List<Product> findByName(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Product> query = em.createNamedQuery("Product.findByName", Product.class);
            query.setParameter("keyword", "%" + keyword + "%");

            List<Product> list = query.getResultList();

            for (Product p : list) {
                Hibernate.initialize(p.getFeedbackCollection());
                Hibernate.initialize(p.getOrderItemCollection());
            }

            return list;
        } finally {
            em.close();
        }
    }

}
