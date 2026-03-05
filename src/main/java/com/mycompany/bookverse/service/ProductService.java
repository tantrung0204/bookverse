package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.FeedbackDAO;
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
    private FeedbackDAO feedbackDao = new FeedbackDAO();

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

    public List<? extends Product> getFilteredProducts(String type, List<Integer> genreIds, String sort, String keyword, int page) {
        int pageSize = PaginationConfig.HOMEPAGE_ITEMS_PER_PAGE;

        if ("book".equalsIgnoreCase(type)) {
            return productDao.findBooksWithFilter(genreIds, sort, keyword, page, pageSize);
        } else if ("stationery".equalsIgnoreCase(type)) {
            return productDao.findStationeryWithFilter(sort, keyword, page, pageSize);
        } else {
            return productDao.searchAllProducts(sort, keyword, page, pageSize);
        }
    }

    public long getTotalCount(String type, List<Integer> genreIds, String keyword) {
        if ("book".equalsIgnoreCase(type)) {
            return productDao.countBooks(genreIds, keyword);
        } else if ("stationery".equalsIgnoreCase(type)) {
            return productDao.countStationery(keyword);
        } else {
            return productDao.countAllProducts(keyword);
        }
    }

    public Product getProductDetail(int productId) {
        return productDao.findProductById(productId);
    }

    public List<Product> getRelatedProducts(Product product) {
        return productDao.findRelatedProducts(product, 4);
    }

    public List<Feedback> getProductFeedbacks(int productId, int page) {
        int pageSize = PaginationConfig.FEEDBACK_ITEMS_PER_PAGE;
        return feedbackDao.getPaginatedFeedbacksByProduct(productId, page, pageSize);
    }
}
