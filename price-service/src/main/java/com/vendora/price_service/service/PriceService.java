package com.vendora.price_service.service;


import com.vendora.price_service.DTO.FinalPriceDTO;
import com.vendora.price_service.DTO.PriceCalculateDTO;
import com.vendora.price_service.DTO.ShippingDTO;
import com.vendora.price_service.DTO.TaxDTO;
import com.vendora.price_service.entity.DiscountEntity;
import com.vendora.price_service.entity.PromoCodeEntity;
import com.vendora.price_service.entity.ShippingEntity;
import com.vendora.price_service.entity.TaxEntity;
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
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class PriceService {


    private static final Logger log = LoggerFactory.getLogger(PriceService.class);
    @Autowired
    private TaxRepo taxRepo;
    @Autowired
    private ShippingRepo shippingRepo;
    @Autowired
    private PromoCodeRepo promoCodeRepo;
    @Autowired
    private PromoCodeService promoCodeService;
    @Autowired
    private DiscountRepo discountRepo;
    @Autowired
    private WarehouseService warehouseService;

    public boolean haveDiscount(UUID product_id){
        return discountRepo.findByProductId(product_id).isPresent();
    }

    public FinalPriceDTO calculatePrice(PriceCalculateDTO request) {
        BigDecimal basePrice;
        try {
            basePrice = warehouseService.getProduct(request.getProductId()).getBody().getBasePrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Product not found: " + request.getProductId(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch product: " + e.getMessage(), e);
        }


        DiscountEntity discount;
        BigDecimal discount_value = BigDecimal.ZERO;
        if (haveDiscount(request.getProductId())) {
            discount = discountRepo.findByProductId(request.getProductId()).get();
            discount_value = BigDecimal.ZERO;
            if (Objects.equals(discount.getDiscountType(), "Percent")) {
                discount_value = basePrice
                        .divide(BigDecimal.valueOf(100), 2)
                        .multiply(discount.getDiscountValue());
            } else {
                discount_value = discount.getDiscountValue();
            }
        }

        BigDecimal code;
        //promocode
        if (promoCodeService.isPromoActive(request.getPromoCode())) {
            PromoCodeEntity promoCode = promoCodeRepo.findByCodeAndIsActive(request.getPromoCode(), true).get();
            if (Objects.equals(promoCode.getDiscountType(), "Percent")) {
                code = basePrice.multiply(promoCode.getDiscountValue()).divide(BigDecimal.valueOf(100), 2);
                System.out.println(code);
            } else {
                code = promoCode.getDiscountValue();
            }
        } else {
            code = BigDecimal.ZERO;
        }
        BigDecimal final_price = basePrice.subtract(discount_value).subtract(code);
        //tax
        BigDecimal tax = final_price
                .divide(BigDecimal.valueOf(100), 2)
                .multiply(taxRepo.findByRegion(request.getRegion()).orElseThrow().getRate());

        BigDecimal shipping = shippingRepo.findById(request.getShippingId()).get().getPrice();
        final_price = final_price.add(tax).add(shipping);

        FinalPriceDTO finalPriceDTO = new FinalPriceDTO();
        finalPriceDTO.setFinalPrice(final_price);
        finalPriceDTO.setTotalPrice(basePrice);
        finalPriceDTO.setTotalDiscount(discount_value.add(code));
        finalPriceDTO.setTotalTax(tax);
        finalPriceDTO.setTotalShipping(shipping);

        return finalPriceDTO;

    }



    public TaxEntity setTax(TaxDTO tax){
        if(taxRepo.findByRegion(tax.getRegion()).isPresent()){
            TaxEntity taxEntity = taxRepo.findByRegion(tax.getRegion()).get();
            return taxRepo.save(taxEntity);
        } else {
            TaxEntity taxEntity = new TaxEntity(tax.getRegion(), tax.getTax_type(), tax.getRate());
            return taxRepo.save(taxEntity);
        }
    }

    public ShippingEntity setShipping(ShippingDTO shipping){
        return shippingRepo.save(new ShippingEntity(shipping.getZone(), shipping.getWeightLimit(), shipping.getPrice(), shipping.getDeliveryType()));
    }

    public Iterable<ShippingEntity> getShippingList(){
        return shippingRepo.findAll();
    }


}
