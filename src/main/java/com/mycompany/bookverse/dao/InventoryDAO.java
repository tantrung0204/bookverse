/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.ImportStock;
import com.mycompany.bookverse.model.ImportStockDetail;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class InventoryDAO {

    public long countAllImports() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
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

    public long countAllExports() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(o) FROM Order o",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<ImportStock> findByImportPage(int offset, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
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

    public List<Order> findByExportPage(int offset, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
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

    public List<ImportStockDetail> findByImportId(int importId, int offset, int limit) {
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
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();

        } finally {
            em.close();
        }
    }
}
