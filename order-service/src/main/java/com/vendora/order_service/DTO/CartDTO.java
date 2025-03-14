package com.vendora.order_service.DTO;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartDTO {
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private UUID userId;

    private List<CartItemDTO> items = new ArrayList<CartItemDTO>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public CartDTO() {
    }
}
