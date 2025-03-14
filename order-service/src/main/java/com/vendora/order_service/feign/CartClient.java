package com.vendora.order_service.feign;

import com.vendora.order_service.DTO.CartDTO;
import com.vendora.order_service.security.OAuth2FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cart-service", configuration = OAuth2FeignConfig.class)
public interface CartClient {
    @GetMapping
    CartDTO getCart(@RequestHeader("Authorization") String token);
}



