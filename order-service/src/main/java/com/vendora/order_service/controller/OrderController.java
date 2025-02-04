package com.vendora.order_service.controller;

import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.exception.OrderUndefinedException;
import com.vendora.order_service.repository.OrderRepo;
import com.vendora.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class OrderController {

    @GetMapping("/test")
    public String test(){
        return "TEST";
    }


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @PostMapping("/create")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderDTO order){
        OrderEntity createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable UUID orderId) throws OrderUndefinedException {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Iterable<OrderEntity>> getListOrder() {
        return ResponseEntity.ok(orderService.getListOrder());
    }

    @GetMapping("/list/{status}")
    public ResponseEntity<Iterable<OrderEntity>> getListOrderByStatus(@PathVariable String status) throws OrderUndefinedException {
        return ResponseEntity.ok(orderService.getListOrderByStatus(status));
    }

    @PutMapping("/status/{orderId}")
    public ResponseEntity<OrderEntity> putOrderStatus(@PathVariable UUID orderId, @RequestParam String status) throws OrderUndefinedException {
        return ResponseEntity.ok(orderService.putOrderStatus(orderId, status));
    }
}
