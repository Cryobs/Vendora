package com.vendora.price_service.service;


import com.vendora.price_service.DTO.FinalPriceDTO;
import com.vendora.price_service.DTO.PriceCalculateDTO;
import com.vendora.price_service.DTO.ShippingDTO;
import com.vendora.price_service.entity.ShippingEntity;
import com.vendora.price_service.feign.WarehouseService;
import com.vendora.price_service.repository.DiscountRepo;
import com.vendora.price_service.repository.PromoCodeRepo;
import com.vendora.price_service.repository.ShippingRepo;
import com.vendora.price_service.repository.TaxRepo;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceService {


    private static final Logger log = LoggerFactory.getLogger(PriceService.class);
    @Autowired
    private ShippingRepo shippingRepo;
    @Autowired
    private PromoCodeService promoCodeService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private TaxService taxService;



    public FinalPriceDTO calculatePrice(PriceCalculateDTO request) {
        BigDecimal basePrice;
        try {
            basePrice = warehouseService.getProduct(request.getProductId()).getBody().getBasePrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Product not found: " + request.getProductId(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch product: " + e.getMessage(), e);
        }

        // discount
        BigDecimal discount_value = discountService.calculateDiscount(request.getProductId(), basePrice);
        //Promo Code
        BigDecimal code = promoCodeService.calculatePromoCode(request.getPromoCode(), basePrice);
        //apply discounts
        BigDecimal final_price = basePrice.subtract(discount_value).subtract(code);
        //tax
        BigDecimal tax = taxService.calculateTax(final_price, request.getRegion());
        //shipping
        BigDecimal shipping = shippingRepo.findById(request.getShippingId()).get().getPrice();
        // apply tax and shipping
        final_price = final_price.add(tax).add(shipping);

        //final price dto create
        FinalPriceDTO finalPriceDTO = new FinalPriceDTO();
        finalPriceDTO.setFinalPrice(final_price);
        finalPriceDTO.setTotalPrice(basePrice);
        finalPriceDTO.setTotalDiscount(discount_value.add(code));
        finalPriceDTO.setTotalTax(tax);
        finalPriceDTO.setTotalShipping(shipping);

        return finalPriceDTO;

    }

}
