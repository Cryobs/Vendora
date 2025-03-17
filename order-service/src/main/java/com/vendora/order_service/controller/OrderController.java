package com.vendora.order_service.controller;

import com.vendora.order_service.DTO.CreateOrderDTO;
import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.exception.OrderUndefinedException;
import com.vendora.order_service.repository.OrderRepo;
import com.vendora.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody CreateOrderDTO request, @AuthenticationPrincipal Jwt jwt){
        OrderEntity createdOrder = orderService.createOrder(request, jwt);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable UUID orderId, @AuthenticationPrincipal Jwt jwt) throws OrderUndefinedException {
        return ResponseEntity.ok(orderService.getOrder(orderId, jwt));
    }

    @GetMapping("/list/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Page<OrderEntity>> getListOfAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getListOfAllOrders(pageable));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<Page<OrderEntity>> getOrdersList(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersList(jwt, pageable));
    }

    @GetMapping("/list/{status}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Page<OrderEntity>> getListOrderByStatus(@PathVariable String status, Pageable pageable) throws OrderUndefinedException {
        return ResponseEntity.ok(orderService.getListOrderByStatus(status, pageable));
    }

    @PutMapping("/status/{orderId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<OrderEntity> putOrderStatus(@PathVariable UUID orderId, @RequestParam String status) throws OrderUndefinedException {
        return ResponseEntity.ok(orderService.putOrderStatus(orderId, status));
    }
}
