/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.StationeryDAO;
import com.mycompany.bookverse.model.Stationery;

/**
 *
 * @author NganTTK-CE190411
 */
public class StationeryService {
    
    private StationeryDAO stationeryDAO = new StationeryDAO();

    public void createStationery(int productId,
            String color, String material) {

        Stationery st = new Stationery();
        st.setProductId(productId);
        st.setColor(color);
        st.setMaterial(material);

        stationeryDAO.insert(st);
    }
}

