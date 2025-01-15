package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.DiscountDTO;
import com.vendora.price_service.DTO.PriceCalculateDTO;
import com.vendora.price_service.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping("/set")
    public ResponseEntity setDiscount(@RequestBody DiscountDTO discountDTO){
        try {
            return ResponseEntity.ok(discountService.setDiscount(discountDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity updatePromo(@PathVariable UUID productId){
        try {
            return ResponseEntity.ok(discountService.updateDiscount(productId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity haveDiscount(@PathVariable UUID productId){
        try {
            return ResponseEntity.ok(discountService.haveDiscount(productId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/calculate/{productId}")
    public ResponseEntity calculateDiscount(@PathVariable UUID productId, @RequestParam BigDecimal price){
        try {
            return ResponseEntity.ok(discountService.calculateDiscount(productId, price));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
