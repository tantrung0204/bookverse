/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.FeedbackDAO;
import com.mycompany.bookverse.model.Feedback;
import java.util.List;

/**
 *
 * @author Admin
 */
public class FeedbackService {

    private final FeedbackDAO dao = new FeedbackDAO();

    public List<Feedback> getByPage(int page, int pageSize) {
        return dao.getByPage(page, pageSize);
    }

    public int getTotalPages(int pageSize) {
        long total = dao.countAll();
        return (int) Math.ceil((double) total / pageSize);
    }

    public List<Feedback> search(String keyword, Integer rating, int page, int pageSize) {
        return dao.search(keyword, rating, page, pageSize);
    }

    public int getSearchPages(String keyword, Integer rating, int pageSize) {
        long total = dao.countSearch(keyword, rating);
        return (int) Math.ceil((double) total / pageSize);
    }

    public String delete(int id) {

        Feedback f = dao.findById(id);

        if (f == null) {
            return "Feedback not found";
        }

        dao.delete(id);
        return "Delete feedback successfully";
    }

    public String deleteForCustomer(int id, int customerId) {

        Feedback f = dao.findById(id);

        if (f == null) {
            return "Feedback not found";
        }

        if (!f.getCustomerId().getCustomerId().equals(customerId)) {
            return "You cannot delete this feedback";
        }

        dao.delete(id);

        return "Delete feedback successfully";
    }

    public List<Feedback> getByCustomer(int customerId) {
        return dao.getByCustomerId(customerId);
    }

    public boolean create(int customerId, int productId, int rating, String content) {
        return dao.create(customerId, productId, rating, content);
    }

    public String update(int id, int rating, String content) {

        Feedback f = dao.findById(id);

        if (f == null) {
            return "Feedback not found";
        }

        f.setRating(rating);
        f.setContentText(content);

        dao.update(f);

        return "Update feedback successfully";
    }

}
