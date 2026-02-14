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
@Table(name = "customer_notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerNotification.findAll", query = "SELECT c FROM CustomerNotification c"),
    @NamedQuery(name = "CustomerNotification.findByCustomerNotificationId", query = "SELECT c FROM CustomerNotification c WHERE c.customerNotificationId = :customerNotificationId"),
    @NamedQuery(name = "CustomerNotification.findByIsRead", query = "SELECT c FROM CustomerNotification c WHERE c.isRead = :isRead")})
public class CustomerNotification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "customer_notification_id")
    private Integer customerNotificationId;
    @Column(name = "is_read")
    private Boolean isRead;
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    private Customer customerId;
    @JoinColumn(name = "notification_id", referencedColumnName = "notification_id")
    @ManyToOne
    private Notification notificationId;

    public CustomerNotification() {
    }

    public CustomerNotification(Integer customerNotificationId) {
        this.customerNotificationId = customerNotificationId;
    }

    public Integer getCustomerNotificationId() {
        return customerNotificationId;
    }

    public void setCustomerNotificationId(Integer customerNotificationId) {
        this.customerNotificationId = customerNotificationId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Notification getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Notification notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerNotificationId != null ? customerNotificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerNotification)) {
            return false;
        }
        CustomerNotification other = (CustomerNotification) object;
        if ((this.customerNotificationId == null && other.customerNotificationId != null) || (this.customerNotificationId != null && !this.customerNotificationId.equals(other.customerNotificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.CustomerNotification[ customerNotificationId=" + customerNotificationId + " ]";
    }
    
}
