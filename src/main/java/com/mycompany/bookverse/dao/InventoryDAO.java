/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.ImportStock;
import com.mycompany.bookverse.model.ImportStockDetail;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.OrderItem;
import com.mycompany.bookverse.model.Product;
import com.mycompany.bookverse.model.Supplier;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class InventoryDAO {

    public long countAllImports(Date from, Date to) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            if (from != null && to != null) {
                return em.createQuery(
                        "SELECT COUNT(i) FROM ImportStock i "
                        + "WHERE i.createdAt BETWEEN :from AND :to",
                        Long.class
                )
                        .setParameter("from", from)
                        .setParameter("to", to)
                        .getSingleResult();
            }
            return em.createQuery(
                    "SELECT COUNT(i) FROM ImportStock i",
                    Long.class
            ).getSingleResult();

        } finally {
            em.close();
        }
    }

    public long countAllImportDetail(int importId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            return em.createQuery(
                    "SELECT COUNT(d) FROM ImportStockDetail d "
                    + "WHERE d.importId.importId = :importId",
                    Long.class
            )
                    .setParameter("importId", importId)
                    .getSingleResult();

        } finally {
            em.close();
        }
    }

    public long countAllExports(Date fromDate, Date toDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            if (fromDate != null && toDate != null) {
                String jpql = "SELECT COUNT(o) FROM Order o "
                        + "WHERE o.orderStatus NOT IN ('Pending','Cancelled') "
                        + "AND o.createdAt BETWEEN :fromDate AND :toDate";

                TypedQuery<Long> query = em.createQuery(jpql, Long.class);
                query.setParameter("fromDate", fromDate);
                query.setParameter("toDate", toDate);

                return query.getSingleResult();
            }
            return em.createQuery(
                    "SELECT COUNT(o) FROM Order o "
                    + "WHERE o.orderStatus NOT IN ('Pending','Cancelled')",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<ImportStock> findByImportPage(int offset, int limit, Date from, Date to) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            if (from != null && to != null) {
                TypedQuery<ImportStock> query
                        = em.createQuery(
                                "SELECT i FROM ImportStock i "
                                + "JOIN FETCH i.supplierId "
                                + "JOIN FETCH i.staffId "
                                + "WHERE i.createdAt BETWEEN :from AND :to "
                                + "ORDER BY i.importId DESC",
                                ImportStock.class
                        );

                query.setParameter("from", from);
                query.setParameter("to", to);
                query.setFirstResult(offset);
                query.setMaxResults(limit);

                return query.getResultList();
            }
            TypedQuery<ImportStock> query
                    = em.createQuery(
                            "SELECT i FROM ImportStock i "
                            + "JOIN FETCH i.supplierId "
                            + "JOIN FETCH i.staffId "
                            + "ORDER BY i.importId DESC",
                            ImportStock.class
                    );
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Order> findByExportPage(int offset, int limit, Date fromDate, Date toDate) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            if (fromDate != null && toDate != null) {
                String jpql = "SELECT o FROM Order o "
                        + "JOIN FETCH o.staffId "
                        + "WHERE o.orderStatus NOT IN ('Pending', 'Cancelled') "
                        + "AND o.createdAt BETWEEN :fromDate AND :toDate "
                        + "ORDER BY o.orderId DESC";

                TypedQuery<Order> query = em.createQuery(jpql, Order.class);
                query.setParameter("fromDate", fromDate);
                query.setParameter("toDate", toDate);
                query.setFirstResult(offset);
                query.setMaxResults(limit);
                return query.getResultList();
            }
            TypedQuery<Order> query
                    = em.createQuery(
                            "SELECT o FROM Order o "
                            + "JOIN FETCH o.staffId "
                            + "WHERE o.orderStatus NOT IN ('Pending', 'Cancelled') "
                            + "ORDER BY o.orderId DESC",
                            Order.class
                    );
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ImportStockDetail> findByImportId(int importId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            TypedQuery<ImportStockDetail> query
                    = em.createQuery(
                            "SELECT d FROM ImportStockDetail d "
                            + "JOIN FETCH d.importId i "
                            + "JOIN FETCH d.productId p "
                            + "WHERE i.importId = :importId "
                            + "ORDER BY d.importDetailId DESC",
                            ImportStockDetail.class
                    );

            query.setParameter("importId", importId);
            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public List<OrderItem> findByExportId(int exportId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            TypedQuery<OrderItem> query
                    = em.createQuery(
                            "SELECT oi FROM OrderItem oi "
                            + "JOIN FETCH oi.orderId o "
                            + "JOIN FETCH o.customerId c "
                            + "JOIN FETCH o.staffId s "
                            + "JOIN FETCH oi.productId p "
                            + "WHERE o.orderId = :orderId "
                            + "ORDER BY oi.orderItemId DESC",
                            OrderItem.class
                    );

            query.setParameter("orderId", exportId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Product> findAllProduct() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Product.findAll", Product.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Supplier> findAllSupplier() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Supplier.findAll", Supplier.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Supplier findSupplierById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Supplier.findBySupplierId", Supplier.class)
                    .setParameter("supplierId", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Product findProductById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Product.findByProductId", Product.class)
                    .setParameter("productId", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public int addImportStock(ImportStock importStock) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(importStock);
            em.flush(); // đảm bảo DB tạo id ngay
            int id = importStock.getImportId();
            em.getTransaction().commit();
            return id;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } finally {
            em.close();
        }
    }

    public boolean addImportStockDetail(ImportStockDetail importStockDetail) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(importStockDetail);

            // Update product stock quantity
            if (importStockDetail.getProductId() != null && importStockDetail.getImportedQuantity() != null) {
                Product product = em.find(Product.class, importStockDetail.getProductId().getProductId());
                if (product != null) {
                    int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
                    product.setStockQuantity(currentStock + importStockDetail.getImportedQuantity());
                    em.merge(product);
                }
            }

            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

}
