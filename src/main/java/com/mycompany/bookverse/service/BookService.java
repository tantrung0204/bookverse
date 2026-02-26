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
    private BookDAO bookDAO = new BookDAO();
    
     public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    public void createBook(int productId, String isbn,
            String publisher, int publishedYear) {

        Book book = new Book();
        book.setProductId(productId);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setPublishedYear(publishedYear);

        bookDAO.insert(book);
    }
    
    public Book findByProductId(int productId) {
    return bookDAO.findByProductId(productId);
}


}
