package com.vendora.order_service.repository;

import com.vendora.order_service.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepo extends CrudRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByStatus(String status);
    Optional<OrderEntity> findByIdAndUserId(UUID id, String userId);
    List<OrderEntity> findAllByUserId(String userId);
}
