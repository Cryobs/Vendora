package com.vendora.warehouse_service.service;

import com.vendora.warehouse_service.entity.InventoryEntity;
import com.vendora.warehouse_service.entity.InventoryMovementEntity;
import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.exception.ProductUnavailableException;
import com.vendora.warehouse_service.repository.InventoryMovementRepo;
import com.vendora.warehouse_service.repository.InventoryRepo;
import com.vendora.warehouse_service.repository.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class WarehouseService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private InventoryMovementRepo inventoryMovementRepo;

    @Autowired
    private ProductsRepo productsRepo;

    public static <T> Iterable<T> reverseIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);

        Collections.reverse(list);

        return list;
    }

    public Iterable<InventoryMovementEntity> getInventoryMovementList(){
        return reverseIterable(inventoryMovementRepo.findAll());
    }

    public InventoryEntity addStockToProduct(UUID productId, int additionalQuantity) throws ProductUnavailableException {
        Optional<InventoryEntity> optionalInventory = inventoryRepo.findByProductId(productId);

        if(optionalInventory.isPresent()){
            InventoryEntity inventory = optionalInventory.get();
            InventoryMovementEntity inventoryMovement = new InventoryMovementEntity();
            //check if unavaible
            if(inventory.getQuantity() + additionalQuantity >= 0 ) {
                inventory.setQuantity(inventory.getQuantity() + additionalQuantity);
                inventory.setLastUpdated(LocalDateTime.now());
                // inventory movement
                inventoryMovement.setProductId(inventory.getProduct().getId());
                inventoryMovement.setChangeType(additionalQuantity >= 0 ? "addition" : "removal");
                inventoryMovement.setQuantity(additionalQuantity);
                inventoryMovementRepo.save(inventoryMovement);
                return inventoryRepo.save(inventory);
            } else {
                throw new ProductUnavailableException("Product with ID " + productId + " unavailable" +
                        (inventory.getQuantity() != 0 ?  " with this quantity, available only " + inventory.getQuantity() : "")
                );
            }
        } else {
            Optional<ProductEntity> optionalProduct = productsRepo.findById(productId);
            //register product in warehouse if absent
            if (optionalProduct.isPresent()) {
                ProductEntity product = optionalProduct.get();
                InventoryEntity newInventory = new InventoryEntity();
                newInventory.setProduct(product);
                newInventory.setQuantity(additionalQuantity);
                newInventory.setLastUpdated(LocalDateTime.now());

                InventoryMovementEntity inventoryMovement = new InventoryMovementEntity();
                inventoryMovement.setProductId(newInventory.getProduct().getId());
                inventoryMovement.setChangeType(additionalQuantity >= 0 ? "addition" : "removal");
                inventoryMovement.setQuantity(additionalQuantity);
                inventoryMovementRepo.save(inventoryMovement);
                return inventoryRepo.save(newInventory);
            } else {
                throw new IllegalArgumentException("Product with ID " + productId + " not found.");
            }
        }
    }
}
