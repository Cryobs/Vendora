package com.vendora.warehouse_service.controller;


import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.repository.ProductRepo;
import com.vendora.warehouse_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductsController {
    @GetMapping("/test")
    public String test(){
        return "TEST";
    }

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody ProductEntity product){
        try {
            return ResponseEntity.ok(productService.registerProduct(product));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

}
