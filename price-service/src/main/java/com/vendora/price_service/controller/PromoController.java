package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.PromoCreateDTO;
import com.vendora.price_service.repository.PromoCodeRepo;
import com.vendora.price_service.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promocode")
public class PromoController {

    @Autowired
    private PromoCodeService promoCodeService;
    @Autowired
    private PromoCodeRepo promoCodeRepo;

    @GetMapping("/active")
    public ResponseEntity getAllActivePromo(){
        try {
            return ResponseEntity.ok(promoCodeService.getActivePromo());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PutMapping("/update/{promo}")
    public ResponseEntity updatePromo(@PathVariable String promo){
        try {
            return ResponseEntity.ok(promoCodeService.updatePromo(promo));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @PostMapping ("/create")
    public ResponseEntity createPromo(@RequestBody PromoCreateDTO promo){
        try {
            return ResponseEntity.ok(promoCodeService.createPromo(promo));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
