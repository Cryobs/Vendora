package com.vendora.price_service.controller;


import com.vendora.price_service.DTO.PriceCalculateDTO;
import com.vendora.price_service.DTO.ShippingDTO;
import com.vendora.price_service.DTO.TaxDTO;
import com.vendora.price_service.entity.TaxEntity;
import com.vendora.price_service.repository.TaxRepo;
import com.vendora.price_service.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class PriceController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private PriceService priceService;

    @PostMapping("/calculate")
    public ResponseEntity calculate(@RequestBody PriceCalculateDTO request){
        try {
            return ResponseEntity.ok(priceService.calculatePrice(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }


}
