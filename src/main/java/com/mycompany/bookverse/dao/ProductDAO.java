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

    public List<Book> findTopSellingBooks(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT b FROM Book b "
                    + "ORDER BY (SELECT COALESCE(SUM(item.orderQuantity), 0) "
                    + "          FROM OrderItem item "
                    + "          WHERE item.productId = b) DESC";
            TypedQuery<Book> query = em.createQuery(sql, Book.class);
            query.setMaxResults(limit);
            List<Book> books = query.getResultList();
            for (Book b : books) {
                b.getOrderItemCollection().size();
                b.getFeedbackCollection().size();
            }
            return books;
        } finally {
            em.close();
        }
    }

    public List<Stationery> findTopSellingStationery(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT s FROM Stationery s "
                    + "ORDER BY (SELECT COALESCE(SUM(item.orderQuantity), 0) "
                    + "          FROM OrderItem item "
                    + "          WHERE item.productId = s) DESC";

            TypedQuery<Stationery> query = em.createQuery(sql, Stationery.class);
            query.setMaxResults(limit);

            List<Stationery> stationeries = query.getResultList();

            for (Stationery s : stationeries) {
                s.getOrderItemCollection().size();
                s.getFeedbackCollection().size();
            }
            return stationeries;
        } finally {
            em.close();
        }
    }

    public long countBooks(List<Integer> genreIds) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");
            if (genreIds != null && !genreIds.isEmpty()) {
                sql.append(" AND b.genreId.genreId IN :genreIds");
            }
            TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);

            if (genreIds != null && !genreIds.isEmpty()) {
                query.setParameter("genreIds", genreIds);
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Book> findBooksWithFilter(List<Integer> genreIds, String sortPrice, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");

            if (genreIds != null && !genreIds.isEmpty()) {
                sql.append(" AND b.genreId.genreId IN :genreIds");
            }

            if ("asc".equalsIgnoreCase(sortPrice)) {
                sql.append(" ORDER BY b.price ASC");
            } else if ("desc".equalsIgnoreCase(sortPrice)) {
                sql.append(" ORDER BY b.price DESC");
            } else {
                sql.append(" ORDER BY b.productId DESC");
            }
            TypedQuery<Book> query = em.createQuery(sql.toString(), Book.class);

            if (genreIds != null && !genreIds.isEmpty()) {
                query.setParameter("genreIds", genreIds);
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Book> books = query.getResultList();

            for (Book b : books) {
                b.getOrderItemCollection().size();
                b.getFeedbackCollection().size();
            }
            return books;
        } finally {
            em.close();
        }
    }

    public long countStationery() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(s) FROM Stationery s", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Stationery> findStationeryWithFilter(String sortPrice, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT s FROM Stationery s");

            if ("asc".equalsIgnoreCase(sortPrice)) {
                jpql.append(" ORDER BY s.price ASC");
            } else if ("desc".equalsIgnoreCase(sortPrice)) {
                jpql.append(" ORDER BY s.price DESC");
            } else {
                jpql.append(" ORDER BY s.productId DESC");
            }

            TypedQuery<Stationery> query = em.createQuery(jpql.toString(), Stationery.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Stationery> list = query.getResultList();
            for (Stationery s : list) {
                s.getOrderItemCollection().size();
                s.getFeedbackCollection().size();
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
}
