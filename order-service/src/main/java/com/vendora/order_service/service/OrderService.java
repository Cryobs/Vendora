package com.vendora.order_service.service;

import com.vendora.order_service.DTO.FinalItemsPriceDTO;
import com.vendora.order_service.DTO.FinalPriceDTO;
import com.vendora.order_service.DTO.OrderDTO;
import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.entity.OrderItemEntity;
import com.vendora.order_service.exception.OrderUndefinedException;
import com.vendora.order_service.feign.PriceClient;
import com.vendora.order_service.feign.WarehouseClient;
import com.vendora.order_service.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private PriceClient priceClient;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private WarehouseClient warehouseClient;

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

    public OrderEntity getOrder(UUID orderId) throws OrderUndefinedException {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderUndefinedException("Order with id " + orderId + " is undefined"));
    }

    public Iterable<OrderEntity> getListOrder() {
        return orderRepo.findAll();
    }



    public OrderEntity putOrderStatus(UUID orderId , String status) throws OrderUndefinedException {
        OrderEntity order = getOrder(orderId);
        order.setStatus(status);
        return orderRepo.save(order);
    }

    public OrderEntity createOrder(OrderDTO request){

        FinalPriceDTO finalPriceDTO = priceClient.calculate(request);
        System.out.println("Received FinalPriceDTO: " + finalPriceDTO.getFinalPrice()); // Логируем полученные данные

        OrderEntity order = new OrderEntity();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("Created");

        //use promo
        if(!request.getPromoCode().isEmpty() || request.getPromoCode() != null){
            System.out.println(priceClient.usePromo(request.getPromoCode()));
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
        List<FinalItemsPriceDTO> itemsPriceDTOs = priceClient.calculateItems(request);

        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (FinalItemsPriceDTO dto : itemsPriceDTOs) {
            OrderItemEntity itemEntity = convertToOrderItemEntity(dto, order);
            //reserve product
            warehouseClient.reserveProduct(itemEntity.getProductId(), itemEntity.getQuantity());
            orderItems.add(itemEntity);
        }

        order.setItems(orderItems);



        return orderRepo.save(order);
    }

    public List<OrderEntity> getListOrderByStatus(String status) throws OrderUndefinedException {
        List<OrderEntity> orders = orderRepo.findAllByStatus(status);

        if (orders.isEmpty()) {
            throw new OrderUndefinedException("No orders with status " + status);
        }
        return orders;
    }
}
