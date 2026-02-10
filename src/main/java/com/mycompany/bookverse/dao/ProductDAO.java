/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import java.util.List;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;

/**
 *
 * @author TrungNT - CE200064
 */
public class ProductDAO {

    public List<Product> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Product.findAll", Product.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Product findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Product p = em.find(Product.class, id);
            if (p != null) {
                if (p instanceof Book) {
                    Book b = (Book) p;
                    Hibernate.initialize(b.getAuthorCollection());
                }
            }
            return p;
        } finally {
            em.close();
        }
    }

}
