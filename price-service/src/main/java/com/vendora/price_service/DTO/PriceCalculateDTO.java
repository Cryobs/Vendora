package com.vendora.price_service.DTO;

import com.vendora.price_service.entity.ShippingEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigInteger;
import java.util.UUID;

public class PriceCalculateDTO {
    private UUID productId;
    private String promoCode;

    private String region;

    private UUID shippingId;

    private int quantity;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public UUID getShippingId() {
        return shippingId;
    }

    public void setShippingId(UUID shippingId) {
        this.shippingId = shippingId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
