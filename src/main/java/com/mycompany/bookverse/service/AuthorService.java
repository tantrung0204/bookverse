/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.AuthorDAO;
import com.mycompany.bookverse.model.Author;
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

    public List<Author> searchAuthors(String keyword) {
        return authorDAO.searchByName(keyword);
    }

    public String insertAuthor(String name, String birth, String nat, String bio) {
        String error ="";
        if (nat == null || nat.trim().isEmpty()) {
            error += "Nationality can not be empty.\n";
        } else if (!nat.matches("^[a-zA-ZÀ-ỹ\\s\\-_&.]+$")) {
            error += "Nationality contains invalid characters.\n";
        }
        if (name == null || name.trim().isEmpty()) {
            error += "Name cannot be left blank.\n";
        } else if (!name.matches("^[a-zA-ZÀ-ỹ\\s\\-_&.]+$")) {
            error += "Name contains invalid characters.\n";
        }
        if (bio == null || bio.trim().isEmpty()) {
            error += "Biography cannot be left blank.\n";
        } else if (!bio.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Biography contains invalid characters.\n";
        }
        if (birth == null || birth.trim().isEmpty()) {
            error += "Birth can not be empty.\n";
        } else if (!birth.matches("\\d{4}")) {
            error += "The date of birth must be a four-digit number.\n";
        }

        if (error.isEmpty()) {
            boolean checkExist = authorDAO.checkAuthorExistByName(name);
            if (!checkExist) {
                Author Author = new Author();
                Author.setAuthorName(name);
                Integer birthDay = Integer.valueOf(birth);
                Author.setBirthYear(birthDay);
                Author.setNationality(nat);
                Author.setBiographyText(bio);
                if (authorDAO.createAuthor(Author)) {
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

    public String editAuthor(int id, String name, String birth, String nationality, String biography) {
        String error ="";
        if (nationality == null || nationality.trim().isEmpty()) {
            error += "Nationality can not be empty.\n";
        } else if (!nationality.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Nationality contains invalid characters.\n";
        }
        if (name == null || name.trim().isEmpty()) {
            error += "Name cannot be left blank.\n";
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Name contains invalid characters.\n";
        }
        if (biography == null || biography.trim().isEmpty()) {
            error += "Biography cannot be left blank.\n";
        } else if (!biography.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Biography contains invalid characters.\n";
        }
        if (birth == null || birth.trim().isEmpty()) {
            error += "Birth can not be empty.\n";
        } else if (!birth.matches("\\d{4}")) {
            error += "Birth day must be a number.\n";
        }
        boolean checkExist = authorDAO.checkAuthorExist(id, name);
        Author old = authorDAO.findById(id);
        if (checkExist) {
            return "Author name already exist.";
        }
        if (error.isEmpty()) {
            Integer birthDay = Integer.valueOf(birth);
            old.setAuthorName(name);
            old.setNationality(nationality);
            old.setBiographyText(biography);
            old.setBirthYear(birthDay);
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

    public Long countBooksByAuthorId(int authorId) {
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
}
