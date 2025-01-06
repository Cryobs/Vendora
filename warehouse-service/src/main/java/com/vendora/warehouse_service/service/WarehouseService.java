package com.vendora.warehouse_service.service;

import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.entity.WarehouseEntity;
import com.vendora.warehouse_service.exception.ProductUnavailableException;
import com.vendora.warehouse_service.repository.ProductRepo;
import com.vendora.warehouse_service.repository.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    public WarehouseEntity addStockToProduct(Long productId, int additionalQuantity) throws ProductUnavailableException {
        Optional<WarehouseEntity> optionalWarehouse = warehouseRepo.findByProductId(productId);

        if(optionalWarehouse.isPresent()){
            WarehouseEntity warehouse = optionalWarehouse.get();
            //check if unavaible
            if(warehouse.getStockQuantity() + additionalQuantity >= 0 ) {
                warehouse.setStockQuantity(warehouse.getStockQuantity() + additionalQuantity);
                return warehouseRepo.save(warehouse);
            } else {
                throw new ProductUnavailableException("Product with ID " + productId + " unavailable" +
                        (warehouse.getStockQuantity() != 0 ?  " with this quantity, available only " + warehouse.getStockQuantity() : "")
                );
            }
        } else {
            Optional<ProductEntity> optionalProduct = productRepo.findById(productId);
            //register product in warehouse if absent
            if (optionalProduct.isPresent()) {
                ProductEntity product = optionalProduct.get();
                WarehouseEntity newWarehouse = new WarehouseEntity();
                newWarehouse.setProduct(product);
                newWarehouse.setStockQuantity(additionalQuantity);
                return warehouseRepo.save(newWarehouse);
            } else {
                throw new IllegalArgumentException("Product with ID " + productId + " not found.");
            }
        }
    }
}
