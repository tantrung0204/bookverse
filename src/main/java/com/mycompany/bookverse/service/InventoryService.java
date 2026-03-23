/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.InventoryDAO;
import com.mycompany.bookverse.model.ImportStock;
import com.mycompany.bookverse.model.ImportStockDetail;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.OrderItem;
import com.mycompany.bookverse.model.Product;
import com.mycompany.bookverse.model.Supplier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class InventoryService {

    private InventoryDAO inventoryDAO = new InventoryDAO();

    public int getTotalImportPages(int pageSize, Date from, Date to) {
        long totalItems = inventoryDAO.countAllImports(from, to);
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public int getTotalExportPages(int pageSize, Date from, Date to) {
        long totalItems = inventoryDAO.countAllExports(from, to);
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public List<ImportStock> getImportsByPage(int page, int pageSize, Date from, Date to) {
        int offset = (page - 1) * pageSize;
        return inventoryDAO.findByImportPage(offset, pageSize, from, to);
    }

    public List<ImportStockDetail> getImportDetail(int id) {
        return inventoryDAO.findByImportId(id);
    }

    public List<OrderItem> getExportDetail(int id) {
        return inventoryDAO.findByExportId(id);
    }

    public List<Order> getExportsByPage(int page, int pageSize, Date from, Date to) {
        int offset = (page - 1) * pageSize;
        return inventoryDAO.findByExportPage(offset, pageSize, from, to);
    }

    public List<Product> getAllProduct() {
        return inventoryDAO.findAllProduct();
    }

    public List<Supplier> getAllSupplier() {
        return inventoryDAO.findAllSupplier();
    }

    public Supplier getSupplier(int id) {
        return inventoryDAO.findSupplierById(id);
    }

    public Product getProduct(int id) {
        return inventoryDAO.findProductById(id);
    }

    public int insertImportStock(ImportStock importStock) {
        return inventoryDAO.addImportStock(importStock);
    }

    public void checkValid(String supplierIdStr, String[] productIdsString,
            ArrayList<String> quantities, ArrayList<String> unitPrices, ArrayList<String> notes) throws Exception {
        int supplierId;
        try {
            supplierId = Integer.parseInt(supplierIdStr);
        } catch (NumberFormatException e) {
            throw new Exception("Id supplier is error");
        }
        Supplier supplier = getSupplier(supplierId);
        if (supplier == null) {
            throw new Exception("supplier is not found");
        }
        if (productIdsString == null || productIdsString.length == 0) {
            throw new Exception("No product selected");
        }
        for (String UnitPrice : unitPrices) {
            try {
                int unitPrice = Integer.parseInt(UnitPrice);
            } catch (NumberFormatException e) {
                throw new Exception("unit price error");
            }
        }
        for (String quantityStr : quantities) {
            try {
                int quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                throw new Exception("quantity error");
            }
        }
        for (String note : notes) {
            if(null==note || note.isEmpty())
                throw new Exception("note can't be empty");
        }
    }

    public boolean insertImportStockDetail(ImportStockDetail importStockDetail) {
        return inventoryDAO.addImportStockDetail(importStockDetail);
    }
}
