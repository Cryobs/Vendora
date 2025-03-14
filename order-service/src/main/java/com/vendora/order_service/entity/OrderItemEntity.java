package com.vendora.order_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vendora.order_service.DTO.CartDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

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

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }


    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public OrderItemEntity() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
