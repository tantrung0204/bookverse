/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Voucher;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VoucherDAO {

    public Voucher findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Voucher.findByVoucherId", Voucher.class)
                    .setParameter("voucherId", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public boolean existsByCode(String code) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(v) FROM Voucher v WHERE v.voucherCode = :code",
                    Long.class)
                    .setParameter("code", code)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void create(Voucher voucher) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(voucher);
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

    public List<Voucher> searchVoucher(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String kw = "%" + keyword.trim().toLowerCase() + "%";

            return em.createQuery(
                    "SELECT v FROM Voucher v "
                    + "WHERE LOWER(v.voucherName) LIKE :kw "
                    + "OR LOWER(v.voucherCode) LIKE :kw",
                    Voucher.class)
                    .setParameter("kw", kw)
                    .getResultList();

        } finally {
            em.close();
        }
    }
    
    public static void main(String[] args) {
        VoucherDAO dao = new VoucherDAO();
        for (Voucher voucher : dao.searchVoucher("K")) {
            System.out.println(voucher.toString());
        }
    }

    public boolean existsByCodeExceptId(String code, int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(v) FROM Voucher v WHERE v.voucherCode = :code AND v.voucherId <> :id",
                    Long.class)
                    .setParameter("code", code)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void update(Voucher voucher) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(voucher);
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

    public boolean deleteById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Voucher v = em.find(Voucher.class, id);
            if (v == null) {
                return false;
            }

            em.getTransaction().begin();
            em.remove(v);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public long countUsedVoucher(int voucherId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(o) FROM Order o "
                    + "WHERE o.voucherId.voucherId = :vid "
                    + "AND o.orderStatus NOT IN ('pending', 'cancelled')",
                    Long.class)
                    .setParameter("vid", voucherId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Voucher> getVouchersPaging(int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT v FROM Voucher v ORDER BY v.voucherId DESC", Voucher.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long getTotalVoucherCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(v) FROM Voucher v", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Voucher> searchByCodePaging(String keyword, int page, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Voucher v "
                    + "WHERE LOWER(v.voucherName) LIKE :kw "
                    + "OR LOWER(v.voucherCode) LIKE :kw ORDER BY v.voucherId DESC",
                    Voucher.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countSearchVoucher(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(v) FROM Voucher v WHERE v.voucherCode LIKE :kw",
                    Long.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

}
