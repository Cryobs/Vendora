package com.vendora.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PutMapping("/reserve/{productId}")
    public ResponseEntity<?> reserveProduct(@PathVariable UUID productId, @RequestParam int quantity);
}

