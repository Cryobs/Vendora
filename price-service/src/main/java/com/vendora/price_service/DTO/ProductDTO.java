package com.vendora.price_service.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    private UUID userId;
    private String name;
    @Column(nullable = true)
    private String description;
    private BigDecimal basePrice;
    private String category;
    private int purchasesCount = 0;

    private Map<String, Object> characteristics;

    public ProductDTO() {
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPurchasesCount() {
        return purchasesCount;
    }

    public void setPurchasesCount(int purchasesCount) {
        this.purchasesCount = purchasesCount;
    }

    public Map<String, Object> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, Object> characteristics) {
        this.characteristics = characteristics;
    }
}
