package com.vendora.order_service.service;

import com.vendora.order_service.DTO.*;
import com.vendora.order_service.entity.OrderEntity;
import com.vendora.order_service.entity.OrderItemEntity;
import com.vendora.order_service.exception.OrderUndefinedException;
import com.vendora.order_service.feign.CartClient;
import com.vendora.order_service.feign.PriceClient;
import com.vendora.order_service.feign.WarehouseClient;
import com.vendora.order_service.mapper.OrderMapper;
import com.vendora.order_service.repository.OrderItemRepo;
import com.vendora.order_service.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private PriceClient priceClient;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private WarehouseClient warehouseClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KafkaProducerService kafkaProducerService;


    public OrderEntity getOrder(UUID orderId, Jwt jwt) throws OrderUndefinedException {
        return orderRepo.findByIdAndUserId(orderId, jwt.getClaim("sub"))
                .orElseThrow(() -> new OrderUndefinedException("Order with id " + orderId + " is undefined"));
    }

    public Iterable<OrderEntity> getOrdersList(Jwt jwt){
        return orderRepo.findAllByUserId(jwt.getClaim("sub"));
    }

    public Iterable<OrderEntity> getListOfAllOrders() {
        return orderRepo.findAll();
    }



    public OrderEntity putOrderStatus(UUID orderId , String status) throws OrderUndefinedException {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderUndefinedException("Order with id " + orderId + " is undefined"));
        order.setStatus(status);

        order = orderRepo.save(order);
        kafkaProducerService.sendOrder(order);
        return order;
    }

    @Transactional(rollbackOn = Exception.class)
    public OrderEntity createOrder(CreateOrderDTO request, @AuthenticationPrincipal Jwt jwt){

        String token = "Bearer " + jwt.getTokenValue();
        CartDTO cart = cartClient.getCart(token);

        OrderEntity order = new OrderEntity();
        order.setUserId(jwt.getSubject());
        order.setStatus("pending");
        order.setRegion(request.getRegion());
        order.setShippingAddress(request.getShippingAddress());

        order = orderRepo.save(order);

        List<OrderItemEntity> items = new ArrayList<>();
        for (int i = 0; i < cart.getItems().size(); i++) {
            UUID productId = cart.getItems().get(i).getProductId();
            int quantity = cart.getItems().get(i).getQuantity();
            warehouseClient.reserveProduct(productId, quantity);
            OrderItemEntity item = new OrderItemEntity(
                    order,
                    productId,
                    quantity,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
            items.add(item);
        }
        orderItemRepo.saveAll(items);
        order.setItems(items);
        order = orderRepo.save(order);
        // get order with prices
        OrderDTO updatedOrderDTO = priceClient.calculateOrder(orderMapper.toDTO(order));

        order.setFinalPrice(updatedOrderDTO.getFinalPrice());
        order.setTotalTax(updatedOrderDTO.getTotalTax());
        order.setTotalDiscount(updatedOrderDTO.getTotalDiscount());

        // Update items prices
        for (int i = 0; i < order.getItems().size(); i++) {
            order.getItems().get(i).setFinalPrice(updatedOrderDTO.getItems().get(i).getFinalPrice());
            order.getItems().get(i).setTotalDiscount(updatedOrderDTO.getItems().get(i).getTotalDiscount());
            order.getItems().get(i).setTotalTax(updatedOrderDTO.getItems().get(i).getTotalTax());
        }

        cartClient.deleteCart(token);

        order = orderRepo.save(order);
        kafkaProducerService.sendOrder(order);

        return order;
    }

    public List<OrderEntity> getListOrderByStatus(String status) throws OrderUndefinedException {
        List<OrderEntity> orders = orderRepo.findAllByStatus(status);

        if (orders.isEmpty()) {
            throw new OrderUndefinedException("No orders with status " + status);
        }
        return orders;
    }
}
