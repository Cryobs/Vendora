package com.vendora.price_service.exception;

public class PromoCodeExpiredException extends RuntimeException {
    public PromoCodeExpiredException(String message) {
        super(message);
    }
}