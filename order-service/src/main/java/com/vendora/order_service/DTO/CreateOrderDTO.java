package com.vendora.order_service.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrderDTO {
    private String shippingAddress;
    private String region; // EU, US
}
