package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.ShippingDTO;
import com.vendora.price_service.entity.ShippingEntity;
import com.vendora.price_service.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ShippingEntity> createShipping(@RequestBody ShippingDTO shipping){
        return ResponseEntity.ok(shippingService.createShipping(shipping));
    }

    @PostMapping("/list")
    public ResponseEntity<Iterable<ShippingEntity>> getShippingList(){
        return ResponseEntity.ok(shippingService.getShippingList());
    }
}
