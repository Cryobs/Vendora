package com.vendora.price_service.controller;


import com.vendora.price_service.DTO.FinalItemsPriceDTO;
import com.vendora.price_service.DTO.FinalPriceDTO;
import com.vendora.price_service.DTO.OrderDTO;
import com.vendora.price_service.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class PriceController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @Autowired
    private PriceService priceService;

    @PostMapping("/calculate/items")
    public ResponseEntity<List<FinalItemsPriceDTO>> calculateItemsPrice(@RequestBody OrderDTO request){
        return ResponseEntity.ok(priceService.calculateItemsPrice(request));
    }

    @PostMapping("/calculate")
    public ResponseEntity<FinalPriceDTO> calculatePrice(@RequestBody OrderDTO request){
        FinalPriceDTO calculate = priceService.calculatePrice(request);
        return ResponseEntity.ok(calculate);
    }


}
