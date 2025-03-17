package com.vendora.cart_service.controller;

import com.netflix.spectator.impl.StepDouble;
import com.vendora.cart_service.DTO.CartItemDTO;
import com.vendora.cart_service.entity.Cart;
import com.vendora.cart_service.entity.CartItem;
import com.vendora.cart_service.service.CartService;
import jakarta.servlet.UnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public Cart addItem(@RequestBody CartItemDTO cartItemDTO, @AuthenticationPrincipal Jwt jwt) throws UnavailableException {
        return cartService.addItem(cartItemDTO, UUID.fromString(jwt.getSubject()));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok("test");
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public Cart getCart(@AuthenticationPrincipal Jwt jwt){
        return cartService.getCart(UUID.fromString(jwt.getSubject()));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal Jwt jwt){
        cartService.deleteCart(UUID.fromString(jwt.getSubject()));
        return ResponseEntity.ok().body("Cart Deleted");
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<String> getCartProduct(@PathVariable UUID productId, @AuthenticationPrincipal Jwt jwt){
        cartService.deleteItem(productId, UUID.fromString(jwt.getSubject()));
        return ResponseEntity.ok("Product deleted");
    }

    @PutMapping
    @PreAuthorize("hasRole('user')")
    public Cart updateItem(@RequestBody CartItemDTO cartItemDTO, @AuthenticationPrincipal Jwt jwt) throws UnavailableException {
        return cartService.updateItem(cartItemDTO, UUID.fromString(jwt.getSubject()));
    }


}
