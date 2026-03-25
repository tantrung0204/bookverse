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

    public boolean isIsbnExists(String isbn, int excludeProductId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(b) FROM Book b WHERE LOWER(b.isbn) = LOWER(:isbn) AND b.productId != :id";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("isbn", isbn.trim())
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
            String sql = "SELECT b FROM Book b WHERE b.status = 1 "
                    + "ORDER BY (SELECT COALESCE(SUM(item.orderQuantity), 0) "
                    + "          FROM OrderItem item JOIN item.orderId o "
                    + "          WHERE item.productId = b AND o.orderStatus = 'Completed') DESC";
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
            String sql = "SELECT s FROM Stationery s WHERE s.status = 1 "
                    + "ORDER BY (SELECT COALESCE(SUM(item.orderQuantity), 0) "
                    + "          FROM OrderItem item JOIN item.orderId o "
                    + "          WHERE item.productId = s AND o.orderStatus = 'Completed') DESC";

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

    public List<Book> findBooksWithFilter(List<Integer> genreIds, String sortPrice, String keyword, int page,
            int pageSize) {
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
            long totalCount = 0;

            // Count Books
            StringBuilder bookSql = new StringBuilder("SELECT COUNT(b) FROM Book b WHERE b.status = 1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                bookSql.append(" AND LOWER(b.name) LIKE LOWER(:keyword)");
            }
            if (categoryId != null && categoryId > 0) {
                bookSql.append(" AND b.categoryId.categoryId = :categoryId");
            }
            TypedQuery<Long> bookQuery = em.createQuery(bookSql.toString(), Long.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                bookQuery.setParameter("keyword", "%" + keyword + "%");
            }
            if (categoryId != null && categoryId > 0) {
                bookQuery.setParameter("categoryId", categoryId);
            }
            totalCount += bookQuery.getSingleResult();

            // Count Stationery
            StringBuilder statSql = new StringBuilder("SELECT COUNT(s) FROM Stationery s WHERE s.status = 1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                statSql.append(" AND LOWER(s.name) LIKE LOWER(:keyword)");
            }
            if (categoryId != null && categoryId > 0) {
                statSql.append(" AND s.categoryId.categoryId = :categoryId");
            }
            TypedQuery<Long> statQuery = em.createQuery(statSql.toString(), Long.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                statQuery.setParameter("keyword", "%" + keyword + "%");
            }
            if (categoryId != null && categoryId > 0) {
                statQuery.setParameter("categoryId", categoryId);
            }
            totalCount += statQuery.getSingleResult();

            return totalCount;
        } finally {
            em.close();
        }
    }

    public List<Product> searchAllProducts(String sortPrice, String keyword, Integer categoryId, int page,
            int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Query Book and Stationery separately to avoid JOINED inheritance issues
            List<Product> allProducts = new ArrayList<>();

            // Query Books
            StringBuilder bookSql = new StringBuilder("SELECT b FROM Book b WHERE b.status = 1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                bookSql.append(" AND LOWER(b.name) LIKE LOWER(:keyword)");
            }
            if (categoryId != null && categoryId > 0) {
                bookSql.append(" AND b.categoryId.categoryId = :categoryId");
            }
            TypedQuery<Book> bookQuery = em.createQuery(bookSql.toString(), Book.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                bookQuery.setParameter("keyword", "%" + keyword + "%");
            }
            if (categoryId != null && categoryId > 0) {
                bookQuery.setParameter("categoryId", categoryId);
            }
            List<Book> books = bookQuery.getResultList();
            for (Book b : books) {
                b.getOrderItemCollection().size();
                b.getFeedbackCollection().size();
            }
            allProducts.addAll(books);

            // Query Stationery
            StringBuilder statSql = new StringBuilder("SELECT s FROM Stationery s WHERE s.status = 1");
            if (keyword != null && !keyword.trim().isEmpty()) {
                statSql.append(" AND LOWER(s.name) LIKE LOWER(:keyword)");
            }
            if (categoryId != null && categoryId > 0) {
                statSql.append(" AND s.categoryId.categoryId = :categoryId");
            }
            TypedQuery<Stationery> statQuery = em.createQuery(statSql.toString(), Stationery.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                statQuery.setParameter("keyword", "%" + keyword + "%");
            }
            if (categoryId != null && categoryId > 0) {
                statQuery.setParameter("categoryId", categoryId);
            }
            List<Stationery> stationeries = statQuery.getResultList();
            for (Stationery s : stationeries) {
                s.getOrderItemCollection().size();
                s.getFeedbackCollection().size();
            }
            allProducts.addAll(stationeries);

            // Sort the combined list
            if ("asc".equalsIgnoreCase(sortPrice)) {
                allProducts.sort((a, b1) -> a.getPrice().compareTo(b1.getPrice()));
            } else if ("desc".equalsIgnoreCase(sortPrice)) {
                allProducts.sort((a, b1) -> b1.getPrice().compareTo(a.getPrice()));
            } else {
                allProducts.sort((a, b1) -> b1.getProductId().compareTo(a.getProductId()));
            }

            // Apply pagination in Java
            int fromIndex = (page - 1) * pageSize;
            if (fromIndex >= allProducts.size()) {
                return new ArrayList<>();
            }
            int toIndex = Math.min(fromIndex + pageSize, allProducts.size());
            return allProducts.subList(fromIndex, toIndex);
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
