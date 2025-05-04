package com.vendora.order_service.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class FinalPriceDTO {
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalTax;
    private BigDecimal totalShipping;
    private BigDecimal finalPrice;
}
