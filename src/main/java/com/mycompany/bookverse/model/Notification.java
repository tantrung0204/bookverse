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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author TrungNT - CE200064
 */
@Entity
@Table(name = "notification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notification.findAll", query = "SELECT n FROM Notification n"),
    @NamedQuery(name = "Notification.findByNotificationId", query = "SELECT n FROM Notification n WHERE n.notificationId = :notificationId"),
    @NamedQuery(name = "Notification.findByTitle", query = "SELECT n FROM Notification n WHERE n.title = :title"),
    @NamedQuery(name = "Notification.findByContentText", query = "SELECT n FROM Notification n WHERE n.contentText = :contentText"),
    @NamedQuery(name = "Notification.findByImageUrl", query = "SELECT n FROM Notification n WHERE n.imageUrl = :imageUrl"),
    @NamedQuery(name = "Notification.findByCreatedAt", query = "SELECT n FROM Notification n WHERE n.createdAt = :createdAt")})
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notification_id")
    private Integer notificationId;
    @Size(max = 150)
    @Column(name = "title")
    private String title;
    @Size(max = 500)
    @Column(name = "content_text")
    private String contentText;
    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "notificationId")
    private Collection<CustomerNotification> customerNotificationCollection;

    public Notification() {
    }

    public Notification(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Collection<CustomerNotification> getCustomerNotificationCollection() {
        return customerNotificationCollection;
    }

    public void setCustomerNotificationCollection(Collection<CustomerNotification> customerNotificationCollection) {
        this.customerNotificationCollection = customerNotificationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificationId != null ? notificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notification)) {
            return false;
        }
        Notification other = (Notification) object;
        if ((this.notificationId == null && other.notificationId != null) || (this.notificationId != null && !this.notificationId.equals(other.notificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.Notification[ notificationId=" + notificationId + " ]";
    }
    
}
