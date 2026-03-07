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
import java.util.ArrayList;

/**
 *
 * @author TrungNT - CE200064
 */
public class ProductDAO {

    // ==========================================
    // CÁC HÀM DÀNH CHO TRANG QUẢN TRỊ (ADMIN)
    // ==========================================
    public List<Book> findAdminBooks(String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT b FROM Book b WHERE 1=1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(b.name) LIKE LOWER(:keyword)");
            }
            // Sắp xếp ID lớn đến nhỏ
            sql.append(" ORDER BY b.productId DESC");
            TypedQuery<Book> query = em.createQuery(sql.toString(), Book.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countAdminBooks(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE 1=1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(b.name) LIKE LOWER(:keyword)");
            }
            TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Stationery> findAdminStationeries(String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT s FROM Stationery s WHERE 1=1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(s.name) LIKE LOWER(:keyword)");
            }
            sql.append(" ORDER BY s.productId DESC");
            TypedQuery<Stationery> query = em.createQuery(sql.toString(), Stationery.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countAdminStationeries(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(s) FROM Stationery s WHERE 1=1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(s.name) LIKE LOWER(:keyword)");
            }
            TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            return query.getSingleResult();
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

    public boolean isProductNameExists(String name, int excludeProductId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) = LOWER(:name) AND p.productId != :id";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("name", name.trim())
                    .setParameter("id", excludeProductId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean createProduct(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (product.getCategoryId() != null) {
                product.setCategoryId(em.getReference(Category.class, product.getCategoryId().getCategoryId()));
            }

            if (product instanceof Book) {
                Book book = (Book) product;
                if (book.getGenreId() != null) {
                    book.setGenreId(em.getReference(Genre.class, book.getGenreId().getGenreId()));
                }
                if (book.getAuthorCollection() != null) {
                    List<Author> attachedAuthors = new ArrayList<>();
                    for (Author a : book.getAuthorCollection()) {
                        attachedAuthors.add(em.getReference(Author.class, a.getAuthorId()));
                    }
                    book.setAuthorCollection(attachedAuthors);
                }
            }

            em.persist(product);
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

    public boolean updateProduct(Product product) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(product);
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

    public boolean deleteProduct(int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);

            if (product != null) {
                em.remove(product);
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

    // ==========================================
    // CÁC HÀM DÀNH CHO TRANG HOME
    // ==========================================
    public List<Book> findTopSellingBooks(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT b FROM Book b WHERE b.status = 1"
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
            String sql = "SELECT s FROM Stationery s WHERE s.status = 1"
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

    public long countBooks(List<Integer> genreIds, String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE b.status = 1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(b.name) LIKE LOWER(:keyword)");
            }

            if (genreIds != null && !genreIds.isEmpty()) {
                sql.append(" AND b.genreId.genreId IN :genreIds");
            }

            TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }

            if (genreIds != null && !genreIds.isEmpty()) {
                query.setParameter("genreIds", genreIds);
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Book> findBooksWithFilter(List<Integer> genreIds, String sortPrice, String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT b FROM Book b WHERE b.status = 1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(b.name) LIKE LOWER(:keyword)");
            }

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

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }

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

    public long countStationery(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(s) FROM Stationery s WHERE s.status = 1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(s.name) LIKE LOWER(:keyword)");
            }
            TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Stationery> findStationeryWithFilter(String sortPrice, String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT s FROM Stationery s WHERE s.status = 1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(s.name) LIKE LOWER(:keyword)");
            }

            if ("asc".equalsIgnoreCase(sortPrice)) {
                sql.append(" ORDER BY s.price ASC");
            } else if ("desc".equalsIgnoreCase(sortPrice)) {
                sql.append(" ORDER BY s.price DESC");
            } else {
                sql.append(" ORDER BY s.productId DESC");
            }

            TypedQuery<Stationery> query = em.createQuery(sql.toString(), Stationery.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
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

    public long countAllProducts(String keyword, Integer categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE p.status = 1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(p.name) LIKE LOWER(:keyword)");
            }

            if (categoryId != null && categoryId > 0) {
                sql.append(" AND p.categoryId.categoryId = :categoryId");
            }

            TypedQuery<Long> query = em.createQuery(sql.toString(), Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }

            if (categoryId != null && categoryId > 0) {
                query.setParameter("categoryId", categoryId);
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Product> searchAllProducts(String sortPrice, String keyword, Integer categoryId, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder sql = new StringBuilder("SELECT p FROM Product p WHERE p.status = 1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(p.name) LIKE LOWER(:keyword)");
            }

            if (categoryId != null && categoryId > 0) {
                sql.append(" AND p.categoryId.categoryId = :categoryId");
            }

            if ("asc".equalsIgnoreCase(sortPrice)) {
                sql.append(" ORDER BY p.price ASC");
            } else if ("desc".equalsIgnoreCase(sortPrice)) {
                sql.append(" ORDER BY p.price DESC");
            } else {
                sql.append(" ORDER BY p.productId DESC");
            }

            TypedQuery<Product> query = em.createQuery(sql.toString(), Product.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }

            if (categoryId != null && categoryId > 0) {
                query.setParameter("categoryId", categoryId);
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Product> list = query.getResultList();

            for (Product p : list) {
                p.getOrderItemCollection().size();
                p.getFeedbackCollection().size();
            }
            return list;
        } finally {
            em.close();
        }
    }

    public Product findProductById(int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Product product = em.find(Product.class, productId);

            if (product != null) {
                if (product.getCategoryId() != null) {
                    product.getCategoryId().getCategoryName();
                }

                product.getOrderItemCollection().size();
                product.getFeedbackCollection().size();

                if (product instanceof Book) {
                    ((Book) product).getAuthorCollection().size();
                }
            }
            return product;
        } finally {
            em.close();
        }
    }

    public List<Product> findRelatedProducts(Product product, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT p FROM Product p WHERE p.status = 1 AND p.categoryId = :category AND p.productId != :id";
            TypedQuery<Product> query = em.createQuery(sql, Product.class);
            query.setParameter("category", product.getCategoryId());
            query.setParameter("id", product.getProductId());
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
