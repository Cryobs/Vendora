package com.vendora.catalog_service.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import java.util.Map;


@org.springframework.data.mongodb.core.mapping.Document(collection = "products") // MongoDB
@org.springframework.data.elasticsearch.annotations.Document(indexName = "products") // Elasticsearch
public class Product {
    @Id
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(nullable = false)
    private BigDecimal basePrice;
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


    public Product(String userId, String name, String description, BigDecimal basePrice, String category, Map<String, Object> characteristics) {
        this.name = name;
        this.userId = userId;
        this.description = description;
        this.basePrice = basePrice;
        this.category = category;
        this.characteristics = characteristics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

