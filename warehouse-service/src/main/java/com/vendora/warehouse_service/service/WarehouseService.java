package com.vendora.warehouse_service.service;

import com.vendora.warehouse_service.entity.InventoryEntity;
import com.vendora.warehouse_service.entity.InventoryMovementEntity;
import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.exception.ProductUnavailableException;
import com.vendora.warehouse_service.exception.ProductUndefinedException;
import com.vendora.warehouse_service.feign.CatalogClient;
import com.vendora.warehouse_service.repository.InventoryMovementRepo;
import com.vendora.warehouse_service.repository.InventoryRepo;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Service
public class WarehouseService {

    private static final Logger log = LogManager.getLogger(WarehouseService.class);
    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private InventoryMovementRepo inventoryMovementRepo;

    @Autowired
    private CatalogClient catalogClient;

    public Page<InventoryMovementEntity> getInventoryMovementList(Pageable pageable){
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        return inventoryMovementRepo.findAll(sortedPageable);
    }

    @Transactional
    public InventoryEntity reserveProduct(UUID productId, int quantity) throws ProductUndefinedException, ProductUnavailableException {
        InventoryEntity productInventory = inventoryRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductUndefinedException("Undefined product id"));
        if (productInventory.getQuantity() < quantity){
            throw new ProductUnavailableException("Unavailable " + quantity + " products, available: " + productInventory.getQuantity());
        } else {
            productInventory.setQuantity(productInventory.getQuantity() - quantity);
            productInventory.setReservedQuantity(productInventory.getReservedQuantity() + quantity);
            productInventory.setLastUpdated(LocalDateTime.now());

            inventoryRepo.save(productInventory);

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

    public Boolean checkStock(UUID productId, int quantity) throws ProductUndefinedException {
        InventoryEntity product = inventoryRepo.findByProductId(productId)
                .orElseThrow(() -> new ProductUndefinedException("Undefined product id"));

        return product.getQuantity() >= quantity;
    }

    @Async("updateStockImportExecutor")
    public void updateStockImport(MultipartFile file){
        try (InputStream inputStream = file.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<String[]> batch = new ArrayList<>();
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null){
                String[] columns = line.split(",");
                batch.add(columns);
                lineCount++;

                if (lineCount == 100){
                    processBatchStockImport(batch);
                    batch.clear();
                    lineCount = 0;
                }
            }

            if (!batch.isEmpty()){
                processBatchStockImport(batch);
            }

        } catch (Exception e){
            log.error("Error processing file: {}", e.getMessage());
        }
    }

    @Async("processBatchStockImportExecutor")
    private void processBatchStockImport (List<String[]> batch){
        for (String[] row : batch){
            System.out.println("Processing row: " + String.join(",", row));
        }
    }

    public InventoryEntity addStockToProduct(UUID productId, int additionalQuantity) throws ProductUnavailableException {
        Optional<InventoryEntity> optionalInventory = inventoryRepo.findByProductId(productId);

        if(optionalInventory.isPresent()){
            InventoryEntity inventory = optionalInventory.get();
            //check if unavailable
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
