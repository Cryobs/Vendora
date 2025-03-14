package com.vendora.order_service.repository;

import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.entity.OrderItemEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepo extends CrudRepository<OrderItemEntity, UUID> {
}
