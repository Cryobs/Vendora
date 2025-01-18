package com.vendora.order_service.feign;

import com.vendora.order_service.DTO.FinalItemsPriceDTO;
import com.vendora.order_service.DTO.FinalPriceDTO;
import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.utils.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "warehouse-service")
public interface WarehouseService {
    @PutMapping("/reserve/{productId}")
    public ResponseEntity reserveProduct(@PathVariable UUID productId, @RequestParam int quantity);
}

