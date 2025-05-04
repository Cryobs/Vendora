package com.vendora.price_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TaxDTO {
    private String region;
    private String tax_type;
    private BigDecimal rate;
}
