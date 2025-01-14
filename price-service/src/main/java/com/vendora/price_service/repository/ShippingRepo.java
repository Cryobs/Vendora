package com.vendora.price_service.repository;

import com.vendora.price_service.entity.ShippingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShippingRepo extends CrudRepository<ShippingEntity, UUID> {
}
