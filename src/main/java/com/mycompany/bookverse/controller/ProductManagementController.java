/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.service.*;
import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
@WebServlet(name = "ProductManagementController", urlPatterns = {"/product"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class ProductManagementController extends HttpServlet {
    
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
    private GenreService genreService = new GenreService();
    private AuthorService authorService = new AuthorService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = (String) request.getAttribute("action");
        if (action == null) {
            action = request.getParameter("action");
        }
        if (action == null || action.isEmpty()) {
            action = "list";
        }
        
        switch (action) {
            case "detail":
                showProductDetail(request, response);
                break;
            case "edit":
                showProductEditForm(request, response);
                break;
            case "list":
            default:
                showProductList(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "create":
                createProductLogic(request, response);
                break;
            case "update":
                updateProductLogic(request, response);
                break;
            case "delete":
                deleteProductLogic(request, response);
                break;
            default:
                doGet(request, response);
        }
    }
    
    private void showProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy các tham số
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "book"; // Mặc định là Book
        }
        
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // 2. Xử lý logic theo tab
        if ("stationery".equalsIgnoreCase(tab)) {
            request.setAttribute("productList", productService.getAdminStationeries(keyword, page));
            request.setAttribute("totalPages", productService.getTotalAdminStationeryPages(keyword));
        } else {
            request.setAttribute("productList", productService.getAdminBooks(keyword, page));
            request.setAttribute("totalPages", productService.getTotalAdminBookPages(keyword));
        }

        // 3. Dữ liệu trạng thái về JSP
        request.setAttribute("contentPage", "product-list.jsp");
        request.setAttribute("activeMenu", "product");
        request.setAttribute("currentTab", tab);
        request.setAttribute("currentKeyword", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("maxNode", PaginationConfig.MAX_PAGE_NODES);

        // 4. Dữ liệu phụ trợ cho Modal Create/Edit
        request.setAttribute("bookCategories", categoryService.getActiveSubCategoriesByParentId(1));
        request.setAttribute("stationeryCategories", categoryService.getActiveSubCategoriesByParentId(2));
        request.setAttribute("genres", genreService.getActiveGenres());
        request.setAttribute("authors", authorService.getAllAuthors());
        
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }
    
    private void showProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product productDetail = productService.getProductDetail(productId);
            
            if (productDetail != null) {
                request.setAttribute("productDetail", productDetail);
            }
        } catch (NumberFormatException e) {
            // Bỏ qua lỗi parse ID, hiển thị danh sách bình thường
        }
        
        showProductList(request, response);
    }
    
    private void showProductEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product productEdit = productService.getProductDetail(productId);
            
            if (productEdit != null) {
                request.setAttribute("productEdit", productEdit);

                // Lấy dữ liệu cho các thẻ <select>
                int parentId = 1;
                if ("Stationery".equals(productEdit.getType())) {
                    parentId = 2;
                }
                // Gọi hàm lấy Category đã lọc
                request.setAttribute("categories", categoryService.getActiveSubCategoriesByParentId(parentId));
                
                if ("Book".equals(productEdit.getType())) {
                    request.setAttribute("genres", genreService.getActiveGenres());
                    request.setAttribute("authors", authorService.getAllAuthors());
                }
            }
        } catch (NumberFormatException e) {
        }
        showProductList(request, response);
    }
    
    private void createProductLogic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String tab = request.getParameter("tab");
        if (tab == null) {
            tab = "book";
        }
        
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String priceStr = request.getParameter("price") != null ? request.getParameter("price").trim() : "";
        String statusStr = request.getParameter("status");
        String description = request.getParameter("description") != null ? request.getParameter("description").trim() : "";
        String type = request.getParameter("productType"); // "Book" hoặc "Stationery"

        // Lấy Category tùy theo type
        String categoryIdStr = "Book".equals(type) ? request.getParameter("bookCategoryId") : request.getParameter("stationeryCategoryId");
        
        String genreId = request.getParameter("genreId");
        String[] authorIds = request.getParameterValues("authorIds");
        String color = request.getParameter("color");
        String material = request.getParameter("material");
        String isbn = request.getParameter("isbn");
        String publisher = request.getParameter("publisher");

        // 1. Tạo đối tượng tạm để giữ dữ liệu nếu có lỗi
        Product tempProduct;
        if ("Stationery".equals(type)) {
            Stationery s = new Stationery();
            s.setColor(color);
            s.setMaterial(material);
            tempProduct = s;
        } else {
            Book b = new Book();
            b.setIsbn(isbn);
            b.setPublisher(publisher);
            try {
                if (genreId != null && !genreId.isEmpty()) {
                    Genre g = new Genre();
                    g.setGenreId(Integer.parseInt(genreId));
                    b.setGenreId(g);
                }
            } catch (NumberFormatException e) {
            }
            List<Author> tempAuthors = new ArrayList<>();
            if (authorIds != null) {
                for (String aId : authorIds) {
                    try {
                        Author a = new Author();
                        a.setAuthorId(Integer.parseInt(aId));
                        tempAuthors.add(a);
                    } catch (NumberFormatException e) {
                    }
                }
            }
            b.setAuthorCollection(tempAuthors);
            String translator = request.getParameter("translator");
            String publishedYearStr = request.getParameter("publishedYear");
            b.setTranslator(translator);
            if (publishedYearStr != null && !publishedYearStr.trim().isEmpty()) {
                try {
                    int py = Integer.parseInt(publishedYearStr.trim());
                    int currentYear = java.time.Year.now().getValue();
                    if (py < 0 || py > currentYear) {
                        returnToCreateFormWithError(request, response, b, type, "Published Year must be between 0 and current year.");
                        return;
                    }
                    b.setPublishedYear(py);
                } catch (NumberFormatException e) {
                    returnToCreateFormWithError(request, response, b, type, "Published Year must be a valid number.");
                    return;
                }
            }

            if (isbn == null || isbn.trim().isEmpty()) {
                returnToCreateFormWithError(request, response, b, type, "ISBN is required for books.");
                return;
            }
            tempProduct = b;
        }
        
        tempProduct.setName(name);
        tempProduct.setDescriptionText(description);
        tempProduct.setStatus(statusStr != null ? Integer.parseInt(statusStr) : 1);
        try {
            tempProduct.setPrice(new BigDecimal(priceStr));
        } catch (Exception e) {
        }
        try {
            tempProduct.setStockQuantity(0);
        } catch (Exception e) {
        }
        try {
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                Category c = new Category();
                c.setCategoryId(Integer.parseInt(categoryIdStr));
                tempProduct.setCategoryId(c);
            }
        } catch (NumberFormatException e) {
        }
        
        try {
            // 2. Validate Dữ Liệu
            if (name.isEmpty()) {
                throw new Exception("Product name is required.");
            }
            if (categoryIdStr == null || categoryIdStr.isEmpty()) {
                throw new Exception("Please select a category.");
            }
            if (priceStr.isEmpty()) {
                throw new Exception("Price is required.");
            }
            
            BigDecimal price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Invalid price.");
            }
            
            if ("Book".equals(type)) {
                if (genreId == null || genreId.isEmpty()) {
                    throw new Exception("Please select a genre.");
                }
                if (authorIds == null || authorIds.length == 0) {
                    throw new Exception("Please select at least one author.");
                }
            }

            // 3. Xử lý Image Upload
            String finalImageUrl = "";
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "images" + File.separator + "products";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + uniqueFileName);
                finalImageUrl = request.getContextPath() + "/assets/images/products/" + uniqueFileName;
            }
            tempProduct.setImageUrl(finalImageUrl);

            // 4. Lưu vào Database
            productService.createProductDetail(tempProduct);

            // 5. Thành công
            request.getSession().setAttribute("successMsg", "Product added successfully.");
            response.sendRedirect(request.getContextPath() + "/product?tab=" + type.toLowerCase());
            
        } catch (Exception e) {
            returnToCreateFormWithError(request, response, tempProduct, type, e.getMessage());
        }
    }
    
    private void updateProductLogic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String tab = request.getParameter("tab");
        if (tab == null) {
            tab = "book";
        }

        // 1. Lấy thông số thô từ request
        String idStr = request.getParameter("id");
        int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;
        
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String priceStr = request.getParameter("price") != null ? request.getParameter("price").trim() : "";
        String categoryIdStr = request.getParameter("categoryId");
        int status = Integer.parseInt(request.getParameter("status"));
        String description = request.getParameter("description") != null ? request.getParameter("description").trim() : "";
        String type = request.getParameter("productType");
        
        String genreId = request.getParameter("genreId");
        String[] authorIds = request.getParameterValues("authorIds");
        String color = request.getParameter("color") != null ? request.getParameter("color").trim() : "";
        String material = request.getParameter("material") != null ? request.getParameter("material").trim() : "";
        
        String oldImageUrl = request.getParameter("oldImageUrl");
        String finalImageUrl = oldImageUrl;
        try {
            Part filePart = request.getPart("imageFile"); // Lấy file từ input
            if (filePart != null && filePart.getSize() > 0) {
                // Lấy tên file gốc
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                // Tạo tên file mới để không bị trùng
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                // Nơi lưu trữ ảnh (Lưu vào thư mục /assets/images/products)
                String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "images" + File.separator + "products";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Tạo thư mục nếu chưa có
                }

                // Lưu file
                String filePath = uploadPath + File.separator + uniqueFileName;
                filePart.write(filePath);

                // Tạo URL mới để lưu vào Database
                finalImageUrl = request.getContextPath() + "/assets/images/products/" + uniqueFileName;
            }
        } catch (Exception e) {
            System.out.println("Image upload error: " + e.getMessage());
        }

        // 2. Tạo đối tượng để lưu dữ liệu vừa nhập
        Product tempProduct;
        if ("Stationery".equals(type)) {
            Stationery s = new Stationery();
            s.setProductId(id);
            s.setColor(color);
            s.setMaterial(material);
            tempProduct = s;
        } else {
            Book b = new Book();
            b.setProductId(id);
            
            if (genreId != null && !genreId.trim().isEmpty()) {
                Genre g = new Genre();
                g.setGenreId(Integer.parseInt(genreId));
                b.setGenreId(g);
            }
            
            List<Author> tempAuthors = new ArrayList<>();
            if (authorIds != null) {
                for (String aId : authorIds) {
                    try {
                        Author a = new Author();
                        a.setAuthorId(Integer.parseInt(aId));
                        tempAuthors.add(a);
                    } catch (NumberFormatException e) {
                    }
                }
            }
            b.setAuthorCollection(tempAuthors);
            
            String isbn = request.getParameter("isbn");
            String publisher = request.getParameter("publisher");
            String translator = request.getParameter("translator");
            String publishedYearStr = request.getParameter("publishedYear");

            b.setIsbn(isbn);
            b.setPublisher(publisher);
            b.setTranslator(translator);

            if (publishedYearStr != null && !publishedYearStr.trim().isEmpty()) {
                try {
                    int py = Integer.parseInt(publishedYearStr.trim());
                    int currentYear = java.time.Year.now().getValue();
                    if (py < 0 || py > currentYear) {
                        returnToEditFormWithError(request, response, b, type, "Published Year must be between 0 and current year.");
                        return;
                    }
                    b.setPublishedYear(py);
                } catch (NumberFormatException e) {
                    returnToEditFormWithError(request, response, b, type, "Published Year must be a valid number.");
                    return;
                }
            }

            if (isbn == null || isbn.trim().isEmpty()) {
                returnToEditFormWithError(request, response, b, type, "ISBN is required for books.");
                return;
            }

            tempProduct = b;
        }
        
        tempProduct.setName(name);
        tempProduct.setDescriptionText(description);
        tempProduct.setStatus(status);
        tempProduct.setImageUrl(finalImageUrl); // finalImageUrl được tạo ra từ bước upload file

        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            Category c = new Category();
            c.setCategoryId(Integer.parseInt(categoryIdStr));
            tempProduct.setCategoryId(c);
        }
        
        try {
            tempProduct.setPrice(new BigDecimal(priceStr));
        } catch (Exception e) {
        }

        // 3. Vaidate
        try {
            // Validate Tên sản phẩm
            if (name.isEmpty()) {
                returnToEditFormWithError(request, response, tempProduct, type, "Product name is required and cannot be empty.");
                return;
            }

            // Validate Category
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                returnToEditFormWithError(request, response, tempProduct, type, "Please select a category.");
                return;
            }
            int categoryId = Integer.parseInt(categoryIdStr);

            // Validate Giá tiền
            if (priceStr.isEmpty()) {
                returnToEditFormWithError(request, response, tempProduct, type, "Price is required.");
                return;
            }
            
            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                returnToEditFormWithError(request, response, tempProduct, type, "Invalid price. It must be a positive number.");
                return;
            }

            // Validate cho Book
            if ("Book".equals(type)) {
                if (genreId == null || genreId.trim().isEmpty()) {
                    returnToEditFormWithError(request, response, tempProduct, type, "Please select a genre for this book.");
                    return;
                }
                if (authorIds == null || authorIds.length == 0) {
                    returnToEditFormWithError(request, response, tempProduct, type, "Please select at least one author.");
                    return;
                }
            }

            // 4. Gọi Service thực thi Update DB
            productService.updateProductDetail(tempProduct);

            // 5. Thành công
            request.getSession().setAttribute("successMsg", "Product updated successfully.");
            response.sendRedirect(request.getContextPath() + "/product?tab=" + tab);
            
        } catch (Exception e) {
            // Bắt lỗi trùng tên từ Service ném ra
            returnToEditFormWithError(request, response, tempProduct, type, e.getMessage());
        }
    }
    
    private void returnToCreateFormWithError(HttpServletRequest request, HttpServletResponse response,
            Product tempProduct, String type, String errorMsg)
            throws ServletException, IOException {
        request.setAttribute("createError", errorMsg);
        request.setAttribute("productCreate", tempProduct);
        request.setAttribute("openCreateModal", true);
        showProductList(request, response);
    }
    
    private void returnToEditFormWithError(HttpServletRequest request, HttpServletResponse response,
            Product tempProduct, String type, String errorMsg)
            throws ServletException, IOException {

        // 1. Đẩy câu báo lỗi và dữ liệu người dùng vừa nhập lại ra View
        request.setAttribute("editError", errorMsg);
        request.setAttribute("productEdit", tempProduct);

        // 2. Load lại các danh sách cho thẻ <select>
        int targetParentId = "Stationery".equals(type) ? 2 : 1;
        request.setAttribute("categories", categoryService.getActiveSubCategoriesByParentId(targetParentId));
        
        if ("Book".equals(type)) {
            request.setAttribute("genres", genreService.getActiveGenres());
            request.setAttribute("authors", authorService.getAllAuthors());
        }

        // 3. Render lại trang danh sách
        showProductList(request, response);
    }
    
    private void deleteProductLogic(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tab = request.getParameter("tab");
        if (tab == null) {
            tab = "book";
        }
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            productService.deleteProduct(id);
            
            request.getSession().setAttribute("successMsg", "Product deleted successfully.");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", "Cannot delete the product.");
        }
        response.sendRedirect(request.getContextPath() + "/product?tab=" + tab);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
