/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookverse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author TrungNT - CE200064
 */
@Entity
@Table(name = "stationery")
@PrimaryKeyJoinColumn(name = "stationery_id", referencedColumnName = "product_id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stationery.findAll", query = "SELECT s FROM Stationery s"),
    @NamedQuery(name = "Stationery.findByColor", query = "SELECT s FROM Stationery s WHERE s.color = :color")})
public class Stationery extends Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 30)
    @Column(name = "color")
    private String color;
    @Size(max = 50)
    @Column(name = "material")
    private String material;

    public Stationery() {
    }

    public Stationery(Integer stationeryId) {
        super(stationeryId);
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getProductId() != null ? getProductId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stationery)) {
            return false;
        }
        Stationery other = (Stationery) object;
        if ((this.getProductId() == null && other.getProductId() != null) || (this.getProductId() != null && !this.getProductId().equals(other.getProductId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.bookverse.model.Stationery[ productId=" + getProductId() + " ]";
    }
    
}
