package com.vendora.price_service.exception;

public class PromoCodeNotStartedException extends RuntimeException {
    public PromoCodeNotStartedException(String message) {
        super(message);
    }
}