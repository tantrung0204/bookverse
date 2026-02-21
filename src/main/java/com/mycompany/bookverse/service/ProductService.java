package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.ProductDAO;
import java.util.List;
import com.mycompany.bookverse.model.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author TrungNT - CE200064
 */
public class ProductService {

    private ProductDAO productDao = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public Product getProductById(String idStr) {
        try {
            Integer id = Integer.parseInt(idStr);
            return productDao.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productDao.findAll();
        }
        return productDao.findByName(keyword.trim());
    }
}
