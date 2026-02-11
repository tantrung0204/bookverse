/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Genre;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
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
}
