package com.vendora.price_service.DTO;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CartDTO {
    private UUID id;
    @Column(nullable = false)
    private UUID userId;

    private List<CartItemDTO> items = new ArrayList<>();
}
