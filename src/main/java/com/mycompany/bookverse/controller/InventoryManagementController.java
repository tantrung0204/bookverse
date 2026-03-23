/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.bookverse.controller;

import com.mycompany.bookverse.model.ImportStock;
import com.mycompany.bookverse.model.ImportStockDetail;
import com.mycompany.bookverse.model.Order;
import com.mycompany.bookverse.model.OrderItem;
import com.mycompany.bookverse.model.Product;
import com.mycompany.bookverse.model.Staff;
import com.mycompany.bookverse.model.Supplier;
import com.mycompany.bookverse.service.InventoryService;
import com.mycompany.bookverse.utils.PaginationConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author LECOO
 */
@WebServlet(name = "InventoryManagementController", urlPatterns = {"/dashboard/inventory"})
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
            if (view == null || view.isEmpty()) {
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
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            //chuyển đổi theo format yyyy-MM-dd vì type date chỉ có format này
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date from = null;
            Date to = null;
            try {
                if (fromDate != null && !fromDate.isEmpty()) {
                    from = sdf.parse(fromDate);
                }
                if (toDate != null && !toDate.isEmpty()) {
                    to = sdf.parse(toDate);
                    //chuyển to thành cuối ngày để có thể filter trong cùng một ngày
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(to);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);

                    to = cal.getTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (view) {
                case "import-list":
                    String success = (String) request.getSession().getAttribute("success");
                    String createError = (String) request.getSession().getAttribute("createError");

                    if (success != null) {
                        request.setAttribute("success", success);
                        request.getSession().removeAttribute("success");
                    } else if (createError != null) {
                        request.setAttribute("createError", createError);
                        request.getSession().removeAttribute("createError");
                        request.setAttribute("openCreatePopup", true);

                    }
                    int totalPages = inventoryServices.getTotalImportPages(pageSize, from, to);
                    List<ImportStock> imports = inventoryServices.getImportsByPage(page, pageSize, from, to);
                    if (imports == null || imports.isEmpty()) {
                        request.setAttribute("message", "No imports found");
                    } else {

                        request.setAttribute("currentTab", view);
                        request.setAttribute("maxNote", maxNode);
                        request.setAttribute("imports", imports);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages", totalPages);
                    }
                    request.setAttribute("fromDate", fromDate);
                    request.setAttribute("toDate", toDate);
                    request.setAttribute("contentPage", "import-list.jsp");
                    request.setAttribute("activeMenu", "inventory");
                    request.getRequestDispatcher("/views/dashboard/dashboard.jsp").forward(request, response);
                    break;
                case "export-list":
                    totalPages = inventoryServices.getTotalExportPages(pageSize, from, to);
                    List<Order> exports = inventoryServices.getExportsByPage(page, pageSize, from, to);
                    if (exports == null || exports.isEmpty()) {
                        request.setAttribute("message", "No exports found");
                    } else {
                        request.setAttribute("currentTab", view);
                        request.setAttribute("maxNote", maxNode);
                        request.setAttribute("exports", exports);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("totalPages", totalPages);

                    }
                    request.setAttribute("fromDate", fromDate);
                    request.setAttribute("toDate", toDate);
                    request.setAttribute("contentPage", "export-list.jsp");
                    request.setAttribute("activeMenu", "inventory");
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
                case "addProductList":
                    List<Product> productList = inventoryServices.getAllProduct();
                    List<Supplier> SupplierList = inventoryServices.getAllSupplier();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    JSONArray supplierArray = new JSONArray();
                    JSONArray productArray = new JSONArray();
                    for (Supplier s : SupplierList) {
                        JSONObject sup = new JSONObject();
                        sup.put("supplierId", s.getSupplierId());
                        sup.put("supplierName", s.getSupplierName());
                        supplierArray.put(sup);
                    }
                    for (Product p : productList) {
                        if (p.getStatus() != null && p.getStatus() == 1) {
                            JSONObject pro = new JSONObject();
                            pro.put("productId", p.getProductId());
                            pro.put("productName", p.getName());
                            productArray.put(pro);
                        }
                    }
                    JSONObject result = new JSONObject();
                    result.put("suppliers", supplierArray);
                    result.put("products", productArray);

                    response.getWriter().write(result.toString());
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

            try {

                Staff staff = (Staff) request.getSession().getAttribute("user");

                int supplierId;
                String supplierIdStr = request.getParameter("supplierId");
                String importDateStr = request.getParameter("importDate");
                
                String[] productIdsString = request.getParameterValues("productIds");// lấy list product id muốn add
                BigDecimal totalCost = BigDecimal.ZERO;
                Supplier supplier = new Supplier();
                ArrayList<String> quantitys = new ArrayList<>(), UnitPrices = new ArrayList<>(), notes = new ArrayList<>();
                if (productIdsString != null && !(productIdsString.length == 0)) {
                    for (String id : productIdsString) {
                        quantitys.add(request.getParameter("quantity_" + id));
                        UnitPrices.add(request.getParameter("unit_price_" + id));
                        notes.add(request.getParameter("note_" + id));
                    }
                }
                
                Date importDate = new Date();
                try {
                    if (importDateStr != null && !importDateStr.isEmpty()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        importDate = sdf.parse(importDateStr);
                        
                        // Validation: importDate not to exceed current date
                        Calendar currentCal = Calendar.getInstance();                     
                        currentCal.set(Calendar.HOUR_OF_DAY, 23);
                        currentCal.set(Calendar.MINUTE, 59);
                        currentCal.set(Calendar.SECOND, 59);
                        if (importDate.after(currentCal.getTime())) {
                            throw new Exception("Import date cannot exceed current date");
                        }
                    }
                } catch (Exception e) {
                    request.getSession().setAttribute("createError", e.getMessage());
                    response.sendRedirect("inventory");
                    return;
                }

                try {
                    inventoryServices.checkValid(supplierIdStr, productIdsString, quantitys, UnitPrices, notes);
                    supplierId = Integer.parseInt(supplierIdStr);// lấy supplier id
                    supplier = inventoryServices.getSupplier(supplierId);

                    for (String id : productIdsString) {//Tính total cost              
                        int quantity = Integer.parseInt(request.getParameter("quantity_" + id));
                        BigDecimal unitPrice = new BigDecimal(request.getParameter("unit_price_" + id));
                        totalCost = totalCost.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));
                    }
                } catch (Exception e) {
                    request.getSession().setAttribute("createError", e.getMessage());
                    response.sendRedirect("inventory");
                    return;
                }
                ImportStock importStock = new ImportStock();
                importStock.setStaffId(staff);
                importStock.setSupplierId(supplier);
                importStock.setTotalCost(totalCost);
                importStock.setCreatedAt(importDate);
                int importId = inventoryServices.insertImportStock(importStock);

                //tạo import_stock_detail
                if (importId == -1) {
                    request.getSession().setAttribute("createError", "Import Stock error");
                    response.sendRedirect("inventory");
                    return;
                } else {
                    importStock.setImportId(importId);
                    boolean allSuccess = true;
                    for (String id : productIdsString) {

                        int productId = Integer.parseInt(id);

                        Product product = inventoryServices.getProduct(productId);
                        Integer quantity = Integer.valueOf(request.getParameter("quantity_" + id));
                        BigDecimal unitPrice = new BigDecimal(request.getParameter("unit_price_" + id));
                        String note = request.getParameter("note_" + id);

                        ImportStockDetail importStockDetail = new ImportStockDetail();

                        importStockDetail.setImportId(importStock);
                        importStockDetail.setProductId(product);
                        importStockDetail.setNote(note);
                        importStockDetail.setImportedQuantity(quantity);
                        importStockDetail.setUnitPrice(unitPrice);

                        if (!inventoryServices.insertImportStockDetail(importStockDetail)) {
                            allSuccess = false;
                        }
                    }
                    if (allSuccess) {
                        request.getSession().setAttribute("success", "Add successfully");
                    }

                    response.sendRedirect("inventory");
                }
            } catch (IOException | NumberFormatException e) {
                response.sendRedirect("inventory");
            }
   
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
