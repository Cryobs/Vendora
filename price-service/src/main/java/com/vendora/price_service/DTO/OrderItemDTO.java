package com.vendora.price_service.DTO;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemDTO {
    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    public OrderItemDTO() {
    }

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

}
