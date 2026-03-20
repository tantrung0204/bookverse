/*
 * CheckoutController - Handles checkout page, Buy Now, Cart checkout,
 * voucher apply, and place order
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.*;
import com.mycompany.bookverse.service.CartService;
import com.mycompany.bookverse.service.CategoryService;
import com.mycompany.bookverse.service.OrderService;
import com.mycompany.bookverse.utils.VNPayConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author TrungNT - CE200064
 */
@WebServlet(name = "CheckoutController", urlPatterns = { "/checkout" })
public class CheckoutController extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();
    private final CategoryService categoryService = new CategoryService();

    /**
     * GET: Show checkout page.
     * - action=fromCart (default): checkout from cart
     * - action=buyNow: checkout single product (productId, quantity)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = getAuthenticatedCustomer(request);
        if (customer == null) {
            session.setAttribute("errorMessage", "Please login to proceed to checkout.");
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "fromCart";
        }

        switch (action) {
            case "buyNow":
                showBuyNowCheckout(request, response, customer);
                break;
            case "orderSuccess":
                // Forward to order success page (order data is in session)
                List<Category> cats = categoryService.getActiveSubCategories();
                request.setAttribute("categories", cats);
                request.getRequestDispatcher("/views/customer/order-success.jsp").forward(request, response);
                break;
            case "fromCart":
            default:
                showCartCheckout(request, response, customer);
                break;
        }
    }

    /**
     * POST: Handle voucher apply (AJAX), get vouchers (AJAX), and place order.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        switch (action) {
            case "buyNow":
                handleBuyNowPost(request, response);
                break;
            case "applyVoucher":
                applyVoucher(request, response);
                break;
            case "getAvailableVouchers":
                getAvailableVouchers(request, response);
                break;
            case "placeOrder":
                placeOrder(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/checkout");
                break;
        }
    }

    // ================================================================
    // SHOW CHECKOUT PAGE
    // ================================================================

    /**
     * Show checkout page for Buy Now (single product).
     */
    private void showBuyNowCheckout(HttpServletRequest request, HttpServletResponse response, Customer customer)
            throws ServletException, IOException {

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Validate
            String error = orderService.validateProductForCheckout(productId, quantity);
            if (error != null) {
                request.getSession().setAttribute("cartMessage", error);
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/product-detail?id=" + productId);
                return;
            }

            // Get fresh product data
            com.mycompany.bookverse.dao.OrderDAO dao = new com.mycompany.bookverse.dao.OrderDAO();
            Product product = dao.findProductById(productId);

            // Build checkout items list (single item)
            List<CheckoutItem> checkoutItems = new ArrayList<>();
            checkoutItems.add(new CheckoutItem(product, quantity));

            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(quantity));

            // Set attributes
            request.setAttribute("checkoutItems", checkoutItems);
            request.setAttribute("subtotal", subtotal);
            request.setAttribute("customer", customer);
            request.setAttribute("checkoutSource", "buyNow");
            request.setAttribute("buyNowProductId", productId);
            request.setAttribute("buyNowQuantity", quantity);
            List<Category> categories = categoryService.getActiveSubCategories();
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/views/customer/checkout.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    /**
     * Show checkout page from cart.
     */
    private void showCartCheckout(HttpServletRequest request, HttpServletResponse response, Customer customer)
            throws ServletException, IOException {

        int customerId = customer.getCustomerId();
        List<Cart> cartItems = cartService.getCustomerCart(customerId);

        // Filter only valid items
        List<Cart> validItems = new ArrayList<>();
        for (Cart item : cartItems) {
            Product p = item.getProductId();
            if (p != null && p.getStatus() != null && p.getStatus() == 1
                    && p.getStockQuantity() != null && p.getStockQuantity() > 0) {
                validItems.add(item);
            }
        }

        if (validItems.isEmpty()) {
            request.getSession().setAttribute("cartMessage", "Your cart has no valid items to checkout.");
            request.getSession().setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Validate cart
        List<String> errors = orderService.validateCartForCheckout(validItems);
        if (!errors.isEmpty()) {
            request.getSession().setAttribute("cartMessage", errors.get(0));
            request.getSession().setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Build checkout items list
        List<CheckoutItem> checkoutItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Cart cartItem : validItems) {
            Product p = cartItem.getProductId();
            int qty = cartItem.getCartQuantity();
            checkoutItems.add(new CheckoutItem(p, qty));
            subtotal = subtotal.add(p.getPrice().multiply(new BigDecimal(qty)));
        }

        request.setAttribute("checkoutItems", checkoutItems);
        request.setAttribute("subtotal", subtotal);
        request.setAttribute("customer", customer);
        request.setAttribute("checkoutSource", "cart");
        List<Category> categories = categoryService.getActiveSubCategories();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/views/customer/checkout.jsp").forward(request, response);
    }

    // ================================================================
    // BUY NOW POST
    // ================================================================

    private void handleBuyNowPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = getAuthenticatedCustomer(request);
        if (customer == null) {
            session.setAttribute("errorMessage", "Please login to proceed to checkout.");
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        String productId = request.getParameter("productId");
        String quantity = request.getParameter("quantity");

        // Redirect as GET to show checkout page
        response.sendRedirect(request.getContextPath()
                + "/checkout?action=buyNow&productId=" + productId + "&quantity=" + quantity);
    }

    /**
     * AJAX: Apply voucher code.
     * Returns JSON: {status, voucherId, voucherName, voucherCode, discountType,
     * discountValue, discount, message}
     */
    private void applyVoucher(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String voucherCode = request.getParameter("voucherCode");
            String subtotalStr = request.getParameter("subtotal");
            BigDecimal subtotal = new BigDecimal(subtotalStr);

            Voucher voucher = orderService.validateVoucher(voucherCode, subtotal);
            BigDecimal discount = orderService.calculateDiscount(voucher, subtotal);

            out.print("{");
            out.print("\"status\": \"success\",");
            out.print("\"voucherId\": " + voucher.getVoucherId() + ",");
            out.print("\"voucherName\": \"" + escapeJson(voucher.getVoucherName()) + "\",");
            out.print("\"voucherCode\": \"" + escapeJson(voucher.getVoucherCode()) + "\",");
            out.print("\"discountType\": " + voucher.getDiscountType() + ",");
            out.print("\"discountValue\": " + voucher.getDiscountValue() + ",");
            out.print("\"minOrderValue\": " + (voucher.getMinOrderValue() != null ? voucher.getMinOrderValue() : 0)
                    + ",");
            out.print("\"discount\": " + discount + ",");
            out.print("\"message\": \"Voucher applied successfully! You save " + discount.toPlainString() + " đ\"");
            out.print("}");

        } catch (Exception e) {
            out.print("{");
            out.print("\"status\": \"error\",");
            out.print("\"message\": \"" + escapeJson(e.getMessage()) + "\"");
            out.print("}");
        }
        out.flush();
    }

    /**
     * AJAX: Get list of available vouchers.
     * Returns JSON array.
     */
    private void getAvailableVouchers(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<Voucher> vouchers = orderService.getAvailableVouchers();
            StringBuilder sb = new StringBuilder("[");

            for (int i = 0; i < vouchers.size(); i++) {
                Voucher v = vouchers.get(i);
                if (i > 0)
                    sb.append(",");
                sb.append("{");
                sb.append("\"voucherId\": ").append(v.getVoucherId()).append(",");
                sb.append("\"voucherName\": \"").append(escapeJson(v.getVoucherName())).append("\",");
                sb.append("\"voucherCode\": \"").append(escapeJson(v.getVoucherCode())).append("\",");
                sb.append("\"discountType\": ").append(v.getDiscountType()).append(",");
                sb.append("\"discountValue\": ").append(v.getDiscountValue()).append(",");
                sb.append("\"minOrderValue\": ").append(v.getMinOrderValue() != null ? v.getMinOrderValue() : 0)
                        .append(",");
                sb.append("\"availableQuantity\": ").append(v.getAvailableQuantity()).append(",");
                sb.append("\"expiryDate\": \"")
                        .append(v.getExpiryDate() != null
                                ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(v.getExpiryDate())
                                : "")
                        .append("\"");
                sb.append("}");
            }

            sb.append("]");
            out.print(sb.toString());

        } catch (Exception e) {
            out.print("[]");
        }
        out.flush();
    }

    // ================================================================
    // PLACE ORDER
    // ================================================================

    private void placeOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = getAuthenticatedCustomer(request);
        if (customer == null) {
            session.setAttribute("errorMessage", "Session expired. Please login again.");
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        try {
            // Get form parameters
            String receiverName = request.getParameter("receiverName");
            String receiverPhone = request.getParameter("receiverPhone");
            String shippingAddress = request.getParameter("shippingAddress");
            String paymentMethod = request.getParameter("paymentMethod");
            String voucherCode = request.getParameter("voucherCode");
            String checkoutSource = request.getParameter("checkoutSource");

            // Validate required fields
            if (receiverName == null || receiverName.trim().isEmpty()) {
                throw new Exception("Receiver name is required.");
            }
            if (receiverPhone == null || receiverPhone.trim().isEmpty()) {
                throw new Exception("Receiver phone is required.");
            }
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                throw new Exception("Shipping address is required.");
            }
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                paymentMethod = "COD";
            }

            // Parse product IDs and quantities
            String[] pidArr = request.getParameterValues("productIds");
            String[] qtyArr = request.getParameterValues("quantities");

            if (pidArr == null || pidArr.length == 0) {
                throw new Exception("No items to order.");
            }

            List<Integer> productIds = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (int i = 0; i < pidArr.length; i++) {
                productIds.add(Integer.parseInt(pidArr[i]));
                quantities.add(Integer.parseInt(qtyArr[i]));
            }

            boolean fromCart = "cart".equals(checkoutSource);

            // Place order
            Order order = orderService.placeOrder(
                    customer, productIds, quantities,
                    voucherCode, paymentMethod,
                    receiverName.trim(), receiverPhone.trim(), shippingAddress.trim(),
                    fromCart);

            // Handle payment method
            if ("ONLINE".equals(paymentMethod)) {
                // Redirect to VNPay
                String baseUrl = request.getScheme() + "://" + request.getServerName()
                        + ":" + request.getServerPort() + request.getContextPath();
                String returnUrl = baseUrl + VNPayConfig.VNP_RETURN_URL;
                String ipAddress = VNPayConfig.getIpAddress(request);

                String paymentUrl = VNPayConfig.createPaymentUrl(
                        order.getOrderId(),
                        order.getTotalAmount(),
                        "BookVerse Order #" + order.getOrderId(),
                        ipAddress,
                        returnUrl);

                response.sendRedirect(paymentUrl);
            } else {
                // COD - redirect to success page
                session.setAttribute("orderSuccess", order);
                response.sendRedirect(request.getContextPath()
                        + "/checkout?action=orderSuccess&orderId=" + order.getOrderId());
            }

        } catch (Exception e) {
            session.setAttribute("checkoutError", e.getMessage());
            // Redirect back based on source
            String src = request.getParameter("checkoutSource");
            if ("buyNow".equals(src)) {
                String pid = request.getParameter("productIds");
                String qty = request.getParameter("quantities");
                if (pid != null && !pid.isEmpty()) {
                    response.sendRedirect(request.getContextPath()
                            + "/checkout?action=buyNow&productId=" + pid + "&quantity=" + qty);
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/checkout?action=fromCart");
            }
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Object userObj = session.getAttribute("user");

        if ("customer".equals(role) && userObj instanceof Customer) {
            return (Customer) userObj;
        }
        return null;
    }

    private String escapeJson(String value) {
        if (value == null)
            return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Inner class to represent a checkout item (product + quantity).
     */
    public static class CheckoutItem implements java.io.Serializable {
        private Product product;
        private int quantity;

        public CheckoutItem() {
        }

        public CheckoutItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getLineTotal() {
            if (product != null && product.getPrice() != null) {
                return product.getPrice().multiply(new BigDecimal(quantity));
            }
            return BigDecimal.ZERO;
        }
    }

    @Override
    public String getServletInfo() {
        return "Checkout Controller";
    }
}
