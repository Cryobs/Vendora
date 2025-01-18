package com.vendora.order_service.service;

import com.vendora.order_service.DTO.FinalItemsPriceDTO;
import com.vendora.order_service.DTO.FinalPriceDTO;
import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.DTO.OrderItemDTO;
import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.entity.OrderItemEntity;
import com.vendora.order_service.feign.PriceService;
import com.vendora.order_service.feign.WarehouseService;
import com.vendora.order_service.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private PriceService priceService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private WarehouseService warehouseService;

    private OrderItemEntity convertToOrderItemEntity(FinalItemsPriceDTO dto, OrderEntity order) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        itemEntity.setProductId(dto.getProductId());
        itemEntity.setQuantity(dto.getQuantity());
        itemEntity.setBasePrice(dto.getBasePrice());
        itemEntity.setFinalPrice(dto.getFinalPrice());
        itemEntity.setTotalDiscount(dto.getTotalDiscount());
        itemEntity.setTotalTax(dto.getTotalTax());
        itemEntity.setOrder(order);
        return itemEntity;
    }

    public OrderEntity createOrder(OrderDTO request){

        FinalPriceDTO finalPriceDTO = priceService.calculate(request).getData();
        System.out.println("Received FinalPriceDTO: " + finalPriceDTO.getFinalPrice()); // Логируем полученные данные

        OrderEntity order = new OrderEntity();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("Created");

        //use promo
        if(!request.getPromoCode().isEmpty() || request.getPromoCode() != null){
            System.out.println(priceService.usePromo(request.getPromoCode()).getData());
            System.out.println("test");
        }

        //set prices
        if (finalPriceDTO.getFinalPrice() == null) {
            throw new IllegalArgumentException("Final price cannot be null");
        }

        order.setTotalDiscount(finalPriceDTO.getTotalDiscount());
        order.setTotalPrice(finalPriceDTO.getTotalPrice());
        order.setTotalTax(finalPriceDTO.getTotalTax());
        order.setFinalPrice(finalPriceDTO.getFinalPrice());



        order = orderRepo.save(order);

        // calculate items
        List<FinalItemsPriceDTO> itemsPriceDTOs = priceService.calculateItems(request).getData();

        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (FinalItemsPriceDTO dto : itemsPriceDTOs) {
            OrderItemEntity itemEntity = convertToOrderItemEntity(dto, order);
            //reserve product
            warehouseService.reserveProduct(itemEntity.getProductId(), itemEntity.getQuantity());
            orderItems.add(itemEntity);
        }

        order.setItems(orderItems);



        return orderRepo.save(order);
    }
}
