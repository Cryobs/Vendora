package com.vendora.warehouse_service.feign;

import com.vendora.warehouse_service.security.OAuth2FeignConfig;
import com.vendora.warehouse_service.entity.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalog-service", configuration = OAuth2FeignConfig.class)
public interface CatalogClient {
    @GetMapping("/{productId}")
    ResponseEntity<ProductEntity> getProduct(@PathVariable UUID productId);
}
