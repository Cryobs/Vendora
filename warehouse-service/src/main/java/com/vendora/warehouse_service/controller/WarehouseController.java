package com.vendora.warehouse_service.controller;

import com.vendora.warehouse_service.entity.InventoryEntity;
import com.vendora.warehouse_service.entity.InventoryMovementEntity;
import com.vendora.warehouse_service.exception.ProductUnavailableException;
import com.vendora.warehouse_service.exception.ProductUndefinedException;
import com.vendora.warehouse_service.repository.InventoryRepo;
import com.vendora.warehouse_service.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InventoryRepo inventoryRepo;

    @PutMapping("/stock/{productId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<InventoryEntity> updateStock(@PathVariable UUID productId, @RequestParam int quantity) throws ProductUnavailableException {
        return ResponseEntity.ok(warehouseService.addStockToProduct(productId, quantity));
    }

    @GetMapping("/movement/list")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Page<InventoryMovementEntity>> getInventoryMovementList(Pageable pageable){
        return ResponseEntity.ok(warehouseService.getInventoryMovementList(pageable));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Page<InventoryEntity>> getInventoryList(Pageable pageable){
        return ResponseEntity.ok(inventoryRepo.findAll(pageable));
    }


    @PutMapping("/reserve/{productId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<InventoryEntity> reserveProduct(@PathVariable UUID productId, @RequestParam int quantity) throws ProductUnavailableException, ProductUndefinedException {
        InventoryEntity product = warehouseService.reserveProduct(productId, quantity);
        return ResponseEntity.ok(product);
    }
}
