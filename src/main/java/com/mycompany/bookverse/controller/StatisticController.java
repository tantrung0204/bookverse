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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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

    private Date[] getDateRange(String filter, String startStr, String endStr) {
        if (filter != null && filter.equals("custom") && startStr != null && !startStr.isEmpty() && endStr != null && !endStr.isEmpty()) {
            try {
                LocalDate start = LocalDate.parse(startStr);
                LocalDate end = LocalDate.parse(endStr);
                Date startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date endDate = Date.from(end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                return new Date[]{startDate, endDate};
            } catch (Exception e) {
            }
        }
        if (filter == null || filter.isEmpty()) {
            filter = "month";
        }
        LocalDate now = LocalDate.now();
        LocalDateTime start = null;
        LocalDateTime end = LocalDateTime.now();
        switch (filter.toLowerCase()) {
            case "week":
                start = now.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay();
                break;
            case "month":
                start = now.withDayOfMonth(1).atStartOfDay();
                break;
            case "year":
                start = now.withDayOfYear(1).atStartOfDay();
                break;
            default:
                start = now.withDayOfMonth(1).atStartOfDay();
        }
        Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
        return new Date[]{startDate, endDate};
    }

    private Date[] getPreviousDateRange(Date[] currentRange) {
        if (currentRange[0] == null || currentRange[1] == null) {
            return new Date[]{null, null};
        }
        LocalDateTime currentStart = currentRange[0].toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentEnd = currentRange[1].toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long daysBetween = ChronoUnit.DAYS.between(currentStart, currentEnd);
        if (daysBetween == 0) {
            daysBetween = 1;
        }
        LocalDateTime previousStart = currentStart.minusDays(daysBetween);
        LocalDateTime previousEnd = currentStart;
        return new Date[]{
            Date.from(previousStart.atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(previousEnd.atZone(ZoneId.systemDefault()).toInstant())
        };
    }

    private String getTrend(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? "+100%" : "0%";
        }
        BigDecimal diff = current.subtract(previous);
        BigDecimal percent = diff.divide(previous, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        String sign = percent.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
        return sign + percent.setScale(1, java.math.RoundingMode.HALF_UP).toString() + "%";
    }

    private String getTrend(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? "+100%" : "0%";
        }
        double diff = current - previous;
        double percent = (diff / previous) * 100;
        String sign = percent > 0 ? "+" : "";
        return sign + String.format(java.util.Locale.US, "%.1f", percent) + "%";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String filter = request.getParameter("filter");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        if ((filter == null || filter.isEmpty()) && (startDateStr == null || startDateStr.isEmpty())) {
            filter = "month";
        }

        Date[] currentRange = getDateRange(filter, startDateStr, endDateStr);
        Date[] prevRange = getPreviousDateRange(currentRange);

        request.setAttribute("filter", (filter != null) ? filter : "custom");
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
       
        BigDecimal currentRevenue = statisticService.getTotalRevenue(currentRange[0], currentRange[1]);
        BigDecimal prevRevenue = statisticService.getTotalRevenue(prevRange[0], prevRange[1]);
        request.setAttribute("totalRevenue", currentRevenue);
        request.setAttribute("revenueTrend", getTrend(currentRevenue, prevRevenue));

        long currentOrders = statisticService.getTotalOrders(currentRange[0], currentRange[1]);
        long prevOrders = statisticService.getTotalOrders(prevRange[0], prevRange[1]);
        request.setAttribute("totalOrders", currentOrders);
        request.setAttribute("orderTrend", getTrend(currentOrders, prevOrders));

        long currentCustomers = statisticService.getTotalCustomers(currentRange[0], currentRange[1]);
        long prevCustomers = statisticService.getTotalCustomers(prevRange[0], prevRange[1]);
        request.setAttribute("totalCustomers", currentCustomers);
        request.setAttribute("customerTrend", getTrend(currentCustomers, prevCustomers));
        
        Object[] topProduct = statisticService.getTopProduct(currentRange[0], currentRange[1]);
        request.setAttribute("topProductName", topProduct[0]);
        request.setAttribute("topProductSold", topProduct[1]);
        
       
        List<Object[]> statusData = statisticService.getOrdersByStatus(currentRange[0], currentRange[1]);
        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for (Object[] row : statusData) {
            labels.append("'").append(row[0]).append("',");
            data.append(row[1]).append(",");
        }
        request.setAttribute("statusLabels", labels.length() > 0 ? labels.toString() : "'Completed','Pending','Cancelled'");
        request.setAttribute("statusData", data.length() > 0 ? data.toString() : "0,0,0");

     
        List<Order> completedOrders = statisticService.getCompletedOrders(currentRange[0], currentRange[1]);
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

   
        request.setAttribute("contentPage", "statistic-list.jsp");
        request.setAttribute("activeMenu", "statistics");
        request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
    }
}
