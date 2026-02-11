/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.VoucherDAO;
import com.mycompany.bookverse.model.Voucher;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Admin
 */
public class VoucherService {

    private final VoucherDAO voucherDAO = new VoucherDAO();

    public List<Voucher> getVouchers() {
        List<Voucher> list = voucherDAO.getAllVouchers();
        return list;
    }

    public Voucher getVoucherById(int id) {
        return voucherDAO.findById(id);
    }

    public String createVoucher(Voucher voucher) {

        if (voucher.getAvailableQuantity() < 0) {
            return "Quantity cannot be less than 0";
        }

        if (voucher.getDiscountPercent() == null
                || voucher.getDiscountPercent().compareTo(BigDecimal.ZERO) < 0
                || voucher.getDiscountPercent().compareTo(new BigDecimal("100")) > 0) {
            return "Discount must be between 0 and 100";
        }

        if (voucher.getExpiryDate() == null) {
            return "Expiry date is required";
        }

        if (voucher.getExpiryDate().before(voucher.getStartDate())) {
            return "Expiry date must be after start date";
        }

        long diff = voucher.getExpiryDate().getTime() - voucher.getStartDate().getTime();
        long hours = diff / (1000 * 60 * 60);

        if (hours < 24) {
            return "Voucher must be valid at least 24 hours";
        }

        if (voucherDAO.existsByCode(voucher.getVoucherCode())) {
            return "Voucher code already exists";
        }

        voucherDAO.create(voucher);
        return "Create voucher successfully";
    }

    public List<Voucher> searchVouchers(String keyword) {
        return voucherDAO.searchByCode(keyword);
    }

    public String updateVoucher(Voucher voucher) {

        Voucher old = voucherDAO.findById(voucher.getVoucherId());
        if (old == null) {
            return "Voucher does not exist";
        }

        if (voucher.getAvailableQuantity() < 0) {
            return "Quantity cannot be less than 0";
        }

        if (voucher.getDiscountPercent() == null
                || voucher.getDiscountPercent().compareTo(BigDecimal.ZERO) < 0
                || voucher.getDiscountPercent().compareTo(new BigDecimal("100")) > 0) {
            return "Discount must be between 0 and 100";
        }

        if (voucher.getExpiryDate().before(old.getStartDate())) {
            return "Expiry date must be after start date";
        }

        long diff = voucher.getExpiryDate().getTime() - old.getStartDate().getTime();
        long hours = diff / (1000 * 60 * 60);

        if (hours < 24) {
            return "Voucher must be valid at least 24 hours";
        }

        if (voucherDAO.existsByCodeExceptId(
                voucher.getVoucherCode(),
                voucher.getVoucherId())) {
            return "Voucher code already exists";
        }

        voucher.setStartDate(old.getStartDate());
        voucherDAO.update(voucher);

        return "Update voucher successfully";
    }

    public String deleteVoucher(int id) {
        Voucher v = voucherDAO.findById(id);
        if (v == null) {
            return "Voucher does not exist";
        }

        voucherDAO.deleteById(id);
        return "Delete voucher successfully";
    }

}
