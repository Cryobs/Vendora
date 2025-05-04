package com.vendora.price_service.entity;


import com.vendora.price_service.DTO.DiscountDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "discounts")
public class DiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity product;
    @Column(nullable = false)
    private String discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    private LocalDateTime  startDate;
    private LocalDateTime  endDate;
    private Boolean isActive;

    public DiscountEntity(DiscountDTO discountDTO, ProductEntity product) {
        this.product = product;
        this.discountType = discountDTO.getDiscountType();
        this.discountValue = discountDTO.getDiscountValue();
        this.endDate = discountDTO.getEndDate();
        this.startDate = discountDTO.getStartDate();
    }


    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
