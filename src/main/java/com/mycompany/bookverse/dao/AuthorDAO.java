/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Author;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class AuthorDAO {

    public List<Author> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Author.findAll", Author.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Author findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Author.findByAuthorId", Author.class)
                    .setParameter("authorId", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Author> searchByName(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Author a WHERE a.authorName LIKE :kw", Author.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean createAuthor(Author author) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(author);
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

    public boolean update(Author author) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(author);
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

    public boolean checkAuthorExist(int id, String name) {
        List<Author> list = findAll();
        for (Author author : list) {
            if (author.getAuthorId() != id && author.getAuthorName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAuthorExistByName(String name) {
        List<Author> list = findAll();
        for (Author author : list) {
            if (author.getAuthorName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Long countBooksByAuthorId(int authorId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(b) FROM Author a JOIN a.bookCollection b WHERE a.authorId = :id";

            long quantity = em.createQuery(jpql, Long.class)
                    .setParameter("id", authorId)
                    .getSingleResult();
            return quantity;
        } finally {
            em.close();
        }
    }

    public boolean deleteById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Author author = em.find(Author.class, id);
            if (author == null) {
                return false;
            }
            em.getTransaction().begin();
            em.remove(author);
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

    public List<Author> findByPage(int offset, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Author> query = em.createQuery(
                    "SELECT a FROM Author a ORDER BY a.authorId DESC",
                    Author.class
            );
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(a) FROM Author a",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Author> findByKeywordAndPage(String keyword, int offset, int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Author> query = em.createQuery(
                    "SELECT a FROM Author a "
                    + "WHERE LOWER(a.authorName) LIKE LOWER(:kw) "
                    + "ORDER BY a.authorId DESC",
                    Author.class
            );

            query.setParameter("kw", "%" + keyword + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countByKeyword(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(a) FROM Author a "
                    + "WHERE LOWER(a.authorName) LIKE LOWER(:kw)",
                    Long.class
            )
                    .setParameter("kw", "%" + keyword + "%")
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
