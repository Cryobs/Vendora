package com.vendora.order_service.controller;

import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @GetMapping("/test")
    public String test(){
        return "TEST ORDER";
    }


    @Autowired
    private OrderService orderService;


//    @PostMapping("/create")
//    public ResponseEntity createOrder(@RequestBody OrderDTO order){
//        try {
//            return ResponseEntity.ok(orderService.createOrder(order));
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body("Error: " + e);
//        }
//    }
}
