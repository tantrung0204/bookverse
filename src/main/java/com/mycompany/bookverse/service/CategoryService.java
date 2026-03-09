/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CategoryDAO;
import com.mycompany.bookverse.model.Category;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class CategoryService {

    private CategoryDAO categoryDAO = new CategoryDAO();
    public List<Category> getAllCategories;

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public List<Category> getAllCategoriesPage(int page) {
        return categoryDAO.getCategoriesPaging(page, PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public long getTotalPages() {

        long total = categoryDAO.countAllCategories();

        return (long) Math.ceil(
                (double) total
                        / PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public List<Category> searchPaging(String keyword, int page) {

        return categoryDAO.searchByNamePaging(
                keyword,
                page,
                PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public long getTotalSearchPages(String keyword) {

        long total = categoryDAO.countSearch(keyword);

        return (long) Math.ceil(
                (double) total
                        / PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public Category getCategoryById(int categoryId) {
        return categoryDAO.findByCategoryId(categoryId);
    }

    public List<Category> getActiveSubCategories() {
        return categoryDAO.findActiveSubCategories();
    }

    public List<Category> getActiveSubCategoriesByParentId(int parentId) {
        return categoryDAO.findActiveSubCategoriesByParentId(parentId);
    }

    public void createCategory(String name, String desc, String statusRaw, String parentRaw) {

        int status = 1;
        int parentId = 1;

        // Validate status
        if (statusRaw != null && !statusRaw.isEmpty()) {
            try {
                status = Integer.parseInt(statusRaw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        // Validate parent
        if (parentRaw == null || parentRaw.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select parent category");
        }

        try {
            parentId = Integer.parseInt(parentRaw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parent category");
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be empty");
        }

        name = name.trim();

        if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            throw new IllegalArgumentException("Category name contains invalid characters");
        }

        // Validate description
        if (desc == null || desc.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
        }

        desc = desc.trim();

        if (!desc.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            throw new IllegalArgumentException("Description contains invalid characters");
        }

        // Check duplicate
        if (existCategoryName(name)) {
            throw new IllegalArgumentException("Category already exists");
        }

        // Create entity
        Category category = new Category();
        category.setCategoryName(name);
        category.setDescriptionText(desc);
        category.setStatus(status);

        Category parent = categoryDAO.findByCategoryId(parentId);
        category.setParent(parent);

        categoryDAO.create(category);
    }

    public boolean existCategoryName(String categoryname) {
        return categoryDAO.existCategoryName(categoryname);
    }

    public boolean existCategory(String categoryname, int id) {
        return categoryDAO.existCategory(categoryname, id);
    }

    public void editCategory(String idRaw,
            String name,
            String desc,
            String statusRaw,
            String parentRaw) {

        // ===== Validate ID =====
        int id;
        int parentId = 1;
        try {
            id = Integer.parseInt(idRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid category ID");
        }

        // ===== Validate status =====
        int status = 1;
        if (statusRaw != null && !statusRaw.isEmpty()) {
            try {
                status = Integer.parseInt(statusRaw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        if (statusRaw != null && !statusRaw.isEmpty()) {
            try {
                status = Integer.parseInt(statusRaw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        // ===== Validate name =====
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be empty");
        }

        name = name.trim();

        if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            throw new IllegalArgumentException("Category name contains invalid characters");
        }

        // ===== Validate description =====
        if (desc == null || desc.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
        }

        desc = desc.trim();

        if (!desc.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            throw new IllegalArgumentException("Description contains invalid characters");
        }

        // ===== Check duplicate (exclude current ID) =====
        if (existCategory(name, id)) {
            throw new IllegalArgumentException("Category already exists");
        }

        // ===== Update entity =====
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        category.setDescriptionText(desc);
        category.setStatus(status);
        Category parent = categoryDAO.findByCategoryId(parentId);
        category.setParent(parent);

        categoryDAO.edit(category);
    }

    public boolean deleteCategory(String idParam) {

        if (idParam == null) {
            return false;
        }

        int id = Integer.parseInt(idParam);

        return categoryDAO.deleteCategoryById(id);
    }

}
