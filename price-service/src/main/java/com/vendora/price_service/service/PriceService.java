package com.vendora.price_service.service;


import com.vendora.price_service.DTO.*;
import com.vendora.price_service.feign.CatalogClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {

    @Autowired
    private CatalogClient catalogClient;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private TaxService taxService;

    public OrderItemDTO calculateItem(OrderItemDTO item, String region){
        try {
            item.setFinalPrice(catalogClient.getProduct(item.getProductId()).getBody().getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Product not found: " + item.getProductId(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch product: " + e.getMessage(), e);
        }
        //calculate discount
        item.setTotalDiscount(discountService.calculateDiscount(item.getProductId(), item.getFinalPrice()));
        item.setFinalPrice(
                item.getFinalPrice()
                        .subtract(item.getTotalDiscount())
        );
        //calculate tax
        item.setTotalTax(taxService.calculateTax(item.getFinalPrice(), region));
        item.setFinalPrice(
                item.getFinalPrice()
                        .add(item.getTotalTax())
        );
        return item;
    }

    public OrderDTO calculateOrder(OrderDTO request){

        request.setFinalPrice(BigDecimal.ZERO);
        request.setTotalTax(BigDecimal.ZERO);
        request.setTotalDiscount(BigDecimal.ZERO);

        List<OrderItemDTO> items = request.getItems();

        for (int i = 0; i < items.size(); i++) {
            OrderItemDTO item = items.get(i);
            item = calculateItem(item, request.getRegion());
            //final price
            request.setFinalPrice(
                    request.getFinalPrice()
                            .add(item.getFinalPrice())
            );
            //discount
            request.setTotalDiscount(
                    request.getTotalDiscount()
                            .add(item.getTotalDiscount())
            );
            //tax
            request.setTotalTax(
                    request.getTotalTax()
                            .add(item.getTotalTax())
            );
        }

        return request;
    }



}
