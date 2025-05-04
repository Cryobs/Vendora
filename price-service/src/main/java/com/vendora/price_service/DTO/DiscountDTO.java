package com.vendora.price_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DiscountDTO {
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime  endDate;
    private Boolean isActive;
    private UUID productId;

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
