package com.vendora.price_service.repository;

import com.vendora.price_service.entity.TaxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaxRepo extends JpaRepository<TaxEntity, UUID> {
    Optional<TaxEntity> findByRegion(String region);
}
