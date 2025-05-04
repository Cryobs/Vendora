package com.vendora.price_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductDTO {
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String category;
    private int purchasesCount = 0;
    private Map<String, Object> characteristics;
}
