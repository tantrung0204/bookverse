package com.mycompany.bookverse.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter ngăn chặn staff (admin, seller, warehouse) truy cập
 * các trang dành cho customer/public như home, shop, cart, checkout, ...
 * 
 * Staff phải ở trong dashboard, không được vào trang public.
 */
@WebFilter(filterName = "CustomerPageFilter", urlPatterns = {
    "/home", "/shop", "/cart", "/checkout", "/customer-order",
    "/product-detail", "/wishlist"
})
public class CustomerPageFilter implements Filter {

    // Các role của staff (quản trị)
    private static final List<String> STAFF_ROLES = Arrays.asList(
            "admin", "seller", "warehouse"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Nếu chưa đăng nhập hoặc là customer → cho qua bình thường
        if (session == null || session.getAttribute("user") == null) {
            chain.doFilter(request, response);
            return;
        }

        String role = (String) session.getAttribute("role");

        // Nếu là customer → cho qua bình thường
        if ("customer".equals(role)) {
            chain.doFilter(request, response);
            return;
        }

        // Nếu là staff role → chặn, redirect về dashboard
        if (STAFF_ROLES.contains(role)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            return;
        }

        // Các trường hợp khác → cho qua
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
