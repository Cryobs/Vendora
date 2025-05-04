package com.vendora.order_service.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CartDTO {
    private UUID id;
    private UUID userId;
    private List<CartItemDTO> items = new ArrayList<>();
}
