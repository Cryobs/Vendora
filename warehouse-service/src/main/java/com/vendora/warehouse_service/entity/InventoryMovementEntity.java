package com.vendora.warehouse_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class InventoryMovementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String changeType; // addition, removal, reservation

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}

