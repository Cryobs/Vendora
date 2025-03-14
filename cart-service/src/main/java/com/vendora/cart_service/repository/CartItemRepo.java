package com.vendora.cart_service.repository;

import com.vendora.cart_service.entity.Cart;
import com.vendora.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, UUID> {
    Optional<Cart> findByCartAndProductId(Cart cart, UUID productId);
}