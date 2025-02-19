package com.vendora.catalog_service.DTO;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.util.Map;

public class ProductDTO {
    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private String category;
    @Column(nullable = true)
    private Map<String, Object> characteristics;

    public Map<String, Object> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, Object> characteristics) {
        this.characteristics = characteristics;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

}
