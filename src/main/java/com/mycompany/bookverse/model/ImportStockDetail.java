/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author TrungNT - CE200064
 */
@Entity
@Table(name = "import_stock_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportStockDetail.findAll", query = "SELECT i FROM ImportStockDetail i"),
    @NamedQuery(name = "ImportStockDetail.findByImportDetailId", query = "SELECT i FROM ImportStockDetail i WHERE i.importDetailId = :importDetailId"),
    @NamedQuery(name = "ImportStockDetail.findByNote", query = "SELECT i FROM ImportStockDetail i WHERE i.note = :note"),
    @NamedQuery(name = "ImportStockDetail.findByImportedQuantity", query = "SELECT i FROM ImportStockDetail i WHERE i.importedQuantity = :importedQuantity"),
    @NamedQuery(name = "ImportStockDetail.findByUnitPrice", query = "SELECT i FROM ImportStockDetail i WHERE i.unitPrice = :unitPrice")})
public class ImportStockDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "import_detail_id")
    private Integer importDetailId;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
    @Column(name = "imported_quantity")
    private Integer importedQuantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @JoinColumn(name = "import_id", referencedColumnName = "import_id")
    @ManyToOne
    private ImportStock importId;
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    private Product productId;

    public ImportStockDetail() {
    }

    public ImportStockDetail(Integer importDetailId) {
        this.importDetailId = importDetailId;
    }

    public Integer getImportDetailId() {
        return importDetailId;
    }

    public void setImportDetailId(Integer importDetailId) {
        this.importDetailId = importDetailId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getImportedQuantity() {
        return importedQuantity;
    }

    public void setImportedQuantity(Integer importedQuantity) {
        this.importedQuantity = importedQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ImportStock getImportId() {
        return importId;
    }

    public void setImportId(ImportStock importId) {
        this.importId = importId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (importDetailId != null ? importDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportStockDetail)) {
            return false;
        }
        ImportStockDetail other = (ImportStockDetail) object;
        if ((this.importDetailId == null && other.importDetailId != null) || (this.importDetailId != null && !this.importDetailId.equals(other.importDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.ImportStockDetail[ importDetailId=" + importDetailId + " ]";
    }
    
}
