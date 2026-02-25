/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Stationery;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class StationeryDAO {

    public List<Stationery> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Stationery.findAll", Stationery.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public void insert(Stationery stationery) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(stationery);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
