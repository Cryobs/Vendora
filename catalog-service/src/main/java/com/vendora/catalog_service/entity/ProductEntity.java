package com.vendora.catalog_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "products")
@Document(indexName = "products") // Elasticsearch
public class ProductEntity {
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


    @Column(columnDefinition = "jsonb",nullable = true)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> characteristics;

    public Map<String, Object> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, Object> characteristics) {
        this.characteristics = characteristics;
    }


    public int getPurchasesCount() {
        return purchasesCount;
    }

    public void setPurchasesCount(int purchasesCount) {
        this.purchasesCount = purchasesCount;
    }

    public void addPurchasesCount() {
        this.purchasesCount += 1;
    }

    public ProductEntity(UUID userId, String name, String description, BigDecimal basePrice, String category, Map<String, Object> characteristics) {
        this.name = name;
        this.userId = userId;
        this.description = description;
        this.basePrice = basePrice;
        this.category = category;
        this.characteristics = characteristics;
    }

    public ProductEntity() {
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
}

