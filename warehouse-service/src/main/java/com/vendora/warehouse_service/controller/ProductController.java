package com.vendora.warehouse_service.controller;


import com.vendora.warehouse_service.DTO.ProductDTO;
import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.repository.ProductsRepo;
import com.vendora.warehouse_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    @GetMapping("/test")
    public String test(){
        return "TEST";
    }

    @Autowired
    private ProductsRepo productRepo;

    @Autowired
    private ProductService productService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody ProductDTO product){
        try {
            return ResponseEntity.ok(productService.registerProduct(product));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
    @GetMapping("/list")
    public ResponseEntity productListAll(){
        try {
            return ResponseEntity.ok(productService.productListAll());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable UUID productId){
        try {
            ProductEntity product = productRepo.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
            return ResponseEntity.ok(product);
        }catch (Exception e){
            throw e;
        }
    }

}
