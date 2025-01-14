package com.vendora.price_service.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

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

    public TaxEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }



}
