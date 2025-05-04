package com.vendora.catalog_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@Document(indexName = "products") // Elasticsearch
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String category;
    private int purchasesCount = 0;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> characteristics;

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
}

