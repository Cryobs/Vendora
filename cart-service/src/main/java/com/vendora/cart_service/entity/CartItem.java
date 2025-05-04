package com.vendora.cart_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    @JsonBackReference
    private Cart cart;

    @NonNull
    @Column(nullable = false)
    private UUID productId;

    @NonNull
    @Column(nullable = false)
    private Integer quantity;

    public CartItem(Cart cart, @NonNull UUID productId, @NonNull Integer quantity) {
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }
}

