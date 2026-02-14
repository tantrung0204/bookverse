/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Category;
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class CategoryDAO {

    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Category.findAll", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Category> searchByName(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Category.searchByName", Category.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Category findByCategoryId(int categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Category.findByCategoryId", Category.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void create(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);   // INSERT
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public boolean existCategoryName(String categoryname) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("Category.existsByName", Long.class)
                    .setParameter("name", categoryname.trim())
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean existCategory(String categoryname, int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createNamedQuery("Category.existsCategory", Long.class)
                    .setParameter("name", categoryname.trim())
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void edit(Category category) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
