package com.vendora.price_service.service;


import com.vendora.price_service.DTO.*;
import com.vendora.price_service.entity.ShippingEntity;
import com.vendora.price_service.feign.WarehouseClient;
import com.vendora.price_service.repository.ShippingRepo;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService {

    @Autowired
    private ShippingRepo shippingRepo;
    @Autowired
    private PromoCodeService promoCodeService;
    @Autowired
    private WarehouseClient warehouseClient;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private TaxService taxService;

    public List<FinalItemsPriceDTO> calculateItemsPrice(OrderDTO request) {
        ShippingEntity shippingRequest = request.getShipping();
        String promoCode = request.getPromoCode();

        List<FinalItemsPriceDTO> result = new ArrayList<>();
        for (OrderItemDTO item : request.getItems()){
            BigDecimal basePrice;
            try {
                basePrice = warehouseClient.getProduct(item.getProductId()).getBody().getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            } catch (FeignException.NotFound e) {
                throw new RuntimeException("Product not found: " + item.getProductId(), e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch product: " + e.getMessage(), e);
            }

            // discount
            BigDecimal discount_value = discountService.calculateDiscount(item.getProductId(), basePrice);
            //apply discounts
            BigDecimal finalPrice = basePrice.subtract(discount_value);
            //tax
            BigDecimal tax = taxService.calculateTax(finalPrice, request.getRegion());
            // apply tax
            finalPrice = finalPrice.add(tax);

            //final price dto create
            FinalItemsPriceDTO finalPriceDTO = new FinalItemsPriceDTO();
            finalPriceDTO.setFinalPrice(finalPrice);
            finalPriceDTO.setBasePrice(basePrice);
            finalPriceDTO.setProductId(item.getProductId());
            finalPriceDTO.setQuantity(item.getQuantity());
            finalPriceDTO.setTotalDiscount(discount_value);
            finalPriceDTO.setTotalTax(tax);

            result.add(finalPriceDTO);
        }

        return result;
    }



    public FinalPriceDTO calculatePrice(OrderDTO request) {
        ShippingEntity shippingRequest = request.getShipping();
        String promoCode = request.getPromoCode();

        FinalPriceDTO result = new FinalPriceDTO();
        result.setTotalTax(BigDecimal.ZERO);
        result.setTotalPrice(BigDecimal.ZERO);
        result.setFinalPrice(BigDecimal.ZERO);
        result.setTotalDiscount(BigDecimal.ZERO);

        for (OrderItemDTO item : request.getItems()){
            BigDecimal basePrice;
            try {
                basePrice = warehouseClient.getProduct(item.getProductId()).getBody().getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            } catch (FeignException.NotFound e) {
                throw new RuntimeException("Product not found: " + item.getProductId(), e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch product: " + e.getMessage(), e);
            }

            // discount
            BigDecimal discount_value = discountService.calculateDiscount(item.getProductId(), basePrice);
            //apply discounts
            BigDecimal finalPrice = basePrice.subtract(discount_value);

            result.setFinalPrice(result.getFinalPrice().add(finalPrice));
            result.setTotalPrice(result.getTotalPrice().add(basePrice));
            result.setTotalDiscount(result.getTotalDiscount().add(discount_value));


        }



        //tax
        BigDecimal tax = taxService.calculateTax(result.getFinalPrice(), request.getRegion());
        result.setTotalTax(result.getTotalTax().add(tax));
        //shipping
        result.setTotalShipping(shippingRepo.findById(shippingRequest.getId()).get().getPrice());

        result.setFinalPrice(result.getFinalPrice().add(tax).add(result.getTotalShipping()));
        //promo code
        BigDecimal code = promoCodeService.calculatePromoCode(promoCode, result.getFinalPrice());
        int compare = code.compareTo(result.getFinalPrice());
        // if code > finalPrice
        if (compare == 1){
            code = result.getFinalPrice();
        }
        result.setFinalPrice(result.getFinalPrice().subtract(code));


        result.setTotalDiscount(result.getTotalDiscount().add(code));


        return result;
    }



}
