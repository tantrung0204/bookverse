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

    public String insertGenre(String name, String des) {

        if (des == null || des.trim().isEmpty()) {
            return "Description is empty";
        }
        if (name == null || name.trim().isEmpty()) {
            return "Name is empty";
        }

        boolean checkExist = genreDAO.checkGenreExist(name);

        if (!checkExist) {// nếu chưa tồn tại thì bắt đầu tạo genre mới
            Genre genre = new Genre();
            genre.setGenreName(name);
            genre.setDescriptionText(des);
            genre.setStatus(0);// set mặt định là 0 (inactive)
            if (genreDAO.createGenre(genre)) {// nếu tạo thành công thì trả về 0
                return "Create genre successfully";
            } else // tạo không được thì trả về 1 (các lỗi như database bị mất kết nối, mạng kém ...)
            {
                return "Create genre false";
            }
        } else {// tạo không được do genre bị trùng thì trả về 2
            return "Genre already exists";
        }
    }

    public String editGenre(int id, String name, String description, int status) {
        boolean checkExist = genreDAO.checkGenreExist(name);
        Genre old = genreDAO.findById(id);
        if (!checkExist) {
            return "Genre does not exist";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description is empty";
        }
        if (name == null || name.trim().isEmpty()) {
            return "Name is empty";
        }
        if (status != 0 && status != 1) {
            return "status must be 1 or 2";
        }
        old.setGenreName(name);
        old.setDescriptionText(description);
        old.setStatus(status);

        if (genreDAO.update(old)) {
            return "Update genre successfully";
        } else {
            return "Update genre false";
        }
    }
    public String deleteGenre(int id){
        if(findGenreById(id)==null){
            return "Genre does not exist";
        }
        if(genreDAO.checkGenreInUse(id)){
            return "Genre is in use";
        }
        if(genreDAO.deleteById(id)){
        return "Delete genre successfully";
        }
        else return "Delete genre false";
    }
}
