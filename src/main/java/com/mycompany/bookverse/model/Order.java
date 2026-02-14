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
@Table(name = "[order]")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o"),
    @NamedQuery(name = "Order.findByOrderId", query = "SELECT o FROM Order o WHERE o.orderId = :orderId"),
    @NamedQuery(name = "Order.findByTotalAmount", query = "SELECT o FROM Order o WHERE o.totalAmount = :totalAmount"),
    @NamedQuery(name = "Order.findByOrderStatus", query = "SELECT o FROM Order o WHERE o.orderStatus = :orderStatus"),
    @NamedQuery(name = "Order.findByIsPaid", query = "SELECT o FROM Order o WHERE o.isPaid = :isPaid"),
    @NamedQuery(name = "Order.findByCreatedAt", query = "SELECT o FROM Order o WHERE o.createdAt = :createdAt")})
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "order_id")
    private Integer orderId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Size(max = 50)
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "is_paid")
    private Boolean isPaid;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "orderId")
    private Collection<OrderItem> orderItemCollection;
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    private Customer customerId;
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    @ManyToOne
    private Staff staffId;
    @JoinColumn(name = "voucher_id", referencedColumnName = "voucher_id")
    @ManyToOne
    private Voucher voucherId;

    public Order() {
    }

    public Order(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Collection<OrderItem> getOrderItemCollection() {
        return orderItemCollection;
    }

    public void setOrderItemCollection(Collection<OrderItem> orderItemCollection) {
        this.orderItemCollection = orderItemCollection;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Staff getStaffId() {
        return staffId;
    }

    public void setStaffId(Staff staffId) {
        this.staffId = staffId;
    }

    public Voucher getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Voucher voucherId) {
        this.voucherId = voucherId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.Order[ orderId=" + orderId + " ]";
    }
    
}
