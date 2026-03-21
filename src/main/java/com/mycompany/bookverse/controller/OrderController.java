package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.service.OrderService;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "OrderController", urlPatterns = {"/order"})
public class OrderController extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        switch (action) {
           case "search":
                String keyword = request.getParameter("keyword");
                if (keyword == null || keyword.trim().isEmpty()) {
                    response.sendRedirect("order?action=list");
                    return;
                }
                keyword = keyword.trim();
                
                // Lấy page hiện tại (giống y chang case "list")
                int searchPage = 1;
                String searchPageStr = request.getParameter("page");
                if (searchPageStr != null && !searchPageStr.trim().isEmpty()) {
                    try {
                        searchPage = Integer.parseInt(searchPageStr);
                        if (searchPage < 1) searchPage = 1;
                    } catch (NumberFormatException e) {
                        searchPage = 1;
                    }
                }
                
                int searchPageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                
                List<Order> searchResults = orderService.searchOrders(keyword, searchPage, searchPageSize);
                
                long totalSearch = orderService.getTotalSearchOrders(keyword);
                int totalSearchPages = (int) Math.ceil((double) totalSearch / searchPageSize);

                request.setAttribute("orders", searchResults);
                request.setAttribute("searchKeyword", keyword);
                request.setAttribute("currentPage", searchPage);
                request.setAttribute("totalPages", totalSearchPages);
                request.setAttribute("pageSize", searchPageSize);
                break;

            case "list":
            default:
                int page = 1;
                String pageStr = request.getParameter("page");
                if (pageStr != null && !pageStr.trim().isEmpty()) {
                    try {
                        page = Integer.parseInt(pageStr);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }
                int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
                List<Order> listForView = orderService.getAllOrders(page, pageSize);
                long totalOrders = orderService.getTotalOrders();
                int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

                request.setAttribute("orders", listForView);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                break;
        }

        request.setAttribute("contentPage", "order-list.jsp");
        request.setAttribute("activeMenu", "order"); // Để menu sáng lên
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("order");
            return;
        }

        switch (action) {
            case "edit":
                try {
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    
                    String isPaidStr = request.getParameter("status"); 
                    boolean isPaid = "1".equals(isPaidStr); 
                    
                    String orderStatus = request.getParameter("orderStatus");

                    // Gọi Service update (hàm này mình đã làm chuẩn ở mấy bước trước)
                    String msg = orderService.editOrder(orderId, isPaid, orderStatus);

                    if (msg.contains("successfully")) {
                        request.getSession().setAttribute("success_edit", "Update Order Successfully!");
                    } else {
                        request.getSession().setAttribute("error_edit", "Update Order Failed: " + msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.getSession().setAttribute("error_edit", "Invalid input format!");
                }
                response.sendRedirect("order");
                break;

            case "cancel":
                try {
                    int orderIdCancel = Integer.parseInt(request.getParameter("orderId"));
                    
                    // Gọi hàm cancelOrder từ Service (Hàm này chuyển orderStatus thành "Cancelled")
                    boolean success = orderService.cancelOrder(orderIdCancel);
                    
                    if (success) {
                        request.getSession().setAttribute("success_edit", "Order #O00" + orderIdCancel + " has been cancelled!");
                    } else {
                        request.getSession().setAttribute("error_edit", "Failed to cancel order!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect("order");
                break;

            default:
                response.sendRedirect("order");
                break;
        }
    }
}
