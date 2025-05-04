package com.vendora.order_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ShippingDTO {
    private UUID id;
    private String zone;
    private BigDecimal weightLimit;
    private BigDecimal price;
    private String deliveryType;
}
