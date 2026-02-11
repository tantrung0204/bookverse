/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Voucher;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VoucherDAO {

    public List<Voucher> getAllVouchers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Voucher.findAll", Voucher.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Voucher findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Voucher> list = em.createNamedQuery("Voucher.findByVoucherId", Voucher.class)
                    .setParameter("voucherId", id)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Error finding voucher by id", e);
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
            throw e; // hoặc log
        } finally {
            em.close();
        }
    }

    public List<Voucher> searchByCode(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Voucher v WHERE v.voucherCode LIKE :kw",
                    Voucher.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
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

}
