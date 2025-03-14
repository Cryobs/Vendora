package com.vendora.catalog_service.controller;

import com.vendora.catalog_service.DTO.ProductDTO;
import com.vendora.catalog_service.entity.Product;
import com.vendora.catalog_service.service.ProductService;
import jakarta.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
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
    public ResponseEntity<Product> addPurchasesCount(@PathVariable UUID productId){
        return ResponseEntity.ok(productService.addPurchasesCount(productId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int psize,
            @RequestParam(defaultValue = "purchasesCount_desc") String sort){
        return ResponseEntity.ok(productService.search(q, page - 1, psize, sort, category, minPrice, maxPrice));
    }

}
