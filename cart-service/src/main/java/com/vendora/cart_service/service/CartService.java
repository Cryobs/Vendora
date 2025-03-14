package com.vendora.cart_service.service;

import com.vendora.cart_service.DTO.CartItemDTO;
import com.vendora.cart_service.entity.Cart;
import com.vendora.cart_service.entity.CartItem;
import com.vendora.cart_service.repository.CartItemRepo;
import com.vendora.cart_service.repository.CartRepo;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;

    public Cart addItem(CartItemDTO cartItemDTO, UUID userId){
        Cart cart = getCart(userId);
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(cartItemDTO.getProductId()))
                .findFirst();

        if (optionalItem.isPresent()) {
            CartItem existingItem = optionalItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItemDTO.getQuantity());
        } else {
            CartItem newItem = new CartItem(cart, cartItemDTO.getProductId(), cartItemDTO.getQuantity());
            cart.addItem(cartItemRepo.save(newItem));
        }

        return cartRepo.save(cart);
    }

    public Cart getCart(UUID userId){
        return cartRepo.findByUserId(userId)
                .orElseGet(() -> cartRepo.save(new Cart(userId)));
    }

    public void deleteItem(UUID productId, UUID userId) {
        Cart cart = getCart(userId);
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        optionalItem.ifPresent(item -> {
            cart.removeItem(item);
            cartRepo.save(cart);
        });
    }

    public Cart updateItem(CartItemDTO cartItemDTO, UUID userId) {
        Cart cart = getCart(userId);
        Optional<CartItem> optionalItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(cartItemDTO.getProductId()))
                .findFirst();

        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            if (cartItemDTO.getQuantity() > 0) {
                item.setQuantity(cartItemDTO.getQuantity());
            } else {
                cart.removeItem(item);
            }
            return cartRepo.save(cart);
        } else {
            return addItem(cartItemDTO, userId);
        }
    }
}
