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

    public long countProductByCategory(int categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("Product.countByCategoryId", Long.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
            return count;
        } finally {
            em.close();
        }

    }

    public List<Product> findProductsHaveBook() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Product p WHERE TYPE(p) = Book",
                    Product.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Product> findProductsHaveStationery() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Stationery p WHERE TYPE(p) = Stationery",
                    Product.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Product insert(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(product);     // INSERT PRODUCT
            em.getTransaction().commit();
            return product;          // có product_id
        } finally {
            em.close();
        }
    }

    public Product getProductDetailById(int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Product p "
                    + "LEFT JOIN FETCH p.category "
                    + "LEFT JOIN FETCH p.feedbackCollection "
                    + "LEFT JOIN FETCH p.book b "
                    + "LEFT JOIN FETCH b.genre "
                    + "LEFT JOIN FETCH b.authorCollection "
                    + "LEFT JOIN FETCH p.stationery s "
                    + "WHERE p.productId = :id", Product.class)
                    .setParameter("id", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Product findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public double getAverageRating(int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Double avg = em.createQuery(
                    "SELECT AVG(f.rating) FROM Feedback f WHERE f.product.productId = :pid",
                    Double.class)
                    .setParameter("pid", productId)
                    .getSingleResult();
            return avg == null ? 0 : avg;
        } finally {
            em.close();
        }
    }

    public int getSoldQuantity(int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long total = em.createQuery(
                    "SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product.productId = :pid",
                    Long.class)
                    .setParameter("pid", productId)
                    .getSingleResult();
            return total == null ? 0 : total.intValue();
        } finally {
            em.close();
        }
    }
}
