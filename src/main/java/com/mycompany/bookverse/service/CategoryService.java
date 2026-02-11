/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CategoryDAO;
import com.mycompany.bookverse.model.Category;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class CategoryService {

    private CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllcategorys() {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(int categoryId) {
        return categoryDAO.findByCategoryId(categoryId);
    }

}
