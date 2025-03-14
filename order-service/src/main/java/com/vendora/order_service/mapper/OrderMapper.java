package com.vendora.order_service.mapper;

import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.entity.OrderEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public OrderDTO toDTO(OrderEntity entity) {
        return modelMapper.map(entity, OrderDTO.class);
    }

    public OrderEntity toEntity(OrderDTO dto) {
        return modelMapper.map(dto, OrderEntity.class);
    }
}
