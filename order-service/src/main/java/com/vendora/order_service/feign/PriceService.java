package com.vendora.order_service.feign;

import com.vendora.order_service.DTO.FinalItemsPriceDTO;
import com.vendora.order_service.DTO.FinalPriceDTO;
import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.DTO.OrderItemDTO;
import com.vendora.order_service.utils.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "price-service")
public interface PriceService {
    @PostMapping("/calculate")
    ApiResponse<FinalPriceDTO> calculate(@RequestBody OrderDTO order);

    @PostMapping("/calculate/items")
    ApiResponse<List<FinalItemsPriceDTO>> calculateItems(@RequestBody OrderDTO order);
}
