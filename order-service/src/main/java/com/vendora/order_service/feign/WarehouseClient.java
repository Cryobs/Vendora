package com.vendora.order_service.feign;

import com.vendora.order_service.security.OAuth2FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "warehouse-service", configuration = OAuth2FeignConfig.class)
public interface WarehouseClient {
    @PutMapping("/reserve/{productId}")
    public ResponseEntity<?> reserveProduct(@PathVariable UUID productId, @RequestParam int quantity);
}

