package com.vendora.price_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CartItemDTO {

    private UUID id;
    private CartDTO cart;
    private UUID productId;
    private int quantity;
}
