package com.vendora.price_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
