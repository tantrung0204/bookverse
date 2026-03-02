/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.dao;

import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class CustomerDAO {

    public List<Customer> findAll() {
        // Khởi tạo entity manager
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Câu lệnh JPQL (Lấy đối tượng Customer)
//            String jpql = "SELECT c FROM Customer c";
//            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
//            
//            return query.getResultList();
            // Chỉ cần gọi tên định danh đã khai báo trong Model
            return em.createNamedQuery("Customer.findAll", Customer.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
