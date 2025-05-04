package com.vendora.catalog_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "products")
@Document(indexName = "products") // Elasticsearch
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    private UUID userId;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal basePrice;
    @NonNull
    private String category;

    private Integer purchasesCount = 0;

    @NonNull
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> characteristics;


    public void addPurchasesCount() {
        this.purchasesCount += 1;
    }
}
