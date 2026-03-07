package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.FeedbackDAO;
import com.mycompany.bookverse.dao.GenreDAO;
import com.mycompany.bookverse.dao.ProductDAO;
import java.util.List;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.JPAUtil;
import com.mycompany.bookverse.utils.PaginationConfig;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;

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

    public void createProductDetail(Product newProduct) throws Exception {

        // 1. Validate trùng tên
        if (productDao.isProductNameExists(newProduct.getName(), 0)) {
            throw new Exception("Product name already exists in the system.");
        }

        // 2. Gọi DAO thao tác với Database
        boolean isSuccess = productDao.createProduct(newProduct);
        if (!isSuccess) {
            throw new Exception("Error creating product in database.");
        }
    }

    public void updateProductDetail(Product updatedProduct) throws Exception {

        // 1. Validate trùng tên
        if (productDao.isProductNameExists(updatedProduct.getName(), updatedProduct.getProductId())) {
            throw new Exception("Product name already exists in the system.");
        }

        // 2. Lấy sản phẩm hiện tại từ DB lên
        Product existingProduct = productDao.findProductById(updatedProduct.getProductId());
        if (existingProduct == null) {
            throw new Exception("Product not found.");
        }

        // 3. Copy dữ liệu mới đè lên dữ liệu cũ
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setDescriptionText(updatedProduct.getDescriptionText());

        // Nếu có ảnh mới thì mới update
        if (updatedProduct.getImageUrl() != null && !updatedProduct.getImageUrl().isEmpty()) {
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
        }

        existingProduct.setCategoryId(updatedProduct.getCategoryId());

        // 4. Copy các trường riêng theo Type
        if (existingProduct instanceof Book && updatedProduct instanceof Book) {
            Book existingBook = (Book) existingProduct;
            Book updatedBook = (Book) updatedProduct;

            existingBook.setGenreId(updatedBook.getGenreId());
            existingBook.setAuthorCollection(updatedBook.getAuthorCollection());

        } else if (existingProduct instanceof Stationery && updatedProduct instanceof Stationery) {
            Stationery existingStat = (Stationery) existingProduct;
            Stationery updatedStat = (Stationery) updatedProduct;

            existingStat.setColor(updatedStat.getColor());
            existingStat.setMaterial(updatedStat.getMaterial());
        }

        // 5. Gọi tầng DAO để thực thi việc lưu xuống DB
        boolean isSuccess = productDao.updateProduct(existingProduct);
        if (!isSuccess) {
            throw new Exception("Error updating product in database.");
        }
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

    public List<Genre> getAllActiveGenre() {
        return genreDao.findActiveGenres();
    }

    public List<? extends Product> getFilteredProducts(String type, List<Integer> genreIds, String sort, String keyword, Integer categoryId, int page) {
        int pageSize = PaginationConfig.HOMEPAGE_ITEMS_PER_PAGE;

        if ("book".equalsIgnoreCase(type)) {
            return productDao.findBooksWithFilter(genreIds, sort, keyword, page, pageSize);
        } else if ("stationery".equalsIgnoreCase(type)) {
            return productDao.findStationeryWithFilter(sort, keyword, page, pageSize);
        } else {
            return productDao.searchAllProducts(sort, keyword, categoryId, page, pageSize);
        }
    }

    public long getTotalCount(String type, List<Integer> genreIds, String keyword, Integer categoryId) {
        if ("book".equalsIgnoreCase(type)) {
            return productDao.countBooks(genreIds, keyword);
        } else if ("stationery".equalsIgnoreCase(type)) {
            return productDao.countStationery(keyword);
        } else {
            return productDao.countAllProducts(keyword, categoryId);
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

    public boolean isProductNameExists(String name, int id) {
        return productDao.isProductNameExists(name, id);
    }
}
