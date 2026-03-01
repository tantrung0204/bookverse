package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.GenreDAO;
import com.mycompany.bookverse.dao.ProductDAO;
import java.util.List;
import java.util.Arrays;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.PaginationConfig;

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
    private GenreDAO genreDao = new GenreDAO();

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

    public long countProductByCategoryId(int categoryId) {
        return productDao.countProductByCategory(categoryId);
    }

    public List<Book> getTopBestSellingBooks() {
        return productDao.findTopSellingBooks(5);
    }

    public List<Stationery> getTopBestSellingStationery() {
        return productDao.findTopSellingStationery(5);
    }

    public List<Genre> getAllGenres() {
        return genreDao.findAll();
    }

    public List<? extends Product> getFilteredProducts(String type, List<Integer> genreIds, String sort, int page) {
        int pageSize = PaginationConfig.HOMEPAGE_ITEMS_PER_PAGE; // = 20

        if ("book".equalsIgnoreCase(type)) {
            return productDao.findBooksWithFilter(genreIds, sort, page, pageSize);
        } else {
            return productDao.findStationeryWithFilter(sort, page, pageSize);
        }
    }

    public long getTotalCount(String type, List<Integer> genreIds) {
        if ("book".equalsIgnoreCase(type)) {
            return productDao.countBooks(genreIds);
        } else {
            return productDao.countStationery();
        }
    }
}
