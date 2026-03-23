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
    </div>

    <div class="filter-buttons" style="display: flex; gap: 15px; align-items: center; flex-wrap: wrap;">
        <div style="display: flex; gap: 10px;">
            <a href="?filter=week" class="btn-filter ${filter == 'week' ? 'active' : ''}">This Week</a>
            <a href="?filter=month" class="btn-filter ${filter == 'month' ? 'active' : ''}">This Month</a>
            <a href="?filter=year" class="btn-filter ${filter == 'year' ? 'active' : ''}">This Year</a>
        </div>
        <form action="" method="get" style="display: flex; gap: 10px; align-items: center; margin: 0;" onsubmit="return validateDateRange()">
            <input type="hidden" name="filter" value="custom">
            <input type="date" name="startDate" value="${startDate}" class="form-control" style="width: auto; padding: 6px 12px; height: 38px; border-radius: 6px;" required>
            <span>-</span>
            <input type="date" name="endDate" value="${endDate}" class="form-control" style="width: auto; padding: 6px 12px; height: 38px; border-radius: 6px;" required>
            <button type="submit" class="btn-filter ${filter == 'custom' ? 'active' : ''}" style="height: 38px; line-height: 1; margin: 0;">Apply Range</button>
        </form>
    </div>

    <div class="kpi-container">
        <div class="kpi-card">
            <div class="kpi-title">Total Revenue</div>
            <div class="kpi-value"><fmt:formatNumber value="${totalRevenue}" type="number" pattern="#,##0"/> đ</div>
            <div class="kpi-trend ${fn:startsWith(revenueTrend, '+') ? 'up' : (fn:startsWith(revenueTrend, '-') ? 'down' : '')}">
                ${fn:startsWith(revenueTrend, '+') ? '↑' : (fn:startsWith(revenueTrend, '-') ? '↓' : '')} ${revenueTrend} vs prev period
            </div>
        </div>
        <div class="kpi-card">
            <div class="kpi-title">Total Orders</div>
            <div class="kpi-value">${totalOrders}</div>
            <div class="kpi-trend ${fn:startsWith(orderTrend, '+') ? 'up' : (fn:startsWith(orderTrend, '-') ? 'down' : '')}">
                ${fn:startsWith(orderTrend, '+') ? '↑' : (fn:startsWith(orderTrend, '-') ? '↓' : '')} ${orderTrend} vs prev period
            </div>
        </div>
        <div class="kpi-card">
            <div class="kpi-title">Total Customers</div>
            <div class="kpi-value">${totalCustomers}</div>
            <div class="kpi-trend ${fn:startsWith(customerTrend, '+') ? 'up' : (fn:startsWith(customerTrend, '-') ? 'down' : '')}">
                ${fn:startsWith(customerTrend, '+') ? '↑' : (fn:startsWith(customerTrend, '-') ? '↓' : '')} ${customerTrend} vs prev period
            </div>
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

    </div>
</div>

<script>
    function validateDateRange() {
        const start = document.querySelector('input[name="startDate"]').value;
        const end = document.querySelector('input[name="endDate"]').value;
        if (start && end) {
            if (new Date(start) > new Date(end)) {
                alert("Start date must be less than or equal to End date.");
                return false;
            }
        }
        return true;
    }
    
    document.addEventListener("DOMContentLoaded", function () {

        // ============================== Pie Chart ==============================
        var ctxPie = document.getElementById('statusPieChart').getContext('2d');
        new Chart(ctxPie, {
            type: 'pie',
            data: {
                labels: [${statusLabels}],
                datasets: [{
                        data: [${statusData}],
                        backgroundColor: [${statusLabels}].map(label => {
                            if (label === 'Completed') return '#10b981'; // green
                            if (label === 'Pending') return '#f59e0b';   // yellow
                            if (label === 'Cancelled') return '#ef4444'; // red
                            return '#3b82f6';
                        }),
                        borderWidth: 0
                    }]
            },
            options: {
                responsive: true, maintainAspectRatio: false,
                plugins: {legend: {position: 'bottom', labels: {usePointStyle: true, boxWidth: 8}}}
            },
            plugins: [{
                id: 'customDataLabels',
                afterDraw: function(chart) {
                    var ctx = chart.ctx;
                    chart.data.datasets.forEach(function(dataset, i) {
                        var meta = chart.getDatasetMeta(i);
                        if (!meta.hidden) {
                            meta.data.forEach(function(element, index) {
                                if (dataset.data[index] > 0) {
                                    ctx.fillStyle = 'white';
                                    ctx.font = 'bold 15px Arial, sans-serif';
                                    ctx.textAlign = 'center';
                                    ctx.textBaseline = 'middle';
                                    
                                    var dataString = dataset.data[index].toString();
                                    var position = element.tooltipPosition();
                                    
                                    ctx.shadowColor = 'rgba(0,0,0,0.6)';
                                    ctx.shadowBlur = 3;
                                    
                                    ctx.fillText(dataString, position.x, position.y);
                                    
                                    ctx.shadowColor = 'transparent';
                                    ctx.shadowBlur = 0;
                                }
                            });
                        }
                    });
                }
            }]
        });

        // ============================== Line Chart ==============================
        var ctxLine = document.getElementById('revenueLineChart').getContext('2d');
        new Chart(ctxLine, {
            type: 'line',
            data: {
                
                labels: [${revLabels}],
                datasets: [{
                        label: 'Revenue (VND)',
                        data: [${revData}], 
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
                        title: {display: true, text: 'Revenue (VND)', color: '#777', font: {size: 13}} 
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
