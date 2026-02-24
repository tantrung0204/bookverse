/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.BookDAO;
import com.mycompany.bookverse.model.Book;
import java.util.List;

/**
 *
 * @author NganTTK-CE190411
 */
public class BookService {
    public BookDAO bookDAO = new BookDAO();
    
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }
}
