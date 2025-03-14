package com.vendora.price_service.controller;


import com.vendora.price_service.DTO.OrderDTO;
import com.vendora.price_service.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PriceController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private PriceService priceService;

    @PostMapping("/calculate/order")
//    @PreAuthorize("hasRole('user')")
    public ResponseEntity<OrderDTO> calculateOrderPrice(@RequestBody OrderDTO request){
        OrderDTO calculate = priceService.calculateOrder(request);
        return ResponseEntity.ok(calculate);
    }


}
