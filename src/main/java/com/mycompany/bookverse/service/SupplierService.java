/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.SupplierDAO;
import com.mycompany.bookverse.model.Supplier;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class SupplierService {

    private SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers(int page) {
        return supplierDAO.getSuppliersPaging(page, PaginationConfig.ADMIN_ITEMS_PER_PAGE);
    }

    public long getTotalPages() {

        long total = supplierDAO.countAllsupplier();

        return (long) Math.ceil(
                (double) total
                / PaginationConfig.ADMIN_ITEMS_PER_PAGE
        );
    }

    public List<Supplier> searchPaging(String keyword, int page) {

        return supplierDAO.searchByNamePaging(
                keyword,
                page,
                PaginationConfig.ADMIN_ITEMS_PER_PAGE
        );
    }

    public long getTotalSearchPages(String keyword) {

        long total = supplierDAO.countSearch(keyword);

        return (long) Math.ceil(
                (double) total
                / PaginationConfig.ADMIN_ITEMS_PER_PAGE
        );
    }

    public void createSupplier(String name, String email, String phone, String address, String statusRaw) {
        int status = 1;

        // Validate status
        if (statusRaw != null && !statusRaw.isEmpty()) {
            try {
                status = Integer.parseInt(statusRaw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name must not be empty");
        }

        name = name.trim();

        if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            throw new IllegalArgumentException("Supplier name contains invalid characters");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        email = email.trim();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone must not be empty");
        }

        phone = phone.trim();

        if (!phone.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Phone must contain 10 digits");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty");
        }

        if (existSupplierName(name)) {
            throw new IllegalArgumentException("Supplier already exists");
        }

        address = address.trim();
        Supplier supplier = new Supplier();
        supplier.setSupplierName(name);
        supplier.setSupplierEmail(email);
        supplier.setSupplierPhone(phone);
        supplier.setSupplierAddress(address);
        supplier.setStatus(status);

        supplierDAO.create(supplier);

    }

    public boolean existSupplierName(String suppliername) {
        return supplierDAO.existSupplierName(suppliername);
    }

    public void editSupplier(String idRaw, String name, String email, String phone, String address, String statusRaw) {
        
        int id;
        try {
            id = Integer.parseInt(idRaw);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        
        int status = 1;
        // Validate status
        if (statusRaw != null && !statusRaw.isEmpty()) {
            try {
                status = Integer.parseInt(statusRaw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid status value");
            }
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name must not be empty");
        }

        name = name.trim();

        if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            throw new IllegalArgumentException("Supplier name contains invalid characters");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }

        email = email.trim();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone must not be empty");
        }

        phone = phone.trim();

        if (!phone.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Phone must contain 10 digits");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty");
        }

        if (existSupplier(name, id)) {
            throw new IllegalArgumentException("Supplier already exists");
        }
        
        Supplier supplier = new Supplier();
        supplier.setSupplierId(id);
        supplier.setSupplierName(name);
        supplier.setSupplierEmail(email);
        supplier.setSupplierPhone(phone);
        supplier.setSupplierAddress(address);
        supplier.setStatus(status);
        
        supplierDAO.edit(supplier);
    }

    private boolean existSupplier(String supplierName, int id) {
      return supplierDAO.existSupplier(supplierName, id);
    }
    
    public boolean deleteSupplier(String idParam) {

        if (idParam == null) {
            return false;
        }

        int id = Integer.parseInt(idParam);

        return supplierDAO.deleteSupplierById(id);
    }

}
