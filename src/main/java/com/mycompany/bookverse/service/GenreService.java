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

    public List<Genre> getAllGenres() {
        return genreDAO.findAll();
    }

    public Genre findGenreById(int id) {
        return genreDAO.findById(id);
    }

    public List<Genre> searchGenres(String keyword) {
        return genreDAO.searchByName(keyword);
    }

    public String insertGenre(String name, String des, int status) {

        if (des == null || des.trim().isEmpty()) {
            return "Description is empty";
        }
        if (name == null || name.trim().isEmpty()) {
            return "Name is empty";
        }

        boolean checkExist = genreDAO.checkGenreExistByName(name);

        if (!checkExist) {
            Genre genre = new Genre();
            genre.setGenreName(name);
            genre.setDescriptionText(des);
            genre.setStatus(status);
            if (genreDAO.createGenre(genre)) {
                return "Create genre successfully";
            } else {
                return "Create genre false";
            }
        } else {
            return "Genre already exists";
        }
    }

    public String editGenre(int id, String name, String description, int status) {
        String error = "";
        boolean checkExist = genreDAO.checkGenreExist(id, name);
        Genre old = genreDAO.findById(id);
        if (checkExist) {
            return "Genre already exist.";
        }
        if (description == null || description.trim().isEmpty()) {
            error += "Description can not be empty.\n";
        } else if (!description.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Description contains invalid characters.\n";
        }
        if (name == null || name.trim().isEmpty()) {
            error += "Name cannot be left blank.\n";
        } else if (!name.matches("^[a-zA-ZÀ-ỹ0-9\\s\\-_&.]+$")) {
            error += "Name contains invalid characters.\n";
        }
        if (status != 0 && status != 1) {
            error += "status must be 1 or 2.\n";
        }

        if (error.isEmpty()) {

            old.setGenreName(name);
            old.setDescriptionText(description);
            old.setStatus(status);

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
}
