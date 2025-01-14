package com.vendora.warehouse_service.controller;

import com.vendora.warehouse_service.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PutMapping("/updateStock/{productId}")
    public ResponseEntity updateStock(@PathVariable UUID productId, @RequestParam int quantity){
        try {
            return ResponseEntity.ok(warehouseService.addStockToProduct(productId, quantity));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @GetMapping("/movement/list")
    public ResponseEntity getInventoryMovementList(){
        try {
            return ResponseEntity.ok(warehouseService.getInventoryMovementList());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
