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

    public int getTotalExportPages(int pageSize,Date from, Date to) {
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

    public List<Order> getExportsByPage(int page, int pageSize,Date from, Date to) {
        int offset = (page - 1) * pageSize;
        return inventoryDAO.findByExportPage(offset, pageSize,from,to);
    }
}
