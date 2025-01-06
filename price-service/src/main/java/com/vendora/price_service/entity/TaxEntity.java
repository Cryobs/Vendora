package com.vendora.price_service.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "taxes")
public class TaxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal tax; // percents

    public TaxEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
