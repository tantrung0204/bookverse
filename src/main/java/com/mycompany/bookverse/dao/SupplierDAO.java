/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Supplier;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class SupplierDAO {

    public List<Supplier> getSuppliersPaging(int page, int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT s FROM Supplier s ORDER BY s.supplierId DESC",
                    Supplier.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countAllsupplier() {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(s) FROM Supplier s",
                    Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Supplier> searchByNamePaging(String keyword,
            int page, int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT s FROM Supplier s "
                    + "WHERE LOWER(c.name) LIKE LOWER(:kw) "
                    + "ORDER BY s.supplierId DESC",
                    Supplier.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countSearch(String keyword) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(s) FROM Supplier s "
                    + "WHERE LOWER(s.name) LIKE LOWER(:kw)",
                    Long.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public void create(Supplier supplier) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(supplier);   // INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public boolean existSupplierName(String suppliername) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("Supplier.existsByName", Long.class)
                    .setParameter("name", suppliername.trim())
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void edit(Supplier supplier) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(supplier);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public boolean existSupplier(String supplierName, int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("Supplier.existsSupplier", Long.class)
                    .setParameter("name", supplierName.trim())
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
    
    public boolean deleteSupplierById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Supplier supplier = em.find(Supplier.class, id);
            if (supplier == null) {
                return false;
            }

            em.remove(supplier);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

}
