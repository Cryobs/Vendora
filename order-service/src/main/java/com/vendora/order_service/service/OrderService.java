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
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    public Page<OrderEntity> getOrdersList(Jwt jwt, Pageable pageable){
        return orderRepo.findAllByUserId(jwt.getClaim("sub"), pageable);
    }

    public Page<OrderEntity> getListOfAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }



    public OrderEntity putOrderStatus(UUID orderId , String status) throws OrderUndefinedException {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderUndefinedException("Order with id " + orderId + " is undefined"));
        order.setStatus(status);

        order = orderRepo.save(order);
        kafkaProducerService.sendOrder(order);
        return order;
    }

    @Async
    public CompletableFuture<Boolean> reserveItem(UUID productId, int quantity){
        boolean reserved = warehouseClient.reserveProduct(productId, quantity).getStatusCode().is2xxSuccessful();
        return CompletableFuture.completedFuture(reserved);
    }

    @Transactional(rollbackOn = Exception.class)
    public OrderEntity createOrder(CreateOrderDTO request, @AuthenticationPrincipal Jwt jwt) {
        String token = "Bearer " + jwt.getTokenValue();
        CartDTO cart = cartClient.getCart(token);

        OrderEntity order = new OrderEntity();
        order.setUserId(jwt.getSubject());
        order.setStatus("created");
        order.setRegion(request.getRegion());
        order.setShippingAddress(request.getShippingAddress());

        order = orderRepo.save(order);

        OrderEntity finalOrder = order;
        List<CompletableFuture<OrderItemEntity>> futures = cart.getItems().stream()
                .map(cartItem -> reserveItem(cartItem.getProductId(), cartItem.getQuantity())
                        .thenApply(reserved -> new OrderItemEntity(
                                finalOrder,
                                cartItem.getProductId(),
                                cartItem.getQuantity(),
                                reserved ? BigDecimal.ONE : BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO
                        ))
                ).toList();

        // Дожидаемся выполнения всех асинхронных задач
        List<OrderItemEntity> items = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // Сохраняем все элементы заказа после завершения резервирования
        orderItemRepo.saveAll(items);
        order.setItems(items);
        order = orderRepo.save(order);

        // Получаем обновленные цены
        OrderDTO updatedOrderDTO = priceClient.calculateOrder(orderMapper.toDTO(order));
        order.setFinalPrice(updatedOrderDTO.getFinalPrice());
        order.setTotalTax(updatedOrderDTO.getTotalTax());
        order.setTotalDiscount(updatedOrderDTO.getTotalDiscount());

        // Обновляем цены у товаров
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

    public Page<OrderEntity> getListOrderByStatus(String status, Pageable pageable) throws OrderUndefinedException {
        Page<OrderEntity> orders = orderRepo.findAllByStatus(status, pageable);

        if (orders.getContent().isEmpty()) {
            throw new OrderUndefinedException("No orders with status " + status);
        }
        return orders;
    }
}
