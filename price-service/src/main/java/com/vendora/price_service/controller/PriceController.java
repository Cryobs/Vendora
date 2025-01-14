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
    @Autowired
    private TaxRepo taxRepo;

    @PostMapping("/calculate")
    public ResponseEntity calculate(@RequestBody PriceCalculateDTO request){
        try {
            return ResponseEntity.ok(priceService.calculatePrice(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PostMapping("/tax")
    public ResponseEntity setTax(@RequestBody TaxDTO tax){
        try {
            return ResponseEntity.ok(priceService.setTax(tax));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/tax/list")
    public ResponseEntity getTaxList(){
        try {
            return ResponseEntity.ok(taxRepo.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PostMapping("/shipping/create")
    public ResponseEntity createShipping(@RequestBody ShippingDTO shipping){
        try {
            return ResponseEntity.ok(priceService.setShipping(shipping));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PostMapping("/shipping/list")
    public ResponseEntity getShippingList(){
        try {
            return ResponseEntity.ok(priceService.getShippingList());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
