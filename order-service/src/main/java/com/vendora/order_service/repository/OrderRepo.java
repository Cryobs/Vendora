package com.vendora.order_service.repository;

import com.vendora.order_service.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<OrderEntity, UUID> {
    Page<OrderEntity> findAllByStatus(String status, Pageable pageable);
    Optional<OrderEntity> findByIdAndUserId(UUID id, String userId);
    Page<OrderEntity> findAllByUserId(String userId, Pageable pageable);
}
