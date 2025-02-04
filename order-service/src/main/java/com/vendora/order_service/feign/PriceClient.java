package com.vendora.order_service.feign;

import com.vendora.order_service.DTO.FinalItemsPriceDTO;
import com.vendora.order_service.DTO.FinalPriceDTO;
import com.vendora.order_service.DTO.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "price-service")
public interface PriceClient {
    @PostMapping("/calculate")
    FinalPriceDTO calculate(@RequestBody OrderDTO order);

    @PostMapping("/calculate/items")
    List<FinalItemsPriceDTO> calculateItems(@RequestBody OrderDTO order);

    @PutMapping("/promocode/use/{promo}")
    Boolean usePromo(@PathVariable String promo);
}

