package com.vendora.cart_service.DTO;

import jakarta.persistence.Column;

import java.util.UUID;

public class CartItemDTO {
    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    public CartItemDTO() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
