<%-- 
    Document   : statistic-list
    Created on : Mar 20, 2026, 11:21:57 PM
    Author     : huyqu
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/statistic-list.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="container-fluid">
    <div class="page-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <div>
            <p class="title">Statistics Dashboard</p>
            <p class="subtitle">Track your business performance</p>
        </div>
        <button class="btn-filter" style="margin: 0;">
            <i class="bi bi-download"></i> Export
        </button>
    </div>

    <div class="filter-buttons">
        <button class="btn-filter active">Today</button>
        <button class="btn-filter">This Week</button>
        <button class="btn-filter">This Month</button>
    </div>

    <div class="kpi-container">
        <div class="kpi-card">
            <div class="kpi-title">Total Revenue</div>
            <div class="kpi-value"><fmt:formatNumber value="${totalRevenue}" type="number" pattern="#,##0"/> đ</div>
            <div class="kpi-trend up">↑ +12% vs last month</div>
        </div>
        <div class="kpi-card">
            <div class="kpi-title">Total Orders</div>
            <div class="kpi-value">${totalOrders}</div>
            <div class="kpi-trend down">↓ -5% vs last month</div>
        </div>
        <div class="kpi-card">
            <div class="kpi-title">Total Customers</div>
            <div class="kpi-value">${totalCustomers}</div>
            <div class="kpi-trend up">↑ +18% vs last month</div>
        </div>
        <div class="kpi-card">
            <div class="kpi-title">Top Product</div>
            <div class="kpi-value" style="font-size: 20px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${topProductName}">
                ${topProductName}
            </div>
            <div class="kpi-trend" style="color: #777; font-weight: normal;">${topProductSold} sold</div>
        </div>
    </div>

    <div class="chart-container">
        <div class="chart-card" style="flex: 2;">
            <h4>Revenue Trend</h4>
            <div style="height: 300px;">
                <canvas id="revenueLineChart"></canvas>
            </div>
        </div>
        <div class="chart-card" style="flex: 1;">
            <h4>Orders by Status</h4>
            <div style="height: 300px;">
                <canvas id="statusPieChart"></canvas>
            </div>
        </div>
    </div>

    <div class="content-card">
        <h4 style="font-size: 16px; font-weight: bold; margin-bottom: 15px; color: #333;">Recent Orders</h4>

        <c:choose>
            <c:when test="${not empty recentOrders}">
                <table class="custom-table">
                    <thead>
                        <tr>
                            <th width="15%">Order ID</th>
                            <th width="25%">Customer</th>
                            <th width="20%">Date</th>
                            <th width="20%">Total Amount</th>
                            <th width="20%">Status</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="ro" items="${recentOrders}">
                            <tr>
                                <td><strong>#O00${ro.orderId}</strong></td>
                                <td>${not empty ro.receiverName ? ro.receiverName : ro.customerId.fullName}</td>
                                <td><fmt:formatDate value="${ro.createdAt}" pattern="yyyy-MM-dd"/></td>
                                <td><fmt:formatNumber value="${ro.totalAmount}" type="number" pattern="#,##0"/> đ</td>
                                <td>
                                    <span class="badge-status ${ro.orderStatus == 'Completed' ? 'badge-active' : (ro.orderStatus == 'Pending' ? 'badge-warning' : 'badge-inactive')}">
                                        ${ro.orderStatus}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>

            <c:otherwise>
                <div class="alert alert-warning" style="margin-top: 20px;">No recent orders found!</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    
    
        const filterBtns = document.querySelectorAll('.btn-filter');
        filterBtns.forEach(btn => {
            btn.addEventListener('click', function() {
        
                filterBtns.forEach(b => b.classList.remove('active'));
              
                this.classList.add('active');
                
                
            });
        });
    
    document.addEventListener("DOMContentLoaded", function () {

        // ============================== Pie Chart ==============================
        var ctxPie = document.getElementById('statusPieChart').getContext('2d');
        new Chart(ctxPie, {
            type: 'pie',
            data: {
                labels: [${statusLabels}],
                datasets: [{
                        data: [${statusData}],
                        backgroundColor: ['#10b981', '#f59e0b', '#ef4444', '#3b82f6'],
                        borderWidth: 0
                    }]
            },
            options: {
                responsive: true, maintainAspectRatio: false,
                plugins: {legend: {position: 'bottom', labels: {usePointStyle: true, boxWidth: 8}}}
            }
        });

        // ============================== Line Chart ==============================
        var ctxLine = document.getElementById('revenueLineChart').getContext('2d');
        new Chart(ctxLine, {
            type: 'line',
            data: {
                
                labels: ['1', '5', '10', '15', '20', '25'],
                datasets: [{
                        label: 'Revenue ($)',
                        data: [200, 1500, 2200, 1800, 2500, 3000], 
                        borderColor: '#2563eb',
                        backgroundColor: 'rgba(37, 99, 235, 0.05)',
                        borderWidth: 2,
                        pointBackgroundColor: '#2563eb',
                        pointBorderColor: '#ffffff',
                        pointBorderWidth: 2,
                        pointRadius: 5,
                        fill: true,
                        tension: 0 
                    }]
            },
            options: {
                responsive: true, maintainAspectRatio: false,
                plugins: {legend: {display: false}},
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: {borderDash: [5, 5], color: '#f0f0f0'},
                        border: {display: false},
                        title: {display: true, text: 'Revenue ($)', color: '#777', font: {size: 13}} 
                    },
                    x: {
                        grid: {display: true, color: '#f9f9f9'},
                        border: {display: false},
                        title: {display: true, text: 'Day of Month', color: '#777', font: {size: 13}} 
                    }
                }
            }
        });

    });
</script>
