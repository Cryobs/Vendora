package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.entity.WarehouseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WarehouseRepo extends CrudRepository<WarehouseEntity, Long> {
    Optional<WarehouseEntity> findByProductId(Long productId);
}
