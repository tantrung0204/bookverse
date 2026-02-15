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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author TrungNT - CE200064
 */
@Entity
@Table(name = "import_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportStock.findAll", query = "SELECT i FROM ImportStock i"),
    @NamedQuery(name = "ImportStock.findByImportId", query = "SELECT i FROM ImportStock i WHERE i.importId = :importId"),
    @NamedQuery(name = "ImportStock.findByTotalCost", query = "SELECT i FROM ImportStock i WHERE i.totalCost = :totalCost"),
    @NamedQuery(name = "ImportStock.findByCreatedAt", query = "SELECT i FROM ImportStock i WHERE i.createdAt = :createdAt")})
public class ImportStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "import_id")
    private Integer importId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_cost")
    private BigDecimal totalCost;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "importId")
    private Collection<ImportStockDetail> importStockDetailCollection;
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ManyToOne
    private Staff staffId;
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    @ManyToOne
    private Supplier supplierId;

    public ImportStock() {
    }

    public ImportStock(Integer importId) {
        this.importId = importId;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Collection<ImportStockDetail> getImportStockDetailCollection() {
        return importStockDetailCollection;
    }

    public void setImportStockDetailCollection(Collection<ImportStockDetail> importStockDetailCollection) {
        this.importStockDetailCollection = importStockDetailCollection;
    }

    public Staff getStaffId() {
        return staffId;
    }

    public void setStaffId(Staff staffId) {
        this.staffId = staffId;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (importId != null ? importId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportStock)) {
            return false;
        }
        ImportStock other = (ImportStock) object;
        if ((this.importId == null && other.importId != null) || (this.importId != null && !this.importId.equals(other.importId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.ImportStock[ importId=" + importId + " ]";
    }
    
}
