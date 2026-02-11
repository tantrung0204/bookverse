/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.Category;
import com.mycompany.bookverse.model.Customer;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class CategoryDAO {
    
    public List<Category> findAll(){
         EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Category.findAll", Category.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
