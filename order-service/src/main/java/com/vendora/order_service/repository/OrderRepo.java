package com.vendora.order_service.repository;

import com.vendora.order_service.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepo extends CrudRepository<OrderEntity, UUID> {
}
