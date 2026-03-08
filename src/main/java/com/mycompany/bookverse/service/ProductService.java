package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.FeedbackDAO;
import com.mycompany.bookverse.dao.GenreDAO;
import com.mycompany.bookverse.dao.ProductDAO;
import java.util.List;
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

    // ==========================================
    // CÁC HÀM DÀNH CHO TRANG QUẢN TRỊ (ADMIN)
    // ==========================================
    public List<Book> getAdminBooks(String keyword, int page) {
        return productDao.findAdminBooks(keyword, page, PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public int getTotalAdminBookPages(String keyword) {
        long totalItems = productDao.countAdminBooks(keyword);
        return (int) Math.ceil((double) totalItems / PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public List<Stationery> getAdminStationeries(String keyword, int page) {
        return productDao.findAdminStationeries(keyword, page, PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public int getTotalAdminStationeryPages(String keyword) {
        long totalItems = productDao.countAdminStationeries(keyword);
        return (int) Math.ceil((double) totalItems / PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public long countProductByCategoryId(int categoryId) {
        return productDao.countProductByCategory(categoryId);
    }

    // ==========================================
    // CÁC HÀM DÀNH CHO TRANG HOME
    // ==========================================
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
