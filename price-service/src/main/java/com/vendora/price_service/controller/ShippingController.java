package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.ShippingDTO;
import com.vendora.price_service.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping("/create")
    public ResponseEntity createShipping(@RequestBody ShippingDTO shipping){
        try {
            return ResponseEntity.ok(shippingService.createShipping(shipping));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PostMapping("/list")
    public ResponseEntity getShippingList(){
        try {
            return ResponseEntity.ok(shippingService.getShippingList());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
