/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.CartDAO;
import com.mycompany.bookverse.model.*;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author TrungNT - CE200064
 */
public class CartService {

    private CartDAO cartDAO = new CartDAO();

    public List<Cart> getCustomerCart(int customerId) {
        return cartDAO.getCartByCustomerId(customerId);
    }

    public BigDecimal calculateCartTotal(List<Cart> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        if (cartItems != null && !cartItems.isEmpty()) {
            for (Cart item : cartItems) {
                Product p = item.getProductId();

                if (p.getStatus() != null && p.getStatus() == 1
                        && p.getStockQuantity() != null && p.getStockQuantity() > 0) {

                    BigDecimal price = p.getPrice();
                    BigDecimal quantity = new BigDecimal(item.getCartQuantity());

                    total = total.add(price.multiply(quantity));
                }
            }
        }
        return total;
    }

    public void addToCart(int customerId, int productId, int quantity) {
        Cart existingCartItem = cartDAO.findByCustomerAndProduct(customerId, productId);

        if (existingCartItem != null) {
            // Đã tồn tại -> Cộng dồn số lượng
            int currentQty = existingCartItem.getCartQuantity();
            existingCartItem.setCartQuantity(currentQty + quantity);
            cartDAO.save(existingCartItem);
        } else {
            // Chưa tồn tại -> Tạo mới
            Cart newItem = new Cart();
            newItem.setCartQuantity(quantity);

            newItem.setCustomerId(new Customer(customerId));
            newItem.setProductId(new Product(productId));

            cartDAO.save(newItem);
        }
    }

    public void updateCartQuantity(int cartId, int newQuantity) throws Exception {
        Cart item = cartDAO.findById(cartId);
        if (item != null) {
            int stock = item.getProductId().getStockQuantity();
            if (newQuantity > stock) {
                throw new Exception("Exceeds available stock quantity.");
            }
            item.setCartQuantity(newQuantity);
            cartDAO.save(item);
        }
    }

    public boolean removeCartItem(int cartId, int customerId) {
        Cart item = cartDAO.findById(cartId);

        if (item != null && item.getCustomerId() != null
                && item.getCustomerId().getCustomerId() == customerId) {
            cartDAO.delete(cartId);
            return true;
        }

        return false;
    }
}
