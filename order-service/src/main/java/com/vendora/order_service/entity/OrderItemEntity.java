package com.vendora.order_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItemEntity {


    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    @JsonBackReference
    private OrderEntity order;
    @Column(nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private int quantity;

    private BigDecimal totalDiscount;
    private BigDecimal totalTax;

    @Column(nullable = false)
    private BigDecimal finalPrice;



    public OrderItemEntity(OrderEntity order, UUID productId, int quantity, BigDecimal totalDiscount, BigDecimal totalTax, BigDecimal finalPrice) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
        this.totalDiscount = totalDiscount;
        this.totalTax = totalTax;
        this.finalPrice = finalPrice;
    }

}
