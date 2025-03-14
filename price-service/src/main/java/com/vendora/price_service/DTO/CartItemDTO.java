package com.vendora.price_service.DTO;

import jakarta.persistence.Column;

import java.util.UUID;

public class CartItemDTO {

    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private CartDTO cart;
    @Column(nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private int quantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
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

    public CartItemDTO() {
    }
}
