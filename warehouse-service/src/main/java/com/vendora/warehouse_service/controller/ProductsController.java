package com.vendora.warehouse_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {
    @GetMapping("/test")
    public String test(){
        return "TEST";
    }

    @PostMapping
    public ResponseEntity<String> addProduct(){
        try {

            return ResponseEntity.ok("Product added successful");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

}
