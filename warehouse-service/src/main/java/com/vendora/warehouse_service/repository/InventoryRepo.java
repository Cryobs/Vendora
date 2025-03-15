package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.InventoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepo extends JpaRepository<InventoryEntity, UUID> {
    Page<InventoryEntity> findAll(Pageable pageable);

    Page<InventoryEntity> findByProductId(UUID productId, Pageable pageable);

    Optional<InventoryEntity> findByProductId(UUID productId);
}
