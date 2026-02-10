/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.VoucherDAO;
import com.mycompany.bookverse.model.Voucher;
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

        boolean exists = voucherDAO.existsByCode(voucher.getVoucherCode());
        if (exists) {
            return "Voucher code already exists";
        }
        voucherDAO.create(voucher);
        return "Create voucher successfully";
    }

    public List<Voucher> searchVouchers(String keyword) {
        return voucherDAO.searchByCode(keyword);
    }

    public String updateVoucher(Voucher voucher) {

        Voucher old = voucherDAO.find(voucher.getVoucherId());
        if (old == null) {
            return "Voucher does not exist";
        }

        boolean exists = voucherDAO.existsByCodeExceptId(
                voucher.getVoucherCode(),
                voucher.getVoucherId()
        );

        if (exists) {
            return "Voucher code already exists";
        }

        voucherDAO.update(voucher);
        return "Update voucher successfully";
    }

    public String deleteVoucher(int id) {
        Voucher v = voucherDAO.find(id);
        if (v == null) {
            return "Voucher does not exist";
        }

        voucherDAO.deleteById(id);
        return "Delete voucher successfully";
    }

}
