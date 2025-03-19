package com.vendora.price_service.service;


import com.vendora.price_service.DTO.*;
import com.vendora.price_service.feign.CatalogClient;
import feign.FeignException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PriceService {

    private static final Logger log = LogManager.getLogger(PriceService.class);
    @Autowired
    private CatalogClient catalogClient;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private TaxService taxService;

    @Async("customExecutor")
    public CompletableFuture<OrderItemDTO> calculateItem(OrderItemDTO orderItem, String region) {
        try {
            log.info("Calculating item for productId: " + orderItem.getProductId());

            BigDecimal basePrice = catalogClient.getProduct(orderItem.getProductId())
                    .getBody().getBasePrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

            log.info("Base price for productId {}: {}", orderItem.getProductId(), basePrice);

            BigDecimal discount = discountService.calculateDiscount(orderItem.getProductId(), basePrice);
            BigDecimal finalPrice = basePrice.subtract(discount);
            BigDecimal tax = taxService.calculateTax(finalPrice, region);
            finalPrice = finalPrice.add(tax);

            orderItem.setFinalPrice(finalPrice);
            orderItem.setTotalDiscount(discount);
            orderItem.setTotalTax(tax);

            log.info("Final price for productId {}: {}", orderItem.getProductId(), finalPrice);

            return CompletableFuture.completedFuture(orderItem);
        } catch (FeignException.NotFound e) {
            log.error("Product not found: " + orderItem.getProductId(), e);
            throw new RuntimeException("Product not found: " + orderItem.getProductId(), e);
        } catch (Exception e) {
            log.error("Failed to fetch product for productId: " + orderItem.getProductId(), e);
            throw new RuntimeException("Failed to fetch product: " + e.getMessage(), e);
        }
    }

    public OrderDTO calculateOrder(OrderDTO request) {
        log.info("Starting order calculation for orderId: {}", request.getId());

        AtomicReference<BigDecimal> totalFinalPrice = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalTax = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalDiscount = new AtomicReference<>(BigDecimal.ZERO);

        List<CompletableFuture<OrderItemDTO>> futures = request.getItems().stream()
                .map(item -> calculateItem(item, request.getRegion())
                        .thenApply(orderItem -> {
                            totalFinalPrice.updateAndGet(v -> v.add(orderItem.getFinalPrice()));
                            totalTax.updateAndGet(v -> v.add(orderItem.getTotalTax()));
                            totalDiscount.updateAndGet(v -> v.add(orderItem.getTotalDiscount()));
                            return orderItem;
                        }))
                .collect(Collectors.toList());

        // Wait for all async tasks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Update order with final calculated values
        request.setFinalPrice(totalFinalPrice.get());
        request.setTotalTax(totalTax.get());
        request.setTotalDiscount(totalDiscount.get());

        // Collect the processed items back
        List<OrderItemDTO> items = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        request.setItems(items);
        log.info("Order calculation completed for orderId: {}", request.getId());

        return request;
    }



}
