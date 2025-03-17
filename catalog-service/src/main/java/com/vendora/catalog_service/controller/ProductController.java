package com.vendora.catalog_service.controller;

import com.vendora.catalog_service.DTO.ProductDTO;
import com.vendora.catalog_service.entity.ProductEntity;
import com.vendora.catalog_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('vendor')")
    public ResponseEntity<ProductEntity> registerProduct(@RequestBody ProductDTO productDTO, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(productService.registerProduct(productDTO, jwt));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('vendor')")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId, @AuthenticationPrincipal Jwt jwt) throws IllegalAccessException {
        return ResponseEntity.ok(productService.deleteProduct(productId, jwt));
    }

    @DeleteMapping("/admin/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteAllProducts()  {
        return ResponseEntity.ok(productService.deleteAllProducts());
    }

    @PutMapping("/admin/{productId}/purchasesCount")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ProductEntity> addPurchasesCount(@PathVariable UUID productId){
        return ResponseEntity.ok(productService.addPurchasesCount(productId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable UUID productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductEntity>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable,
            @RequestParam(defaultValue = "purchasesCount_desc") String sort){
        return ResponseEntity.ok(productService.search(q, pageable, sort, category, minPrice, maxPrice));
    }

}
