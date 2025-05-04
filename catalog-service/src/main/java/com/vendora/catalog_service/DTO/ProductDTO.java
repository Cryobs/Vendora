package com.vendora.catalog_service.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private String category;
    private Map<String, Object> characteristics;
}
