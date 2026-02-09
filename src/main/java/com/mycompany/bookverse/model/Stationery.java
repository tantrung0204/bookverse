/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author TrungNT - CE200064
 */
@Entity
@Table(name = "stationery")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stationery.findAll", query = "SELECT s FROM Stationery s"),
    @NamedQuery(name = "Stationery.findByStationeryId", query = "SELECT s FROM Stationery s WHERE s.stationeryId = :stationeryId"),
    @NamedQuery(name = "Stationery.findByColor", query = "SELECT s FROM Stationery s WHERE s.color = :color"),
    @NamedQuery(name = "Stationery.findByMaterial", query = "SELECT s FROM Stationery s WHERE s.material = :material")})
public class Stationery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "stationery_id")
    private Integer stationeryId;
    @Size(max = 30)
    @Column(name = "color")
    private String color;
    @Size(max = 50)
    @Column(name = "material")
    private String material;
    @JoinColumn(name = "stationery_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Product product;

    public Stationery() {
    }

    public Stationery(Integer stationeryId) {
        this.stationeryId = stationeryId;
    }

    public Integer getStationeryId() {
        return stationeryId;
    }

    public void setStationeryId(Integer stationeryId) {
        this.stationeryId = stationeryId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stationeryId != null ? stationeryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stationery)) {
            return false;
        }
        Stationery other = (Stationery) object;
        if ((this.stationeryId == null && other.stationeryId != null) || (this.stationeryId != null && !this.stationeryId.equals(other.stationeryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.Stationery[ stationeryId=" + stationeryId + " ]";
    }
    
}
