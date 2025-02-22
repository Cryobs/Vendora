package com.vendora.price_service.entity;

import com.vendora.price_service.DTO.ShippingDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shipping")
public class ShippingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String zone;

    @Column(nullable = false)
    private BigDecimal weightLimit;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String deliveryType;

    public ShippingEntity(String zone, BigDecimal weightLimit, BigDecimal price, String deliveryType) {
        this.zone = zone;
        this.weightLimit = weightLimit;
        this.price = price;
        this.deliveryType = deliveryType;
    }

    public ShippingEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public BigDecimal getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(BigDecimal weightLimit) {
        this.weightLimit = weightLimit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
