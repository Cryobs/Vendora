package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.InventoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepo extends CrudRepository<InventoryEntity, UUID> {
    Optional<InventoryEntity> findByProductId(UUID productId);
}
