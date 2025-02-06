package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.PromoCreateDTO;
import com.vendora.price_service.entity.PromoCodeEntity;
import com.vendora.price_service.repository.PromoCodeRepo;
import com.vendora.price_service.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/promocode")
public class PromoController {

    @Autowired
    private PromoCodeService promoCodeService;
    @Autowired
    private PromoCodeRepo promoCodeRepo;

    @GetMapping("/active")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ArrayList<PromoCodeEntity>> getAllActivePromo(){
        return ResponseEntity.ok(promoCodeService.getActivePromo());
    }

    @PutMapping("/update/{promo}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<PromoCodeEntity> updatePromo(@PathVariable String promo){
        return ResponseEntity.ok(promoCodeService.updatePromo(promo));
    }

    @PutMapping("/use/{promo}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Boolean> usePromo(@PathVariable String promo){
        return ResponseEntity.ok(promoCodeService.usePromo(promo));
    }

    @PostMapping ("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<PromoCodeEntity> createPromo(@RequestBody PromoCreateDTO promo){
        return ResponseEntity.ok(promoCodeService.createPromo(promo));
    }
}
