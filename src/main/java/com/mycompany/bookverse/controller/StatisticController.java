/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.controller;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.service.StatisticService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author huyqu
 */
@WebServlet(name = "StatisticController", urlPatterns = {"/dashboard/statistics"})
public class StatisticController extends HttpServlet {

    private StatisticService statisticService = new StatisticService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       
        request.setAttribute("totalRevenue", statisticService.getTotalRevenue());
        request.setAttribute("totalOrders", statisticService.getTotalOrders());
        request.setAttribute("totalCustomers", statisticService.getTotalCustomers());
        
        Object[] topProduct = statisticService.getTopProduct();
        request.setAttribute("topProductName", topProduct[0]);
        request.setAttribute("topProductSold", topProduct[1]);
        
       
        List<Object[]> statusData = statisticService.getOrdersByStatus();
        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for (Object[] row : statusData) {
            labels.append("'").append(row[0]).append("',");
            data.append(row[1]).append(",");
        }
        request.setAttribute("statusLabels", labels.length() > 0 ? labels.toString() : "'Completed','Pending','Cancelled'");
        request.setAttribute("statusData", data.length() > 0 ? data.toString() : "0,0,0");

     
        List<Order> completedOrders = statisticService.getCompletedOrders();
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM"); 
        
        for (Order o : completedOrders) {
            if (o.getCreatedAt() != null && o.getTotalAmount() != null) {
                String dateStr = sdf.format(o.getCreatedAt());
                BigDecimal currentRev = revenueMap.getOrDefault(dateStr, BigDecimal.ZERO);
                revenueMap.put(dateStr, currentRev.add(o.getTotalAmount()));
            }
        }
        
        StringBuilder revLabels = new StringBuilder();
        StringBuilder revData = new StringBuilder();
        for (Map.Entry<String, BigDecimal> entry : revenueMap.entrySet()) {
            revLabels.append("'").append(entry.getKey()).append("',");
            revData.append(entry.getValue()).append(",");
        }
        request.setAttribute("revLabels", revLabels.length() > 0 ? revLabels.toString() : "'No Data'");
        request.setAttribute("revData", revData.length() > 0 ? revData.toString() : "0");

   
        request.setAttribute("recentOrders", statisticService.getRecentOrders());

   
        request.setAttribute("contentPage", "statistic-list.jsp");
        request.setAttribute("activeMenu", "statistics");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }
}
