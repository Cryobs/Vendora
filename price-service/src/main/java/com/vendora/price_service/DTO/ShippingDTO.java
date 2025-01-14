package com.vendora.price_service.DTO;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class ShippingDTO {
    @Column(nullable = false)
    private String zone;


    @Column(nullable = false)
    private BigDecimal weightLimit;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String deliveryType;

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
