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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
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
@Table(name = "voucher")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Voucher.findAll", query = "SELECT v FROM Voucher v"),
        @NamedQuery(name = "Voucher.findByVoucherId", query = "SELECT v FROM Voucher v WHERE v.voucherId = :voucherId"),
        @NamedQuery(name = "Voucher.findByVoucherCode", query = "SELECT v FROM Voucher v WHERE v.voucherCode = :voucherCode"),
        @NamedQuery(name = "Voucher.findByDiscountType", query = "SELECT v FROM Voucher v WHERE v.discountType = :discountType"),
        @NamedQuery(name = "Voucher.findByStatus", query = "SELECT v FROM Voucher v WHERE v.status = :status")
})
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "voucher_id")
    private Integer voucherId;

    @Size(max = 100)
    @Column(name = "voucher_name")
    private String voucherName;

    @Size(max = 50)
    @Column(name = "voucher_code")
    private String voucherCode;

    /**
     * Giá trị giảm: - Nếu discount_type = 1 → % giảm (vd: 10 = 10%) - Nếu
     * discount_type = 2 → số tiền giảm cố định
     */
    @Column(name = "discount_value")
    private BigDecimal discountValue;

    /**
     * 1 = Giảm theo phần trăm 2 = Giảm giá cố định
     */
    @Column(name = "discount_type")
    private Integer discountType;

    /**
     * Giá trị đơn hàng tối thiểu để áp dụng voucher
     */
    @Column(name = "min_order_value")
    private BigDecimal minOrderValue;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "status")
    private Integer status;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @OneToMany(mappedBy = "voucherId")
    private Collection<Order> order1Collection;

    @Transient
    private long usedCount;

    public Voucher() {
    }

    public Voucher(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @XmlTransient
    public Collection<Order> getOrderCollection() {
        return order1Collection;
    }

    public void setOrderCollection(Collection<Order> order1Collection) {
        this.order1Collection = order1Collection;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (voucherId != null ? voucherId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Voucher)) {
            return false;
        }
        Voucher other = (Voucher) object;
        if ((this.voucherId == null && other.voucherId != null)
                || (this.voucherId != null && !this.voucherId.equals(other.voucherId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Voucher[ voucherId=" + voucherId + " ]";
    }
}
