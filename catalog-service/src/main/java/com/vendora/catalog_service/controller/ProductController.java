package com.vendora.catalog_service.controller;

import com.vendora.catalog_service.DTO.ProductDTO;
import com.vendora.catalog_service.entity.Product;
import com.vendora.catalog_service.service.ProductService;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('vendor')")
    public ResponseEntity<Product> registerProduct(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(productService.registerProduct(productDTO, jwt));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('vendor')")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId, @AuthenticationPrincipal Jwt jwt) throws IllegalAccessException {
        return ResponseEntity.ok(productService.deleteProduct(productId, jwt));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> search(@RequestParam String q, @RequestParam int page, @RequestParam int psize){
        return ResponseEntity.ok(productService.search(q, page, psize));
    }

}
