/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Category;
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


    public List<Category> getCategoriesPaging(int page, int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category c WHERE c.parent IS NOT NULL ORDER BY c.categoryId DESC",
                    Category.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countAllCategories() {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(c) FROM Category c  WHERE c.parent IS NOT NULL",
                    Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Category> searchByNamePaging(String keyword,
            int page, int pageSize) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category c "
                    + "WHERE LOWER(c.categoryName) LIKE LOWER(:kw) "
                    + "ORDER BY c.categoryId DESC",
                    Category.class)
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
                    "SELECT COUNT(c) FROM Category c "
                    + "WHERE LOWER(c.categoryName) LIKE LOWER(:kw)",
                    Long.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getSingleResult();
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

//    public boolean existCategoryById(int id) {
//        EntityManager em = JPAUtil.getEntityManager();
//        try {
//            Long count = em.createQuery(
//                    "SELECT COUNT(c) FROM Category c WHERE c.categoryId = :id",
//                    Long.class)
//                    .setParameter("id", id)
//                    .getSingleResult();
//            return count > 0;
//        } finally {
//            em.close();
//        }
//    }

    public boolean canDeleteCategory(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long bookCount = em.createQuery(
                    "SELECT COUNT(b) FROM Book b WHERE b.category.categoryId = :id",
                    Long.class
            ).setParameter("id", id)
                    .getSingleResult();

            Long stationeryCount = em.createQuery(
                    "SELECT COUNT(s) FROM Stationery s WHERE s.category.categoryId = :id",
                    Long.class
            ).setParameter("id", id)
                    .getSingleResult();

            return bookCount == 0 && stationeryCount == 0;
        } finally {
            em.close();
        }
    }

    public boolean deleteCategoryById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Category category = em.find(Category.class, id);
            if (category == null) {
                return false;
            }

            em.remove(category);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Category> findActiveSubCategories() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT c FROM Category c WHERE c.parent IS NOT NULL AND c.status = 1";
            return em.createQuery(sql, Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Category> findActiveSubCategoriesByParentId(int parentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String sql = "SELECT c FROM Category c WHERE c.parent.categoryId = :parentId AND c.status = 1";

            return em.createQuery(sql, Category.class)
                    .setParameter("parentId", parentId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
