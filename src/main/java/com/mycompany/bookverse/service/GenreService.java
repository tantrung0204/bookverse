/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.GenreDAO;
import com.mycompany.bookverse.model.Genre;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class GenreService {
    private GenreDAO genreDAO= new GenreDAO();
     public List <Genre> getAllgenres(){
         return genreDAO.findAll();
     }
}
