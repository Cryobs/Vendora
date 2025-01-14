package com.vendora.price_service.repository;

import com.vendora.price_service.entity.DiscountEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DiscountRepo extends CrudRepository<DiscountEntity, UUID> {
    public Optional<DiscountEntity> findByProductId(UUID product_id);
}
