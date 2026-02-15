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
        try (
                EntityManager em = JPAUtil.getEntityManager()) {

            return em.createNamedQuery("Genre.findAll", Genre.class)
                    .getResultList();
        }
    }

    public Genre findById(int id) {
        try (
                EntityManager em = JPAUtil.getEntityManager()) {
            return em.createNamedQuery("Genre.findByGenreId", Genre.class)
                    .setParameter("genreId", id)
                    .getSingleResult();

        }
    }

    public Genre findByName(String name) {
        try (
                EntityManager em = JPAUtil.getEntityManager()) {
            return em.createNamedQuery("Genre.findByGenreName", Genre.class)
                    .setParameter("genreName", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
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
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

}
