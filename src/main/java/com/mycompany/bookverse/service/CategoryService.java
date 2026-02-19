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

    public List<Category> getAllcategories() {
        return categoryDAO.findAll();
    }

    public List<Category> getsearchByName(String keyword) {
        return categoryDAO.searchByName(keyword);
    }

    public Category getCategoryById(int categoryId) {
        return categoryDAO.findByCategoryId(categoryId);
    }

    public void createCategory(Category category) {
        categoryDAO.create(category);
    }

    public boolean existCategoryName(String categoryname) {
        return categoryDAO.existCategoryName(categoryname);
    }

    public boolean existCategory(String categoryname, int id) {
        return categoryDAO.existCategory(categoryname, id);
    }

    public void editCategory(Category category) {
        categoryDAO.edit(category);
    }

    public boolean deleteCategory(int id) {
        if (!categoryDAO.canDeleteCategory(id)) {
            return false;
        }
        return categoryDAO.deleteCategoryById(id);
    }

}
