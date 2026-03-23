package com.mycompany.bookverse.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter chặn truy cập dashboard theo role:
 * - Guest/Customer: không được vào /dashboard/*
 * - Admin: toàn quyền truy cập dashboard
 * - Seller: chỉ được truy cập order, feedback, product, statistics
 * - Warehouse: chỉ được truy cập product, supplier, inventory, statistics
 */
@WebFilter(filterName = "DashboardAuthFilter", urlPatterns = {"/dashboard/*"})
public class DashboardAuthFilter implements Filter {

    // Các path mà mọi staff role đều được truy cập
    private static final List<String> COMMON_ALLOWED = Arrays.asList(
            "/dashboard",
            "/dashboard/statistics"
    );

    // Các path riêng cho từng role (ngoài COMMON_ALLOWED)
    private static final Map<String, List<String>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        ROLE_PERMISSIONS.put("seller", Arrays.asList(
                "/dashboard/order",
                "/dashboard/feedback",
                "/dashboard/product"
        ));

        ROLE_PERMISSIONS.put("warehouse", Arrays.asList(
                "/dashboard/product",
                "/dashboard/supplier",
                "/dashboard/inventory"
        ));

        // Admin có toàn quyền, không cần liệt kê
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String contextPath = httpRequest.getContextPath();

        // 1. Kiểm tra đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(contextPath + "/signin");
            return;
        }

        String role = (String) session.getAttribute("role");

        // 2. Customer không được vào dashboard
        if ("customer".equals(role)) {
            httpResponse.sendRedirect(contextPath + "/home");
            return;
        }

        // 3. Admin được truy cập tất cả
        if ("admin".equals(role)) {
            chain.doFilter(request, response);
            return;
        }

        // 4. Kiểm tra quyền cho seller/warehouse
        String requestURI = httpRequest.getRequestURI();
        String path = requestURI.substring(contextPath.length());

        // Loại bỏ query string nếu có
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }

        // Kiểm tra common paths
        if (COMMON_ALLOWED.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra role-specific paths
        List<String> allowedPaths = ROLE_PERMISSIONS.get(role);
        if (allowedPaths != null) {
            for (String allowed : allowedPaths) {
                if (path.equals(allowed) || path.startsWith(allowed + "/") || path.startsWith(allowed + "?")) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        // 5. Không có quyền → redirect về dashboard chính với thông báo lỗi
        session.setAttribute("accessDenied", "You do not have permission to access this page.");
        httpResponse.sendRedirect(contextPath + "/dashboard");
    }

    @Override
    public void destroy() {
    }
}
