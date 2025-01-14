package com.vendora.price_service.DTO;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class TaxDTO {
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private String tax_type;

    public TaxDTO() {
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Column(nullable = false)
    private BigDecimal rate;
}
