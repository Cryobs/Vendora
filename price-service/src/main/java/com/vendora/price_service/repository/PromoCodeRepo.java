package com.vendora.price_service.repository;

import com.vendora.price_service.entity.PromoCodeEntity;
import org.springframework.data.repository.CrudRepository;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface PromoCodeRepo extends CrudRepository<PromoCodeEntity, UUID> {
    Optional<PromoCodeEntity> findByCodeAndIsActive(String code, boolean isActive);
    Optional<ArrayList<PromoCodeEntity>> findByIsActive(boolean isActive);
    Optional<ArrayList<PromoCodeEntity>> findByCode(String code);
}
