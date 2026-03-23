/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.AuthorDAO;
import com.mycompany.bookverse.model.Author;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author LECOO
 */
public class AuthorService {

    private AuthorDAO authorDAO = new AuthorDAO();

    public List<Author> getAllAuthors() {
        return authorDAO.findAll();
    }

    public Author findAuthorById(int id) {
        return authorDAO.findById(id);
    }

    public String insertAuthor(Author author) {
        String error = "";
        String nat = author.getNationality(),
                name = author.getAuthorName(),
                bio = author.getBiographyText();
        Integer birth = author.getBirthYear();
        if (nat == null || nat.trim().isEmpty()) {
            error += "Nationality can not be empty.<br>";
        } else if (!nat.matches("^[a-zA-ZÀ-ỹ\\s\\-_&.]+$")) {
            error += "Nationality contains invalid characters.<br>";
        }
        if (name == null || name.trim().isEmpty()) {
            error += "Name cannot be left blank.<br>";
        } else if (!name.matches("^[a-zA-ZÀ-ỹ\\s\\-_&.]+$")) {
            error += "Name contains invalid characters.<br>";
        }
        if (bio == null || bio.trim().isEmpty()) {
            error += "Biography cannot be left blank.<br>";
        } else if (!bio.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Biography contains invalid characters.<br>";
        }
        if (null == birth) {
            error += "Birth year cannot be empty.<br>";
        } else if (!(birth > 0 && birth < LocalDate.now().getYear())) {
            error += "Year of birth must be between 1 and the current year.<br>";
        }
        if (error.isEmpty()) {
            boolean checkExist = checkAuthorExistByName(name);
            if (!checkExist) {
                if (authorDAO.createAuthor(author)) {
                    return "Create Author successfully";
                } else {
                    return "Create Author false";
                }
            } else {
                return "Author name already exists";
            }
        }
        return error;
    }

    public String editAuthor(Author author) {
        String error = "";
        if (author.getNationality() == null || author.getNationality().trim().isEmpty()) {
            error += "Nationality can not be empty.<br>";
        } else if (!author.getNationality().matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Nationality contains invalid characters.<br>";
        }
        if (author.getAuthorName() == null || author.getAuthorName().trim().isEmpty()) {
            error += "Name cannot be left blank.<br>";
        } else if (!author.getAuthorName().matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Name contains invalid characters.<br>";
        }
        if (author.getBiographyText() == null || author.getBiographyText().trim().isEmpty()) {
            error += "Biography cannot be left blank.<br>";
        } else if (!author.getBiographyText().matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Biography contains invalid characters.<br>";
        }
        if (null == author.getBirthYear()) {
            error += "Birth can not be empty.<br>";
        } else if (!(author.getBirthYear() > 0 && author.getBirthYear() < LocalDate.now().getYear())) {
            error += "Year of birth must be between 1 and the current year.<br>";
        }
        boolean checkExist = checkAuthorExist(author.getAuthorId(), author.getAuthorName());
        Author old = authorDAO.findById(author.getAuthorId());
        if (checkExist) {
            return "Author name already exist.";
        }
        if (error.isEmpty()) {
            old.setAuthorName(author.getAuthorName());
            old.setNationality(author.getNationality());
            old.setBiographyText(author.getBiographyText());
            old.setBirthYear(author.getBirthYear());
            if (authorDAO.update(old)) {
                return "Update Author successfully";
            }
        }
        return error;

    }

    public String deleteAuthor(int id) {
        if (findAuthorById(id) == null) {
            return "Author does not exist";
        }
        if (authorDAO.countBooksByAuthorId(id) > 0) {
            return "Author is in use";
        }
        if (authorDAO.deleteById(id)) {
            return "Delete Author successfully";
        } else {
            return "Delete Author false";
        }
    }

    public long countBooksByAuthorId(int authorId) {
        return authorDAO.countBooksByAuthorId(authorId);
    }

    public List<Author> getAuthorsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return authorDAO.findByPage(offset, pageSize);
    }

    public int getTotalPages(int pageSize) {
        long totalItems = authorDAO.countAll();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public List<Author> searchByKeyword(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return authorDAO.findByKeywordAndPage(keyword, offset, pageSize);
    }

    public int getTotalPagesByKeyword(String keyword, int pageSize) {
        long totalItems = authorDAO.countByKeyword(keyword);
        return (int) Math.ceil((double) totalItems / pageSize);
    }
     public boolean checkAuthorExist(int id, String name) {
        List<Author> list = authorDAO.findAll();
        for (Author author : list) {
            if (author.getAuthorId() != id && author.getAuthorName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAuthorExistByName(String name) {
        List<Author> list = authorDAO.findAll();
        for (Author author : list) {
            if (author.getAuthorName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
