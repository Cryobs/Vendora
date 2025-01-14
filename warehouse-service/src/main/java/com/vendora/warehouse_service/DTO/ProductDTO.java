package com.vendora.warehouse_service.DTO;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class ProductDTO {
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;

    public ProductDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Column(nullable = false)
    private BigDecimal basePrice;
}
