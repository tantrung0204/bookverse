/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.SupplierDAO;
import com.mycompany.bookverse.model.Supplier;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class SupplierService {

    private SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.findAll();
    }

    public List<Supplier> getSearchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return supplierDAO.findAll();
        }
        return supplierDAO.searchByName(keyword.trim());

    }

}
