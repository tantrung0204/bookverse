/*
 * OrderService - Business logic for order checkout flow
 */
package com.mycompany.bookverse.service;

import com.mycompany.bookverse.dao.FeedbackDAO;
import com.mycompany.bookverse.dao.OrderDAO;
import com.mycompany.bookverse.model.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.mycompany.bookverse.dao.OrderItemDAO;

/**
 * @author TrungNT - CE200064
 */
public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    public List<Order> getAllOrders(int page, int pageSize) {
        return orderDAO.findAll(page, pageSize);
    }

    public long getTotalOrders() {
        return orderDAO.getTotalOrders();
    }

    public List<Order> searchOrders(String keyword, int page, int pageSize) {
        return orderDAO.searchOrders(keyword, page, pageSize);
    }

    public long getTotalSearchOrders(String keyword) {
        return orderDAO.getTotalSearchOrders(keyword);
    }

    public String editOrder(int id, boolean isPaid, String orderStatus) {
        Order oldOrder = orderDAO.findById(id);
        if (oldOrder != null) {
            oldOrder.setIsPaid(isPaid);
            oldOrder.setOrderStatus(orderStatus);

            boolean result = orderDAO.update(oldOrder);
            return result ? "Edit successfully" : "Edit false";
        }
        return "Order not found";
    }

    // =============================================
    // VALIDATION METHODS
    // =============================================
    /**
     * Validate a single product for checkout (Buy Now). Returns error message
     * or null if valid.
     */
    public String validateProductForCheckout(int productId, int quantity) {
        Product product = orderDAO.findProductById(productId);
        if (product == null) {
            return "Product does not exist.";
        }
        if (product.getStatus() == null || product.getStatus() != 1) {
            return "Product \"" + product.getName() + "\" is currently unavailable.";
        }
        if (product.getStockQuantity() == null || product.getStockQuantity() <= 0) {
            return "Product \"" + product.getName() + "\" is out of stock.";
        }
        if (quantity <= 0) {
            return "Quantity must be at least 1.";
        }
        if (quantity > product.getStockQuantity()) {
            return "Only " + product.getStockQuantity() + " units of \"" + product.getName() + "\" are available.";
        }
        return null;
    }

    /**
     * Validate all cart items for checkout. Returns list of error messages
     * (empty if all valid).
     */
    public List<String> validateCartForCheckout(List<Cart> cartItems) {
        List<String> errors = new ArrayList<>();
        if (cartItems == null || cartItems.isEmpty()) {
            errors.add("Your cart is empty.");
            return errors;
        }
        for (Cart item : cartItems) {
            Product p = item.getProductId();
            if (p == null) {
                errors.add("A product in your cart no longer exists.");
                continue;
            }
            // Re-check from DB for real-time stock
            Product fresh = orderDAO.findProductById(p.getProductId());
            if (fresh == null) {
                errors.add("Product \"" + p.getName() + "\" no longer exists.");
            } else if (fresh.getStatus() == null || fresh.getStatus() != 1) {
                errors.add("Product \"" + fresh.getName() + "\" is currently unavailable.");
            } else if (fresh.getStockQuantity() == null || fresh.getStockQuantity() <= 0) {
                errors.add("Product \"" + fresh.getName() + "\" is out of stock.");
            } else if (item.getCartQuantity() > fresh.getStockQuantity()) {
                errors.add("Only " + fresh.getStockQuantity() + " units of \"" + fresh.getName()
                        + "\" are available (you requested " + item.getCartQuantity() + ").");
            }
        }
        return errors;
    }

    // =============================================
    // VOUCHER METHODS
    // =============================================
    /**
     * Get all available vouchers for display in the modal.
     */
    public List<Voucher> getAvailableVouchers(BigDecimal subtotal) {
        return orderDAO.findAvailableVouchers(subtotal);
    }

    /**
     * Validate a voucher code and check if it can be applied to the order.
     * Returns the Voucher if valid, throws Exception with error message
     * otherwise.
     */
    public Voucher validateVoucher(String voucherCode, BigDecimal orderTotal) throws Exception {
        if (voucherCode == null || voucherCode.trim().isEmpty()) {
            throw new Exception("Please enter a voucher code.");
        }

        Voucher voucher = orderDAO.findVoucherByCode(voucherCode.trim());
        if (voucher == null) {
            throw new Exception("Voucher code \"" + voucherCode + "\" does not exist.");
        }

        if (voucher.getStatus() == null || voucher.getStatus() != 1) {
            throw new Exception("This voucher is no longer active.");
        }

        if (voucher.getAvailableQuantity() == null || voucher.getAvailableQuantity() <= 0) {
            throw new Exception("This voucher has been fully redeemed.");
        }

        Date now = new Date();
        if (voucher.getStartDate() != null && now.before(voucher.getStartDate())) {
            throw new Exception("This voucher is not yet valid.");
        }
        if (voucher.getExpiryDate() != null && now.after(voucher.getExpiryDate())) {
            throw new Exception("This voucher has expired.");
        }

        if (voucher.getMinOrderValue() != null
                && orderTotal.compareTo(voucher.getMinOrderValue()) < 0) {
            throw new Exception("Minimum order value for this voucher is "
                    + voucher.getMinOrderValue().setScale(0, RoundingMode.FLOOR).toPlainString() + " đ.");
        }

        return voucher;
    }

    /**
     * Calculate the discount amount for a voucher. discountType 1 = percentage,
     * discountType 2 = fixed amount.
     */
    public BigDecimal calculateDiscount(Voucher voucher, BigDecimal orderTotal) {
        if (voucher == null || voucher.getDiscountValue() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if (voucher.getDiscountType() != null && voucher.getDiscountType() == 1) {
            // Percentage discount
            discount = orderTotal.multiply(voucher.getDiscountValue())
                    .divide(new BigDecimal("100"), 0, RoundingMode.FLOOR);
        } else {
            // Fixed amount discount
            discount = voucher.getDiscountValue();
        }

        // Discount cannot exceed orderTotal
        if (discount.compareTo(orderTotal) > 0) {
            discount = orderTotal;
        }

        return discount;
    }

    // =============================================
    // PLACE ORDER
    // =============================================
    /**
     * Place an order.
     *
     * @param customer        The customer placing the order
     * @param productIds      List of product IDs
     * @param quantities      List of quantities (same order as productIds)
     * @param voucherCode     Voucher code (nullable)
     * @param paymentMethod   "COD" or "ONLINE"
     * @param receiverName    Receiver's name
     * @param receiverPhone   Receiver's phone number
     * @param shippingAddress Shipping address
     * @param fromCart        Whether the order is from cart (to clear cart after)
     * @return The created Order with orderId set
     */
    public Order placeOrder(Customer customer,
            List<Integer> productIds,
            List<Integer> quantities,
            String voucherCode,
            String paymentMethod,
            String receiverName,
            String receiverPhone,
            String shippingAddress,
            boolean fromCart) throws Exception {

        if (productIds == null || productIds.isEmpty()) {
            throw new Exception("No items to order.");
        }

        // 1. Re-validate stock for each product
        BigDecimal subtotal = BigDecimal.ZERO;
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < productIds.size(); i++) {
            int pid = productIds.get(i);
            int qty = quantities.get(i);

            Product product = orderDAO.findProductById(pid);
            if (product == null) {
                throw new Exception("Product ID " + pid + " does not exist.");
            }
            if (product.getStatus() == null || product.getStatus() != 1) {
                throw new Exception("Product \"" + product.getName() + "\" is currently unavailable.");
            }
            if (product.getStockQuantity() == null || product.getStockQuantity() < qty) {
                throw new Exception("Not enough stock for \"" + product.getName() + "\". Available: "
                        + (product.getStockQuantity() == null ? 0 : product.getStockQuantity()));
            }
            products.add(product);
            subtotal = subtotal.add(product.getPrice().multiply(new BigDecimal(qty)));
        }

        // 2. Validate and calculate voucher discount
        Voucher voucher = null;
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            voucher = validateVoucher(voucherCode, subtotal);
            discount = calculateDiscount(voucher, subtotal);
        }

        // 3. Calculate total
        BigDecimal totalAmount = subtotal.subtract(discount);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        // 4. Create Order entity
        Order order = new Order();
        order.setCustomerId(customer);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus("Pending");
        order.setPaymentMethod(paymentMethod);
        order.setIsPaid(false);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setShippingAddress(shippingAddress);
        order.setCreatedAt(new Date());

        if (voucher != null) {
            order.setVoucherId(voucher);
        }

        // 5. Persist Order
        order = orderDAO.createOrder(order);

        // 6. Create OrderItems + deduct stock
        for (int i = 0; i < productIds.size(); i++) {
            int pid = productIds.get(i);
            int qty = quantities.get(i);

            OrderItem item = new OrderItem();
            item.setOrderId(order);
            item.setProductId(new Product(pid));
            item.setOrderQuantity(qty);
            orderDAO.createOrderItem(item);

            // Deduct stock
            orderDAO.updateProductStock(pid, qty);
        }

        // 7. Decrement voucher quantity
        if (voucher != null) {
            orderDAO.decrementVoucherQuantity(voucher.getVoucherId());
        }

        // 8. Clear cart if from cart
        if (fromCart && customer.getCustomerId() != null) {
            orderDAO.clearCart(customer.getCustomerId());
        }

        return order;
    }

    /**
     * Update order's isPaid status (for VNPay callback).
     */
    public void updatePaymentStatus(int orderId, boolean isPaid) {
        Order order = orderDAO.findById(orderId);
        if (order != null) {
            order.setIsPaid(isPaid);
            orderDAO.updateOrder(order);
        }
    }

    /**
     * Find Order by ID.
     */
    public Order getOrderById(int orderId) {
        return orderDAO.findById(orderId);
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDAO.getOrdersByCustomerId(customerId);
    }

    public Order getOrder(int orderId) {
        return orderDAO.findById(orderId);
    }

    public boolean cancelOrder(int orderId) {

        Order order = orderDAO.findById(orderId);

        if (order != null && "Pending".equalsIgnoreCase(order.getOrderStatus())) {

            order.setOrderStatus("Canceled");
            orderDAO.update(order);
            return true;
        }

        return false;
    }

    public boolean confirmReceived(int orderId) {

        Order order = orderDAO.findById(orderId);

        if (order != null && "Shipping".equalsIgnoreCase(order.getOrderStatus())) {

            order.setOrderStatus("Completed");
            orderDAO.update(order);
            return true;
        }

        return false;
    }

    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemDAO.getByOrder(orderId);
    }

    private FeedbackDAO feedbackDAO = new FeedbackDAO();

    public boolean isReviewed(int customerId, int productId) {
        return feedbackDAO.isProductReviewed(customerId, productId);
    }

}
