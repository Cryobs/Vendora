package com.vendora.warehouse_service.controller;

import com.vendora.warehouse_service.entity.InventoryEntity;
import com.vendora.warehouse_service.entity.InventoryMovementEntity;
import com.vendora.warehouse_service.exception.ProductUnavailableException;
import com.vendora.warehouse_service.exception.ProductUndefinedException;
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
    public ResponseEntity<InventoryEntity> updateStock(@PathVariable UUID productId, @RequestParam int quantity) throws ProductUnavailableException {
        return ResponseEntity.ok(warehouseService.addStockToProduct(productId, quantity));
    }

    @GetMapping("/movement/list")
    public ResponseEntity<Iterable<InventoryMovementEntity>> getInventoryMovementList(){
        return ResponseEntity.ok(warehouseService.getInventoryMovementList());
    }


    @PutMapping("/reserve/{productId}")
    public ResponseEntity<InventoryEntity> reserveProduct(@PathVariable UUID productId, @RequestParam int quantity) throws ProductUnavailableException, ProductUndefinedException {
        InventoryEntity product = warehouseService.reserveProduct(productId, quantity);
        return ResponseEntity.ok(product);
    }
}
