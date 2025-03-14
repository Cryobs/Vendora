package com.vendora.order_service.feign;

import com.vendora.order_service.DTO.FinalItemsPriceDTO;
import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.security.OAuth2FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "price-service", configuration = OAuth2FeignConfig.class)
public interface PriceClient {
    @PostMapping("/calculate/order")
    OrderDTO calculateOrder(@RequestBody OrderDTO order);

    @PutMapping("/promocode/use/{promo}")
    Boolean usePromo(@PathVariable String promo);
}

