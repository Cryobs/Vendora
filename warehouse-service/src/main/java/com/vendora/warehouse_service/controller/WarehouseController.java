package com.vendora.warehouse_service.controller;

import com.vendora.warehouse_service.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PutMapping("/updateStock/{productId}")
    public ResponseEntity updateStock(@PathVariable Long productId, @RequestParam int quantity){
        try {
            return ResponseEntity.ok(warehouseService.addStockToProduct(productId, quantity));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
