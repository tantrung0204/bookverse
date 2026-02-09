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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author TrungNT - CE200064
 */
@Entity
@Table(name = "order_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderItem.findAll", query = "SELECT o FROM OrderItem o"),
    @NamedQuery(name = "OrderItem.findByOrderItemId", query = "SELECT o FROM OrderItem o WHERE o.orderItemId = :orderItemId"),
    @NamedQuery(name = "OrderItem.findByOrderQuantity", query = "SELECT o FROM OrderItem o WHERE o.orderQuantity = :orderQuantity")})
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "order_item_id")
    private Integer orderItemId;
    @Column(name = "order_quantity")
    private Integer orderQuantity;
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @ManyToOne
    private Order orderId;
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    private Product productId;

    public OrderItem() {
    }

    public OrderItem(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
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
        hash += (orderItemId != null ? orderItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderItem)) {
            return false;
        }
        OrderItem other = (OrderItem) object;
        if ((this.orderItemId == null && other.orderItemId != null) || (this.orderItemId != null && !this.orderItemId.equals(other.orderItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.OrderItem[ orderItemId=" + orderItemId + " ]";
    }
    
}
