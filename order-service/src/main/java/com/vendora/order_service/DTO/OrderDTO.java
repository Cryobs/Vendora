package com.vendora.order_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderDTO {
    private UUID id;
    private String userId;
    private String status; // pending, completed, cancelled
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private String shippingAddress;
    private String region;
    private BigDecimal finalPrice;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private List<OrderItemDTO> items;
}
