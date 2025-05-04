package com.vendora.order_service.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@NoArgsConstructor
public class FinalItemsPriceDTO {
    private UUID productId;
    private int quantity;
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
}
