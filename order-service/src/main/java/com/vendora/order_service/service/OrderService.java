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
        return orderRepo.save(order);
    }

    @Transactional
    public OrderEntity createOrder(CreateOrderDTO request, @AuthenticationPrincipal Jwt jwt){

        System.out.println("Get cart");
        String token = "Bearer " + jwt.getTokenValue();
        CartDTO cart = cartClient.getCart(token);
        System.out.println("getted cart");

        OrderEntity order = new OrderEntity();
        order.setUserId(jwt.getSubject());
        order.setStatus("pending");
        order.setRegion(request.getRegion());
        order.setShippingAddress(request.getShippingAddress());

        order = orderRepo.save(order);

        System.out.println("start calc items");
        List<OrderItemEntity> items = new ArrayList<>();
        for (int i = 0; i < cart.getItems().size(); i++) {
            OrderItemEntity item = new OrderItemEntity(
                    order,
                    cart.getItems().get(i).getProductId(),
                    cart.getItems().get(i).getQuantity(),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
            items.add(item);
        }
        orderItemRepo.saveAll(items);
        System.out.println("end calc items");
        order.setItems(items);
        order = orderRepo.save(order);

        System.out.println("start calc order");
        // Получаем обновлённый заказ с рассчитанными ценами
        OrderDTO updatedOrderDTO = priceClient.calculateOrder(orderMapper.toDTO(order));

        // **Не создаём новый объект, а обновляем старый**
        order.setFinalPrice(updatedOrderDTO.getFinalPrice());
        order.setTotalTax(updatedOrderDTO.getTotalTax());
        order.setTotalDiscount(updatedOrderDTO.getTotalDiscount());

        // Обновляем цены у товаров
        for (int i = 0; i < order.getItems().size(); i++) {
            order.getItems().get(i).setFinalPrice(updatedOrderDTO.getItems().get(i).getFinalPrice());
            order.getItems().get(i).setTotalDiscount(updatedOrderDTO.getItems().get(i).getTotalDiscount());
            order.getItems().get(i).setTotalTax(updatedOrderDTO.getItems().get(i).getTotalTax());
        }
        System.out.println("end calc order");
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
