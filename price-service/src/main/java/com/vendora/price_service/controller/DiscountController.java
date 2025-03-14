package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.DiscountDTO;
import com.vendora.price_service.entity.DiscountEntity;
import com.vendora.price_service.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DiscountEntity> setDiscount(@RequestBody DiscountDTO discountDTO){
        return ResponseEntity.ok( discountService.setDiscount(discountDTO));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DiscountEntity> updatePromo(@PathVariable UUID productId){
        return ResponseEntity.ok(discountService.updateDiscount(productId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<DiscountEntity> haveDiscount(@PathVariable UUID productId){
        return ResponseEntity.ok(discountService.haveDiscount(productId));
    }

    @GetMapping("/calculate/{productId}")
    public ResponseEntity<BigDecimal> calculateDiscount(@PathVariable UUID productId, @RequestParam BigDecimal price){
        return ResponseEntity.ok(discountService.calculateDiscount(productId, price));
    }
}
