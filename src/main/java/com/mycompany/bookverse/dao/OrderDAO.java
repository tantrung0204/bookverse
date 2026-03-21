
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author TrungNT - CE200064
 */
public class OrderDAO {

    public List<Order> findAll(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            TypedQuery<Order> query = em.createQuery("SELECT o FROM Order o ORDER BY o.orderId DESC", Order.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long getTotalOrders() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Find an Order by its ID.
     */
    public Order findById(int orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Order.class, orderId);
        } finally {
            em.close();
        }
    }

    public List<Order> searchOrders(String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            String jpql = "SELECT o FROM Order o WHERE LOWER(CONCAT('o00', CAST(o.orderId AS string))) LIKE :kw OR LOWER(o.receiverPhone) LIKE LOWER(:kw) OR LOWER(o.customerId.fullName) LIKE LOWER(:kw) OR LOWER(o.orderStatus) LIKE LOWER(:kw) ORDER BY o.orderId DESC";
            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long getTotalSearchOrders(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            String jpql = "SELECT COUNT(o) FROM Order o WHERE LOWER(CONCAT('o00', CAST(o.orderId AS string))) LIKE :kw OR LOWER(o.receiverPhone) LIKE LOWER(:kw) OR LOWER(o.customerId.fullName) LIKE LOWER(:kw) OR LOWER(o.orderStatus) LIKE LOWER(:kw)";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Persist a new Order entity and return the generated orderId.
     */
    public Order createOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
            return order;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Persist a new OrderItem entity.
     */
    public void createOrderItem(OrderItem item) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Update an existing Order (e.g. set isPaid = true after VNPay callback).
     */
    public void updateOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(order);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Deduct stock quantity for a product.
     */
    public void updateProductStock(int productId, int quantityToDeduct) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);
            if (product != null) {
                int newStock = product.getStockQuantity() - quantityToDeduct;
                if (newStock < 0) {
                    newStock = 0;
                }
                product.setStockQuantity(newStock);
                em.merge(product);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Find a fresh Product by ID (to check real-time stock).
     */
    public Product findProductById(int productId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Product.class, productId);
        } finally {
            em.close();
        }
    }

    /**
     * Get available vouchers: status = 1, availableQuantity > 0,
     * startDate <= now, expiryDate > now.
     */
    public List<Voucher> findAvailableVouchers(BigDecimal subtotal) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            String jpql = "SELECT v FROM Voucher v "
                    + "WHERE v.status = 1 "
                    + "AND v.availableQuantity > 0 "
                    + "AND v.startDate <= :now "
                    + "AND v.expiryDate > :now "
                    + "AND (v.minOrderValue IS NULL OR v.minOrderValue <= :subtotal) "
                    + "ORDER BY v.expiryDate ASC";

            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);

            query.setParameter("now", new Date());
            query.setParameter("subtotal", subtotal);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    /**
     * Find a voucher by its code.
     */
    public Voucher findVoucherByCode(String code) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Voucher> query = em.createQuery(
                    "SELECT v FROM Voucher v WHERE v.voucherCode = :code", Voucher.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Decrement voucher's availableQuantity by 1.
     */
    public void decrementVoucherQuantity(int voucherId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Voucher v = em.find(Voucher.class, voucherId);
            if (v != null && v.getAvailableQuantity() > 0) {
                v.setAvailableQuantity(v.getAvailableQuantity() - 1);
                em.merge(v);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Delete cart items for a customer (after successful checkout from cart).
     */
    public void clearCart(int customerId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Cart c WHERE c.customerId.customerId = :cid")
                    .setParameter("cid", customerId)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<Order> query = em.createQuery(
                    "SELECT o FROM Order o WHERE o.customerId.customerId = :cid ORDER BY o.createdAt DESC",
                    Order.class);

            query.setParameter("cid", customerId);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    public boolean update(Order order) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(order);
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
