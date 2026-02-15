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

    public Genre findGenreByName(String name) {
        return genreDAO.findByName(name);
    }

    public int insertGenre(String name, String des) {
        if ((name == null || name.trim().isEmpty()) && (des == null || des.trim().isEmpty())) {
            return 5;
        } else if (des == null || des.trim().isEmpty()) {
            return 4;
        } else if (name == null || name.trim().isEmpty()) {
            return 3;
        }
        List<Genre> list = genreDAO.findAll();// Lấy danh sách genre trong database
        boolean checkExist = false;
        for (Genre genre : list) {// Kiểm tra xem genre đã tồn tại trong db chưa.
            if (genre.getGenreName().equalsIgnoreCase(name)) {// nếu đã tồn tại thì trả về true
                checkExist = true;
                break;
            }
        }
        if (!checkExist) {// nếu chưa tồn tại thì bắt đầu tạo genre mới
            Genre genre = new Genre();
            genre.setGenreName(name);
            genre.setDescriptionText(des);
            genre.setStatus(0);// set mặt định là 0 (inactive)
            if (genreDAO.createGenre(genre)) {// nếu tạo thành công thì trả về 0
                return 0;
            } else // tạo không được thì trả về 1 (các lỗi như database bị mất kết nối, mạng kém ...)
            {
                return 1;
            }
        } else {// tạo không được do genre bị trùng thì trả về 2
            return 2;
        }
    }
}
