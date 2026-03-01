/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Genre;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class GenreDAO {

    public List<Genre> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Genre.findAll", Genre.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Genre findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Genre.findByGenreId", Genre.class)
                    .setParameter("genreId", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Genre> searchByName(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT g FROM Genre g WHERE g.genreName LIKE :kw", Genre.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean createGenre(Genre genre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(genre);// tạo genre mới
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {// nếu Transaction đang hoạt động mà bị lỗi thì trả về.
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public boolean update(Genre genre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(genre);
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

    public boolean checkGenreExist(String name) {
        List<Genre> list = findAll();// Lấy danh sách genre trong database
        for (Genre genre : list) {// Kiểm tra xem genre đã tồn tại trong db chưa.
            if (genre.getGenreName().equalsIgnoreCase(name)) {// nếu đã tồn tại thì trả về true
                return true;
            }
        }
        return false;
    }
    public boolean checkGenreInUse(int id){
        EntityManager em = JPAUtil.getEntityManager();
    try {
        Long count = em.createQuery(
                "SELECT COUNT(b) FROM Book b WHERE b.genreId.id = :id",
                Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return count > 0;

    } finally {
        em.close();
    }
    }
    public boolean deleteById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Genre genre = em.find(Genre.class, id);
            if (genre == null) {
                return false;
            }
            em.getTransaction().begin();
            em.remove(genre);
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
