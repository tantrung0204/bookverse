package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.Staff;
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

@WebServlet(name = "OrderManagementController", urlPatterns = { "/dashboard/order" })
public class OrderManagementController extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        switch (action) {
            case "search":
                String keyword = request.getParameter("keyword");
                if (keyword == null || keyword.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/dashboard/order?action=list");
                    return;
                }
                keyword = keyword.trim();

                int searchPage = 1;
                String searchPageStr = request.getParameter("page");
                if (searchPageStr != null && !searchPageStr.trim().isEmpty()) {
                    try {
                        searchPage = Integer.parseInt(searchPageStr);
                        if (searchPage < 1)
                            searchPage = 1;
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
                        if (page < 1)
                            page = 1;
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
        request.setAttribute("activeMenu", "order");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard/order");
            return;
        }

        HttpSession session = request.getSession();

        switch (action) {
            case "confirm": {
                try {
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    // Get logged-in staff from session
                    Staff staff = (Staff) session.getAttribute("user");
                    if (staff == null) {
                        session.setAttribute("error_edit", "Staff session expired. Please login again.");
                        break;
                    }
                    String error = orderService.confirmOrderByStaff(orderId, staff);
                    if (error == null) {
                        session.setAttribute("success_edit",
                                "Order #O00" + orderId + " has been confirmed!");
                    } else {
                        session.setAttribute("error_edit", error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error_edit", "Failed to confirm order: " + e.getMessage());
                }
                break;
            }

            case "ship": {
                try {
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    Staff staff = (Staff) session.getAttribute("user");
                    if (staff == null) {
                        session.setAttribute("error_edit", "Staff session expired. Please login again.");
                        break;
                    }
                    String error = orderService.shipOrder(orderId, staff);
                    if (error == null) {
                        session.setAttribute("success_edit",
                                "Order #O00" + orderId + " is now Shipping!");
                    } else {
                        session.setAttribute("error_edit", error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error_edit", "Failed to ship order: " + e.getMessage());
                }
                break;
            }

            case "complete": {
                try {
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    Staff staff = (Staff) session.getAttribute("user");
                    if (staff == null) {
                        session.setAttribute("error_edit", "Staff session expired. Please login again.");
                        break;
                    }
                    String error = orderService.completeOrder(orderId, staff);
                    if (error == null) {
                        session.setAttribute("success_edit",
                                "Order #O00" + orderId + " has been completed!");
                    } else {
                        session.setAttribute("error_edit", error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error_edit", "Failed to complete order: " + e.getMessage());
                }
                break;
            }

            case "cancel": {
                try {
                    int orderId = Integer.parseInt(request.getParameter("orderId"));
                    Staff staff = (Staff) session.getAttribute("user");
                    if (staff == null) {
                        session.setAttribute("error_edit", "Staff session expired. Please login again.");
                        break;
                    }
                    String error = orderService.cancelOrderByStaff(orderId, staff);
                    if (error == null) {
                        session.setAttribute("success_edit",
                                "Order #O00" + orderId + " has been cancelled!");
                    } else {
                        session.setAttribute("error_edit", error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("error_edit", "Failed to cancel order: " + e.getMessage());
                }
                break;
            }

            default:
                break;
        }

        response.sendRedirect(request.getContextPath() + "/dashboard/order");
    }
}
