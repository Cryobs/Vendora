package com.vendora.price_service.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    private BigDecimal  basePrice;

    public ProductEntity(String name, String description, BigDecimal base_price) {
        this.name = name;
        this.description = description;
        this.basePrice = base_price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public BigDecimal  getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal  basePrice) {
        this.basePrice = basePrice;
    }

    public ProductEntity() {
    }
}
