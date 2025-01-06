package com.vendora.price_service.controller;


import com.vendora.price_service.DTO.PriceCalculateDTO;
import com.vendora.price_service.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class PriceController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private PriceService priceService;

    @GetMapping("/calculate")
    public ResponseEntity calculate(@RequestBody PriceCalculateDTO request){
        try {
            BigDecimal finalPrice = priceService.calculatePrice(request);
            return ResponseEntity.ok(finalPrice);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
