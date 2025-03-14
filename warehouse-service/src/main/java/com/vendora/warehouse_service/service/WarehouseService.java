package com.vendora.warehouse_service.service;

import com.vendora.warehouse_service.entity.InventoryEntity;
import com.vendora.warehouse_service.entity.InventoryMovementEntity;
import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.exception.ProductUnavailableException;
import com.vendora.warehouse_service.exception.ProductUndefinedException;
import com.vendora.warehouse_service.feign.CatalogClient;
import com.vendora.warehouse_service.repository.InventoryMovementRepo;
import com.vendora.warehouse_service.repository.InventoryRepo;
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
    private CatalogClient catalogClient;

    private static <T> Iterable<T> reverseIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);

        Collections.reverse(list);

        return list;
    }

    public Iterable<InventoryMovementEntity> getInventoryMovementList(){
        return reverseIterable(inventoryMovementRepo.findAll());
    }

    public InventoryEntity reserveProduct(UUID productId, int quantity) throws ProductUndefinedException, ProductUnavailableException {
        InventoryEntity productInventory = inventoryRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductUndefinedException("Undefined product id"));
        if (productInventory.getQuantity() < quantity){
            throw new ProductUnavailableException("Unavailable " + quantity + " products, available: " + productInventory.getQuantity());
        } else {
            productInventory.setQuantity(productInventory.getQuantity() - quantity);
            productInventory.setReservedQuantity(productInventory.getReservedQuantity() + quantity);
            productInventory.setLastUpdated(LocalDateTime.now());

            moveInventory(productId, "reserve", quantity);

            return productInventory;
        }
    }

    private InventoryMovementEntity moveInventory(UUID productId, String changeType, int quantity){
        InventoryMovementEntity inventoryMovement = moveInventory(productId, changeType);
        inventoryMovement.setQuantity(quantity);
        return inventoryMovementRepo.save(inventoryMovement);
    }

    private InventoryMovementEntity moveInventory(UUID productId, String changeType){
        InventoryMovementEntity inventoryMovement = new InventoryMovementEntity();
        inventoryMovement.setProductId(productId);
        inventoryMovement.setChangeType(changeType);
        inventoryMovement.setQuantity(0);
        return inventoryMovementRepo.save(inventoryMovement);
    }

    public InventoryEntity addStockToProduct(UUID productId, int additionalQuantity) throws ProductUnavailableException {
        Optional<InventoryEntity> optionalInventory = inventoryRepo.findByProductId(productId);

        if(optionalInventory.isPresent()){
            InventoryEntity inventory = optionalInventory.get();
            //check if unavaible
            if(inventory.getQuantity() + additionalQuantity >= 0 ) {
                inventory.setQuantity(inventory.getQuantity() + additionalQuantity);
                inventory.setLastUpdated(LocalDateTime.now());
                // inventory movement
                if (additionalQuantity >= 0){
                    moveInventory(productId, "addition");
                } else {
                    moveInventory(productId, "removal");
                }
                return inventoryRepo.save(inventory);
            } else {
                throw new ProductUnavailableException("ProductEntity with ID " + productId + " unavailable" +
                        (inventory.getQuantity() != 0 ?  " with this quantity, available only " + inventory.getQuantity() : "")
                );
            }
        } else {
            ProductEntity product = catalogClient.getProduct(productId).getBody();
            //register product in warehouse if absent
            if (product != null) {
                InventoryEntity newInventory = new InventoryEntity();
                newInventory.setProduct(product);
                newInventory.setQuantity(additionalQuantity);
                newInventory.setLastUpdated(LocalDateTime.now());

                if (additionalQuantity >= 0){
                    moveInventory(productId, "addition");
                } else {
                    moveInventory(productId, "removal");
                }
                return inventoryRepo.save(newInventory);
            } else {
                throw new IllegalArgumentException("ProductEntity with ID " + productId + " not found.");
            }
        }
    }
}
