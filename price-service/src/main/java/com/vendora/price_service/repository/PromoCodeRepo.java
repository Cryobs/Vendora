package com.vendora.price_service.repository;

import com.vendora.price_service.entity.PromoCodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PromoCodeRepo extends CrudRepository<PromoCodeEntity, String> {
    Optional<PromoCodeEntity> findByCodeAndIsActive(String code, boolean isActive);
}