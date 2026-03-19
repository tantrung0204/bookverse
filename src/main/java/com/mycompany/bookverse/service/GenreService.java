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

    private GenreDAO genreDAO = new GenreDAO();

    public Genre findGenreById(int id) {
        return genreDAO.findById(id);
    }

    public List<Genre> getActiveGenres() {
        return genreDAO.findActiveGenres();
    }

    public String insertGenre(Genre genre) {
        String error = "";
        String des = genre.getDescriptionText(),
                name = genre.getGenreName();
        int status = genre.getStatus();
        if (des == null || des.trim().isEmpty()) {
            error += "Description can not be empty.<br>";
        } else if (!des.matches("^[a-zA-ZÀ-ỹ0-9\\s]+$")) {
            error += "Description contains invalid characters.<br>";
        }
        if (name == null || name.trim().isEmpty()) {
            error += "Name cannot be left blank.<br>";
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s]+$")) {
            error += "Name contains invalid characters.<br>";
        }
        if (status != 0 && status != 1) {
            error += "status must be 1 or 2.<br>";
        }
        boolean checkExist = genreDAO.checkGenreExistByName(name);

        if (checkExist) {
            return "Genre name already exists";
        }
        if (error.isEmpty()) {
            if (genreDAO.createGenre(genre)) {
                return "Create genre successfully";
            } else {
                return "Create genre false";
            }
        }
        return error;
    }

    public String editGenre(Genre genre) {
        String error = "";
        boolean checkExist = genreDAO.checkGenreExist(genre.getGenreId(), genre.getGenreName());
        Genre old = genreDAO.findById(genre.getGenreId());
        if (checkExist) {
            return "Genre name already exist.";
        }
        if (genre.getDescriptionText() == null || genre.getDescriptionText().trim().isEmpty()) {
            error += "Description can not be empty.<br>";
        } else if (!genre.getDescriptionText().matches("^[a-zA-ZÀ-ỹ]+[.]?((\\s[a-zA-ZÀ-ỹ0-9]+)+\\s?[.,-]?)*$")) {
            error += "Description contains invalid characters.<br>";
        }
        if (genre.getGenreName() == null || genre.getGenreName().trim().isEmpty()) {
            error += "Name cannot be left blank.<br>";
        } else if (!genre.getGenreName().matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Name contains invalid characters.<br>";
        }
        if (genre.getStatus() != 0 && genre.getStatus() != 1) {
            error += "status must be 1 or 2.<br>";
        }

        if (error.isEmpty()) {

            old.setGenreName(genre.getGenreName());
            old.setDescriptionText(genre.getDescriptionText());
            old.setStatus(genre.getStatus());

            if (genreDAO.update(old)) {
                return "Update genre successfully";
            }
        }
        return error;

    }

    public String deleteGenre(int id) {
        if (findGenreById(id) == null) {
            return "Genre does not exist";
        }
        if (genreDAO.checkGenreInUse(id) > 0) {
            return "Genre is in use";
        }
        if (genreDAO.deleteById(id)) {
            return "Delete genre successfully";
        } else {
            return "Delete genre false";
        }
    }

    public long countProductByGenreId(int id) {
        return genreDAO.checkGenreInUse(id);
    }

    public List<Genre> getGenresByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return genreDAO.findByPage(offset, pageSize);
    }

    public int getTotalPages(int pageSize) {
        long totalItems = genreDAO.countAll();
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public List<Genre> searchByKeyword(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return genreDAO.findByKeywordAndPage(keyword, offset, pageSize);
    }

    public int getTotalPagesByKeyword(String keyword, int pageSize) {
        long totalItems = genreDAO.countByKeyword(keyword);
        return (int) Math.ceil((double) totalItems / pageSize);
    }
}
