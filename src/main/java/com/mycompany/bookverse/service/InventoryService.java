/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.InventoryDAO;
import com.mycompany.bookverse.model.ImportStock;
import com.mycompany.bookverse.model.ImportStockDetail;
import com.mycompany.bookverse.model.Order;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class InventoryService {

    private InventoryDAO inventoryDAO = new InventoryDAO();

    public int getTotalImportPages(int pageSize) {
        long totalItems = inventoryDAO.countAllImports();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public int getTotalImportDetailPages(int pageSize, int id) {
        long totalItems = inventoryDAO.countAllImportDetail(id);
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public int getTotalExportPages(int pageSize) {
        long totalItems = inventoryDAO.countAllImports();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public List<ImportStock> getImportsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return inventoryDAO.findByImportPage(offset, pageSize);
    }

    public List<ImportStockDetail> getImportDetail(int page, int pageSize, int id) {
        int offset = (page - 1) * pageSize;
        return inventoryDAO.findByImportId(id, offset, pageSize);
    }

    public List<Order> getExportsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return inventoryDAO.findByExportPage(offset, pageSize);
    }
}
