package com.vendora.order_service.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private UUID id;
    private String orderId;
    private UUID productId;
    private int quantity;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal finalPrice;
}

