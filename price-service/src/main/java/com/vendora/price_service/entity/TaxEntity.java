package com.vendora.price_service.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "taxes")
public class TaxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String region;
    @Column(nullable = false)
    private String taxType;
    @Column(nullable = false)
    private BigDecimal rate;

    public TaxEntity(String region, String tax_type, BigDecimal rate) {
        this.region = region;
        this.taxType = tax_type;
        this.rate = rate;
    }
}
