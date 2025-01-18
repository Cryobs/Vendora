package com.vendora.price_service.controller;


import com.vendora.price_service.DTO.FinalItemsPriceDTO;
import com.vendora.price_service.DTO.FinalPriceDTO;
import com.vendora.price_service.DTO.OrderDTO;
import com.vendora.price_service.service.PriceService;
import com.vendora.price_service.utils.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<FinalItemsPriceDTO>>> calculateItemsPrice(@RequestBody OrderDTO request){
        try {
            return ResponseEntity.ok(new ApiResponse<>(priceService.calculateItemsPrice(request), null));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<FinalPriceDTO>> calculatePrice(@RequestBody OrderDTO request){
        try {
            FinalPriceDTO calculate = priceService.calculatePrice(request);
            System.out.println(calculate.getFinalPrice());
            return ResponseEntity.ok(new ApiResponse<>(calculate, null));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(null, "Error: " + e));
        }
    }


}
