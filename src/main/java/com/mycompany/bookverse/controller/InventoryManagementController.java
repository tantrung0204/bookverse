/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.ImportStock;
import com.mycompany.bookverse.model.ImportStockDetail;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.OrderItem;
import com.mycompany.bookverse.service.InventoryService;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author LECOO
 */
@WebServlet(name = "InventoryManagementController", urlPatterns = {"/inventory"})
public class InventoryManagementController extends HttpServlet {

    private InventoryService inventoryServices = new InventoryService();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InventoryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InventoryController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        if (view == null) {
            view = "import-list";
        }
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int pageSize = PaginationConfig.ADMIN_ITEMS_PER_PAGE;
        int maxNode = PaginationConfig.MAX_PAGE_NODES;
        switch (view) {
            case "import-list":
                int totalPages = inventoryServices.getTotalImportPages(pageSize);
                List<ImportStock> imports = inventoryServices.getImportsByPage(page, pageSize);
                if (imports == null || imports.isEmpty()) {
                    request.setAttribute("message", "No imports found");
                } else {
                    request.setAttribute("maxNote", maxNode);
                    request.setAttribute("imports", imports);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("contentPage", "import-list.jsp");
                    request.setAttribute("activeMenu", "inventory");
                }
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "export-list":
                totalPages = inventoryServices.getTotalExportPages(pageSize);
                List<Order> exports = inventoryServices.getExportsByPage(page, pageSize);
                if (exports == null || exports.isEmpty()) {
                    request.setAttribute("message", "No exports found");
                } else {
                    request.setAttribute("maxNote", maxNode);
                    request.setAttribute("exports", exports);
                    request.setAttribute("currentPage", page);
                    request.setAttribute("totalPages", totalPages);
                    request.setAttribute("contentPage", "export-list.jsp");
                    request.setAttribute("activeMenu", "inventory");
                }
                request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                break;
            case "importDetail":
                String idStr = request.getParameter("importId");
                try {
                    int id = Integer.parseInt(idStr);
                    List<ImportStockDetail> importDetails = inventoryServices.getImportDetail(id);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    JSONArray jsonArray = new JSONArray();
                    if (importDetails != null) {
                        for (ImportStockDetail d : importDetails) {
                            JSONObject obj = new JSONObject();
                            obj.put("importDetailId", d.getImportDetailId());
                            obj.put("importedQuantity", d.getImportedQuantity());
                            obj.put("unitPrice", d.getUnitPrice());
                            obj.put("note", d.getNote() != null ? d.getNote() : "");
                            if (d.getProductId() != null) {
                                JSONObject productObj = new JSONObject();
                                productObj.put("name", d.getProductId().getName());
                                obj.put("product", productObj);
                            }
                            jsonArray.put(obj);
                        }
                    }
                    response.getWriter().write(jsonArray.toString());
                } catch (IOException | NumberFormatException e) {
                    response.sendRedirect("inventory");
                }
                break;
            case "exportDetail":
                idStr = request.getParameter("exportId");
                try {
                    int id = Integer.parseInt(idStr);
                    List<OrderItem> exportDetails = inventoryServices.getExportDetail(id);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    JSONArray jsonArray = new JSONArray();
                    if (exportDetails != null) {
                        for (OrderItem o : exportDetails) {
                            JSONObject obj = new JSONObject();
                            obj.put("customerName", o.getOrderId().getCustomerId().getFullName());
                            obj.put("staffName", o.getOrderId().getStaffId().getUsername());
                            obj.put("createdAt", o.getOrderId().getCreatedAt());
                            if (o.getProductId() != null) {
                                JSONObject productObj = new JSONObject();
                                productObj.put("quantity", o.getOrderQuantity());
                                productObj.put("name", o.getProductId().getName());
                                obj.put("product", productObj);
                            }
                            jsonArray.put(obj);
                        }
                    }
                    response.getWriter().write(jsonArray.toString());
                } catch (IOException | NumberFormatException e) {
                    response.sendRedirect("inventory");
                }
                break;
            default:
                response.sendRedirect("inventory");
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
