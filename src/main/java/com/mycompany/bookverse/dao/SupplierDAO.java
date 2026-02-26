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

    public List<Supplier> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Supplier.findAll", Supplier.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Supplier> searchByName(String keyword){
         EntityManager em = JPAUtil.getEntityManager();
         try {
            return em.createNamedQuery("Supplier.searchByName", Supplier.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
