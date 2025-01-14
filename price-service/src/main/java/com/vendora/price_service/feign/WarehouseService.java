package com.vendora.price_service.feign;

import com.vendora.price_service.entity.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "warehouse-service")
public interface WarehouseService {
    @GetMapping("/product/{productId}")
    ResponseEntity<ProductEntity> getProduct(@PathVariable UUID productId);
}
